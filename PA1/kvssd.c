#include "migration/qemu-file.h"
#include "hw/android/goldfish/device.h"
#include "hw/android/goldfish/nand.h"
#include "hw/android/goldfish/vmem.h"
#include "hw/hw.h"
#include "hw/irq.h"
#include "android/utils/path.h"
#include "android/utils/tempfile.h"
#include "android/qemu-debug.h"
#include "android/android.h"
#include "android/skin/event.h"
#include <stdio.h>
#include <stdint.h>
#include <string.h>
extern int SDL_SendCustomEvent(int code, void* data);

#define N_BLK		20
#define N_KEY		20

#define CMD_PUT		3
#define CMD_GET		4
#define CMD_ERASE	5
#define CMD_EXIST	6

#define DEV_BUSY	1
#define DEV_READY	0
#define DEV_WAIT	2


enum{
	KVSSD_STATUS_REG	=0x00,
	KVSSD_CMD_REG		=0x04,
	KVSSD_KEY_REG		=0x08,
	KVSSD_VALUE_REG		=0x0C,
};

struct goldfish_kvssd_state {
	struct goldfish_device dev;
	uint8_t exist;
	uint16_t key_index;
	uint16_t val_index;
	int kvssd_meta_fd;
	int kvssd_data_fd;
	uint32_t free_blk;
	uint32_t status; 
	uint32_t cmd;	
	uint32_t key[4];
	uint32_t value[1024];
};

struct kvssd_meta_data{
	uint32_t key[4];
};

struct kvssd_meta_data *k2p;

uint32_t new_blk_ptr=0;
int checking_process,check_key = 0;
char blk_valid[N_BLK];

#define KVSSD_STATE_SAVE_VERSION	3
#define QFIELD_STRUCT	struct goldfish_kvssd_state

QFIELD_BEGIN(goldfish_kvssd_fields)
	QFIELD_INT32(status),
	QFIELD_INT32(cmd),
	QFIELD_INT32(free_blk),
	QFIELD_INT16(key_index),
	QFIELD_INT16(val_index),
QFIELD_END



void garbage_collection(struct goldfish_kvssd_state* s)
{
	int idx,i;
	for(idx=0;idx<N_BLK;idx++){
		if(blk_valid[idx] == 'I'){
			lseek(s->kvssd_meta_fd,idx,SEEK_SET);
			write(s->kvssd_meta_fd,"0",1);
			lseek(s->kvssd_meta_fd,N_BLK+1+(idx*(sizeof(uint32_t)*4+1)),SEEK_SET);
			write(s->kvssd_meta_fd,"0000000000000000\n",17);
			lseek(s->kvssd_data_fd,idx*((sizeof(uint32_t)*1024)+1),SEEK_SET);
			for(i=0;i<1024;i++) write(s->kvssd_data_fd,"0000",sizeof(uint32_t));
			write(s->kvssd_data_fd,"\n",1);
			blk_valid[idx] = 'E';
			s->free_blk++;
		}
	}
}

static void goldfish_kvssd_save(QEMUFile* f, void* opaque)
{
	struct goldfish_kvssd_state* s = opaque;
	/* TODO */
	qemu_put_buffer(f, (uint8_t*)s->key, 4 * 4);
	qemu_put_buffer(f, (uint8_t*)s->value, 1024 * 4);
	qemu_put_struct(f, goldfish_kvssd_fields, s);
}

static int goldfish_kvssd_load(QEMUFile* f, void* opaque, int version_id)
{
	struct goldfish_kvssd_state* s = opaque;
	if ( version_id != KVSSD_STATE_SAVE_VERSION )
		return -1;
	/* TODO */
	qemu_get_buffer(f, (uint8_t*)s->key, 4 * 4);
	qemu_get_buffer(f, (uint8_t*)s->value, 1024 * 4);
	qemu_get_struct(f, goldfish_kvssd_fields, s);
	return 0;
}
/////////////
static uint32_t goldfish_kvssd_read(void* opaque, hwaddr offset)
{
	struct goldfish_kvssd_state* s = (struct goldfish_kvssd_state*)opaque;
	uint32_t temp;

	if ( offset < 0 ) {
		cpu_abort(cpu_single_env, "kvssd_dev_read: Bad offset %" HWADDR_PRIx "\n", offset);
		return 0;
	}

	switch (offset) {
		case KVSSD_STATUS_REG:
			return s->status;
		case KVSSD_KEY_REG:
			return s->exist;
		case KVSSD_VALUE_REG:
			temp = s->value[s->val_index];
			s->val_index = (s->val_index+1)%1024;
			return temp;
	};

	return 0;
}

