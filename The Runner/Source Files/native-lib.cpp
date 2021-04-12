#include <jni.h>
#include <unistd.h>
#include <string>
#include <iostream>
#include <fstream>
#include <string>
#include <iosfwd>
using namespace std;

#define black 0
#define white 1
#define red 2
#define yello 3
#define green 4
#define blue 5d
#define pink 6
#define LED0 0
#define LED1 10
#define LED2 20
#define LED3 30
#define LED4 40
#define LED5 50
#define LED6 60
//syscall 322 -> seg
//syscall 314 -> led

extern "C"
JNIEXPORT void JNICALL
Java_com_vogella_android_therunner_Game_printtext(JNIEnv *env, jobject thiz, jint input) {
  syscall(322,input);
}extern "C"
JNIEXPORT void JNICALL
Java_com_vogella_android_therunner_Game_printled(JNIEnv *env, jobject thiz, jint input) {
   syscall(314,input);
}