static void goldfish_kvssd_write(void* opaque, hwaddr offset, uint32_t value)
{
	struct goldfish_kvssd_state* s = (struct goldfish_kvssd_state*)opaque;
	int idx,i;
	char *buf = (char*)malloc(sizeof(uint32_t));


	if ( offset < 0 ) {
		cpu_abort(cpu_single_env, "iodev_dev_read: Bad offset %" HWADDR_PRIx "\n", offset);
		return;
	}
	switch (offset) {
		case KVSSD_STATUS_REG:
			if(s->status == DEV_WAIT){
				if(checking_process == 3){
					if(check_key == 3){
						s->status = DEV_READY;
					}
					check_key = 0;
					checking_process = 0;
				}
				else{
					if(s->key[checking_process] == value) check_key++;
					checking_process++;
				}
			}
			break;
		case KVSSD_CMD_REG:
			switch(value){
				case CMD_PUT:
					if(s->free_blk == 1) garbage_collection(s); 
					s->exist = 0;
					for(idx=0;idx<N_BLK;idx++)
					{
						if(blk_valid[idx] == 'V' && memcmp(&s->key[0],&k2p[idx].key[0],sizeof(uint32_t)*4) == 0){
							blk_valid[idx] = 'I';
							lseek(s->kvssd_meta_fd,idx,SEEK_SET);
							write(s->kvssd_meta_fd,"2",1);
							s->exist = 1;
							break;
						}
					}
					memcpy(&k2p[new_blk_ptr].key[0],&s->key[0],4*sizeof(uint32_t));
					blk_valid[new_blk_ptr] = 'V';
					lseek(s->kvssd_data_fd,(new_blk_ptr)*(1024*sizeof(uint32_t)+1),SEEK_SET);
					for(idx=0;idx<1024;idx++){
						memcpy(buf,&s->value[idx],sizeof(uint32_t));
						write(s->kvssd_data_fd,buf,sizeof(uint32_t));
					}
					write(s->kvssd_data_fd,"\n",1);///data
					lseek(s->kvssd_meta_fd,new_blk_ptr,SEEK_SET);
					write(s->kvssd_meta_fd,"1",1);
					lseek(s->kvssd_meta_fd,N_BLK+(new_blk_ptr*4*sizeof(uint32_t)+1),SEEK_SET);
					write(s->kvssd_meta_fd,&s->key[0],sizeof(uint32_t)*4);
					for(idx=0;idx<1024;idx++){
						new_blk_ptr = (new_blk_ptr + 1)%1024;
						if(blk_valid[new_blk_ptr] == 'E') break;
					}
					s->free_blk--;
					s->status = DEV_READY;
					break;

				case CMD_GET:
					s->exist = 0;
					for(idx=0;idx<N_BLK;idx++){
						if(blk_valid[idx] == 'V' && memcmp(&s->key[0],&k2p[idx].key[0],sizeof(uint32_t)*4) == 0){
							s->exist = 1;
							lseek(s->kvssd_data_fd,idx*(1024*sizeof(uint32_t)+1),SEEK_SET);
							for(i=0;i<1024;i++){
								read(s->kvssd_data_fd,buf,sizeof(uint32_t));
								memcpy(&s->value[i],buf,sizeof(uint32_t));
							}
							break;
						}
					}
					s->status = DEV_WAIT;
					break;

				case CMD_ERASE:
					s->exist = 0;
					for(idx=0;idx<N_BLK;idx++){
						if(blk_valid[idx] == 'V' && memcmp(&s->key[0],&k2p[idx].key[0],sizeof(uint32_t)*4) == 0){
							s->exist = 1;
							blk_valid[idx] = 'I';
							lseek(s->kvssd_meta_fd,idx,SEEK_SET);
							write(s->kvssd_meta_fd,"2",1);
							break;
						}
					}
					s->status = DEV_READY;
					break;

				case CMD_EXIST:
					s->exist = 0;
					for(idx=0;idx<N_BLK;idx++){
						if(blk_valid[idx] == 'V' && memcmp(&s->key[0],&k2p[idx].key[0],sizeof(uint32_t)*4) == 0){
							s->exist = 1;
							break;
						}
					}
					s->status = DEV_WAIT;
					break;
			}
			goldfish_device_set_irq(&s->dev, 0, 1);
			break;
		case KVSSD_KEY_REG:
			s->key[s->key_index] = value;
			s->key_index = (s->key_index + 1)%4;
			break;
		case KVSSD_VALUE_REG:
			s->value[s->val_index] = value;
			s->val_index = (s->val_index + 1)%1024;
			break;
	}
	free(buf);
}

static CPUReadMemoryFunc *goldfish_kvssd_readfn[] = {
	goldfish_kvssd_read,
	goldfish_kvssd_read,
	goldfish_kvssd_read
};

static CPUWriteMemoryFunc *goldfish_kvssd_writefn[] = {
	goldfish_kvssd_write,
	goldfish_kvssd_write,
	goldfish_kvssd_write
};

void goldfish_kvssd_init(void)
{
	int idx,i;
	k2p = (struct kvssd_meta_data*)malloc(sizeof(struct kvssd_meta_data)* N_BLK);
	struct goldfish_kvssd_state *s;
	new_blk_ptr = 0;
	s = (struct goldfish_kvssd_state *)g_malloc0(sizeof(*s));
	s->dev.name = "goldfish_kvssd";
	s->dev.base = 0;
	s->dev.size = 0x1000;
	s->dev.irq_count = 1;
	s->dev.irq = 15;
	s->free_blk = N_BLK;
	s->kvssd_data_fd = open("/home/han/emu-2.2-release/external/qemu/kvssd.data", O_RDWR | O_CREAT, 0777);
	s->kvssd_meta_fd = open("/home/han/emu-2.2-release/external/qemu/kvssd.meta", O_RDWR | O_CREAT, 0777);
	goldfish_device_add(&s->dev, goldfish_kvssd_readfn, goldfish_kvssd_writefn, s);
	char *buf = (char*)malloc(sizeof(uint32_t));
	char *valid_check = (char*)malloc(sizeof(char)*N_BLK);
	int ret = read(s->kvssd_meta_fd,valid_check,sizeof(char)*N_BLK);
	if(ret == 0){
		for(idx=0;idx<N_BLK;idx++) write(s->kvssd_meta_fd,"0",1); //0 empty 1 vaild 2 invaild
		write(s->kvssd_meta_fd,"\n",1);
		for(idx=0;idx<N_BLK;idx++){
			write(s->kvssd_meta_fd,"0000000000000000\n",(4*sizeof(uint32_t))+1);
			for(i=0;i<4;i++) k2p[idx].key[i] = 0;
			blk_valid[idx] = 'E';
		}
		
	}
	else{
		for(idx=0;idx<N_BLK;idx++){
			switch(valid_check[idx]){
				case '0':
					blk_valid[idx] = 'E';
					break;
				case '1':
					s->free_blk--;
					blk_valid[idx] = 'V';
					break;
				case '2':
					s->free_blk--;
					blk_valid[idx] = 'I';
					break;
			}
		}
		lseek(s->kvssd_meta_fd,1,SEEK_CUR);
		for(idx=0;idx<N_BLK;idx++){
			if(blk_valid[idx] == 'V'){
				for(i=0;i<4;i++){
					read(s->kvssd_meta_fd,buf,sizeof(uint32_t));
					memcpy(&k2p[idx].key[i],buf,sizeof(uint32_t));
				}
			}
		}
	}


	for(new_blk_ptr=0;new_blk_ptr<N_BLK;new_blk_ptr++) if(blk_valid[new_blk_ptr] == 'E') break;
	for(idx=0;idx<N_BLK;idx++){
		if(blk_valid[idx] == 'V') lseek(s->kvssd_data_fd,sizeof(uint32_t)*1024+1,SEEK_CUR);
		else{
			for(i=0;i<sizeof(uint32_t)*1024;i++) write(s->kvssd_data_fd,"0000",sizeof(uint32_t));
			write(s->kvssd_data_fd,"\n",1);
		}
	};

	register_savevm(NULL, 
			"goldfish_kvssd",
			0,
			KVSSD_STATE_SAVE_VERSION,
			goldfish_kvssd_save,
			goldfish_kvssd_load,
			s);
}
