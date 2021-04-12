package com.vogella.android.therunner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Game extends AppCompatActivity {
    ImageView places[] = new ImageView[24];
    Integer imglist[] = new Integer[43];
    Integer imgplace[] = new Integer[43];
    ImageView Number[] = new ImageView[42];
    Integer to14left;
    Integer to28left;
    Integer to41left;
    Integer Current;
    Integer State;
    Integer cardsleft;
    Integer drawlimit = 12;
    Integer matched;
    Integer todecide;
    Runner Computer;
    Chaser Player;

    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        Bundle getdata = getIntent().getExtras();
        if(getdata == null) Toast.makeText(getApplicationContext(),"load fail",Toast.LENGTH_SHORT).show();
        todecide = Integer.parseInt(getdata.getString(Intent.EXTRA_TEXT));
        places[0] = findViewById(R.id.placecard1);
        places[1] = findViewById(R.id.placecard2);
        places[2] = findViewById(R.id.placecard3);
        places[3] = findViewById(R.id.placecard4);
        places[4] = findViewById(R.id.placecard5);
        places[5] = findViewById(R.id.placecard6);
        places[6] = findViewById(R.id.placecard7);
        places[7] = findViewById(R.id.placecard8);
        places[8] = findViewById(R.id.placecard9);
        places[9] = findViewById(R.id.placecard10);
        places[10] = findViewById(R.id.placecard11);
        places[11] = findViewById(R.id.placecard12);
        places[12] = findViewById(R.id.placecard13);
        places[13] = findViewById(R.id.placecard14);
        places[14] = findViewById(R.id.placecard15);
        places[15] = findViewById(R.id.placecard16);
        places[16] = findViewById(R.id.placecard17);
        places[17] = findViewById(R.id.placecard18);
        places[18] = findViewById(R.id.placecard19);
        places[19] = findViewById(R.id.placecard20);
        places[20] = findViewById(R.id.placecard21);
        places[21] = findViewById(R.id.placecard22);
        places[22] = findViewById(R.id.placecard23);
        places[23] = findViewById(R.id.placecard24);
        Number[4] = findViewById(R.id.N4);
        Number[5] = findViewById(R.id.N5);
        Number[6] = findViewById(R.id.N6);
        Number[7] = findViewById(R.id.N7);
        Number[8] = findViewById(R.id.N8);
        Number[9] = findViewById(R.id.N9);
        Number[10] = findViewById(R.id.N10);
        Number[11] = findViewById(R.id.N11);
        Number[12] = findViewById(R.id.N12);
        Number[13] = findViewById(R.id.N13);
        Number[14] = findViewById(R.id.N14);
        Number[15] = findViewById(R.id.N15);
        Number[16] = findViewById(R.id.N16);
        Number[17] = findViewById(R.id.N17);
        Number[18] = findViewById(R.id.N18);
        Number[19] = findViewById(R.id.N19);
        Number[20] = findViewById(R.id.N20);
        Number[21] = findViewById(R.id.N21);
        Number[22] = findViewById(R.id.N22);
        Number[23] = findViewById(R.id.N23);
        Number[24] = findViewById(R.id.N24);
        Number[25] = findViewById(R.id.N25);
        Number[26] = findViewById(R.id.N26);
        Number[27] = findViewById(R.id.N27);
        Number[28] = findViewById(R.id.N28);
        Number[29] = findViewById(R.id.N29);
        Number[30] = findViewById(R.id.N30);
        Number[31] = findViewById(R.id.N31);
        Number[32] = findViewById(R.id.N32);
        Number[33] = findViewById(R.id.N33);
        Number[34] = findViewById(R.id.N34);
        Number[35] = findViewById(R.id.N35);
        Number[36] = findViewById(R.id.N36);
        Number[37] = findViewById(R.id.N37);
        Number[38] = findViewById(R.id.N38);
        Number[39] = findViewById(R.id.N39);
        Number[40] = findViewById(R.id.N40);
        Number[41] = findViewById(R.id.N41);
        imglist[0] = R.drawable.c0;
        imglist[1] = R.drawable.c1;
        imglist[2] = R.drawable.c2;
        imglist[3] = R.drawable.c3;
        imglist[4] = R.drawable.c4;
        imglist[5] = R.drawable.c5;
        imglist[6] = R.drawable.c6;
        imglist[7] = R.drawable.c7;
        imglist[8] = R.drawable.c8;
        imglist[9] = R.drawable.c9;
        imglist[10] = R.drawable.c10;
        imglist[11] = R.drawable.c11;
        imglist[12] = R.drawable.c12;
        imglist[13] = R.drawable.c13;
        imglist[14] = R.drawable.c14;
        imglist[15] = R.drawable.c15;
        imglist[16] = R.drawable.c16;
        imglist[17] = R.drawable.c17;
        imglist[18] = R.drawable.c18;
        imglist[19] = R.drawable.c19;
        imglist[20] = R.drawable.c20;
        imglist[21] = R.drawable.c21;
        imglist[22] = R.drawable.c22;
        imglist[23] = R.drawable.c23;
        imglist[24] = R.drawable.c24;
        imglist[25] = R.drawable.c25;
        imglist[26] = R.drawable.c26;
        imglist[27] = R.drawable.c27;
        imglist[28] = R.drawable.c28;
        imglist[29] = R.drawable.c29;
        imglist[30] = R.drawable.c30;
        imglist[31] = R.drawable.c31;
        imglist[32] = R.drawable.c32;
        imglist[33] = R.drawable.c33;
        imglist[34] = R.drawable.c34;
        imglist[35] = R.drawable.c35;
        imglist[36] = R.drawable.c36;
        imglist[37] = R.drawable.c37;
        imglist[38] = R.drawable.c38;
        imglist[39] = R.drawable.c39;
        imglist[40] = R.drawable.c40;
        imglist[41] = R.drawable.c41;
        imglist[42] = R.drawable.c42;
        for(int i=0;i<43;i++) imgplace[i] = 23;
        if(todecide == 4) {
            load_game();
        }
        else{
            initialize_game(todecide);
            Play_game();
        }
    }


    public void onClick(View view) throws IOException{
        switch(view.getId()){
            case R.id.guessbutton:
                if(this.State == 1){
                    TextView t = findViewById(R.id.guessnum);
                    String text = t.getText().toString();
                    if(text.getBytes().length <= 0){
                        break;
                    }
                    Integer guessnum =  Integer.parseInt(text);
                    if(guessnum>41){
                        Toast.makeText(getApplicationContext(),"You should guess number lower then 42",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if(places[imgplace[guessnum]].getVisibility() == View.VISIBLE && Computer.Runner_cards.contains(guessnum) && !Player.Chased.contains(guessnum)){
                        this.matched++;
                        printtext(1);
                        printled(1);
                        Player.Chased.add(guessnum);
                        places[imgplace[guessnum]].setImageResource(imglist[guessnum]);
                        this.cardsleft--;
                        if(this.cardsleft == 0){
                            Toast.makeText(getApplicationContext(),"You Win!!",Toast.LENGTH_LONG).show();
                            Button btn = (Button) findViewById(R.id.guessbutton);
                            btn.setText("Replay");
                            this.State = 3;
                            printtext(3);
                            printled(3);
                            break;
                        }
                        else storestate();
                        break;
                    }
                    else{
                        printtext(2);
                        printled(2);
                        this.Current++;
                        this.cardsleft++;
                        places[this.Current].setVisibility(View.VISIBLE);
                        if(Computer.Runner_cards.get(this.Current) == (Integer)42){
                            printtext(3);
                            printled(3);
                            places[this.Current].setImageResource(imglist[42]);
                            Toast.makeText(getApplicationContext(),"You LOSE!!",Toast.LENGTH_LONG).show();
                            Button btn = (Button) findViewById(R.id.guessbutton);
                            btn.setText("Replay");
                            this.State = 3;
                            break;
                        }
                        if(Player.Chaser_cards.size() == drawlimit){
                            TextView t1 = findViewById(R.id.textView9);
                            TextView t2 = findViewById(R.id.textView10);
                            TextView t3 = findViewById(R.id.textView11);
                            t1.setText("15 Cards");
                            t2.setText("is all");
                            t3.setText("you can have");
                            break;
                        }
                        else this.State = 2;
                        break;
                    }
                }
                else if(this.State == 3){
                    initialize_game(2);
                    Play_game();
                    break;
                }
                break;
            case R.id.to14btn:
                if(this.State == 0){
                    if(to14left == 0){
                        Toast.makeText(getApplicationContext(),"No more Card left here!",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if(Player.Chaser_cards.size() == 1){
                        Random rand = new Random();
                        Integer pick = (Integer)rand.nextInt(11)+4;
                        while(Computer.Runner_cards.contains(pick) || Player.Chaser_cards.contains(pick)){
                            pick = (Integer)rand.nextInt(11)+4;
                        }
                        Number[pick].setVisibility(View.VISIBLE);
                        Player.Chaser_cards.add(pick);
                        this.to14left--;
                        this.State = 1;
                    }
                    else{
                        Random rand = new Random();
                        Integer pick = (Integer)rand.nextInt(11)+4;
                        while(Computer.Runner_cards.contains(pick) || Player.Chaser_cards.contains(pick)){
                            pick = (Integer)rand.nextInt(11)+4;
                        }
                        Number[pick].setVisibility(View.VISIBLE);
                        Player.Chaser_cards.add(pick);
                        this.to14left--;
                    }
                    break;
                }
                else if(this.State == 2){
                    if(to14left == 0){
                        Toast.makeText(getApplicationContext(),"No more Card left here!",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    Random rand = new Random();
                    Integer pick = (Integer)rand.nextInt(11)+4;
                    while(Computer.Runner_cards.contains(pick) || Player.Chaser_cards.contains(pick)){
                        pick = (Integer)rand.nextInt(11)+4;
                    }
                    Number[pick].setVisibility(View.VISIBLE);
                    Player.Chaser_cards.add(pick);
                    this.to14left--;
                    this.State = 1;
                    break;
                }
                break;
            case R.id.to28btn:
                if(this.State == 0){
                    if(to28left == 0){
                        Toast.makeText(getApplicationContext(),"No more Card left here!",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if(Player.Chaser_cards.size() == 1){
                        Random rand = new Random();
                        Integer pick = (Integer)rand.nextInt(14)+15;
                        while(Computer.Runner_cards.contains(pick) || Player.Chaser_cards.contains(pick)){
                            pick = (Integer)rand.nextInt(14)+15;
                        }
                        Number[pick].setVisibility(View.VISIBLE);
                        Player.Chaser_cards.add(pick);
                        this.State = 1;
                        this.to28left--;
                    }
                    else{
                        Random rand = new Random();
                        Integer pick = (Integer)rand.nextInt(14)+15;
                        while(Computer.Runner_cards.contains(pick) || Player.Chaser_cards.contains(pick)){
                            pick = (Integer)rand.nextInt(14)+15;
                        }
                        Number[pick].setVisibility(View.VISIBLE);
                        Player.Chaser_cards.add(pick);
                        this.to28left--;
                    }
                    break;

                }
                else if(this.State == 2){
                    if(to28left == 0){
                        Toast.makeText(getApplicationContext(),"No more Card left here!",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    Random rand = new Random();
                    Integer pick = (Integer)rand.nextInt(14)+15;
                    while(Computer.Runner_cards.contains(pick) || Player.Chaser_cards.contains(pick)){
                        pick = (Integer)rand.nextInt(14)+15;
                    }
                    Number[pick].setVisibility(View.VISIBLE);
                    Player.Chaser_cards.add(pick);
                    this.State = 1;
                    this.to28left--;
                    break;
                }
                break;
            case R.id.to41btn:
                if(this.State == 0){
                    if(to41left == 0){
                        Toast.makeText(getApplicationContext(),"No more Card left here!",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if(Player.Chaser_cards.size() == 1){
                        Random rand = new Random();
                        Integer pick = (Integer)rand.nextInt(13)+29;
                        while(Computer.Runner_cards.contains(pick) || Player.Chaser_cards.contains(pick)){
                            pick = (Integer)rand.nextInt(13)+29;
                        }
                        Number[pick].setVisibility(View.VISIBLE);
                        Player.Chaser_cards.add(pick);
                        this.State = 1;
                        this.to41left--;
                    }
                    else{
                        Random rand = new Random();
                        Integer pick = (Integer)rand.nextInt(13)+29;
                        while(Computer.Runner_cards.contains(pick) || Player.Chaser_cards.contains(pick)){
                            pick = (Integer)rand.nextInt(13)+29;
                        }
                        Number[pick].setVisibility(View.VISIBLE);
                        Player.Chaser_cards.add(pick);
                        this.to41left--;
                    }
                    break;
                }
                else if(this.State == 2){
                    if(to41left == 0){
                        Toast.makeText(getApplicationContext(),"No more Card left here!",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    Random rand = new Random();
                    Integer pick = (Integer)rand.nextInt(13)+29;
                    while(Computer.Runner_cards.contains(pick) || Player.Chaser_cards.contains(pick)){
                        pick = (Integer)rand.nextInt(13)+29;
                    }
                    Number[pick].setVisibility(View.VISIBLE);
                    Player.Chaser_cards.add(pick);
                    this.State = 1;
                    this.to41left--;
                    break;
                }
                break;
        }
    }

    public void load_game(){
        StringBuffer buffer = new StringBuffer();
        String data = null;
        FileInputStream fis = null;
        try {
            fis = openFileInput("internal.txt");
            BufferedReader iReader = new BufferedReader(new InputStreamReader((fis)));

            data = iReader.readLine();
            while(data != null) {
                buffer.append(data);
                data = iReader.readLine();
            }
            iReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Can't find a stored game",Toast.LENGTH_LONG);
            initialize_game(1);
            Play_game();
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] meta_load = buffer.toString().split("\\|");
        this.cardsleft = Integer.parseInt(meta_load[0]);
        this.Current = Integer.parseInt(meta_load[1]);
        this.to14left = Integer.parseInt(meta_load[2]);
        this.to28left = Integer.parseInt(meta_load[3]);
        this.to41left = Integer.parseInt(meta_load[4]);
        this.Player = new Chaser();
        this.Computer = new Runner(3);
        this.Computer.Runner_cards = new ArrayList<Integer>();
        String[] to_Runner_cards = meta_load[5].substring(1,meta_load[5].lastIndexOf("]")).split(", ");
        for(String num : to_Runner_cards){
            this.Computer.Runner_cards.add(Integer.parseInt(num));
        }
        this.Player.Chaser_cards = new ArrayList<Integer>();
        String[] to_Chaser_cards = meta_load[6].substring(1,meta_load[6].lastIndexOf("]")).split(", ");
        for(String num : to_Chaser_cards){
            this.Player.Chaser_cards.add(Integer.parseInt(num));
        }
        this.matched = Integer.parseInt(meta_load[7]);
        this.todecide = Integer.parseInt(meta_load[8]);
        this.Player.Chased = new ArrayList<Integer>();
        String[] to_Chased = meta_load[9].substring(1,meta_load[9].lastIndexOf("]")).split(", ");
        for(String num : to_Chased){
            this.Player.Chased.add(Integer.parseInt(num));
        }
        for(int i=1;i<24;i++){
            places[i].setVisibility(View.GONE);
            places[i].setImageResource(R.drawable.back);
        }
        for(int i=4;i<42;i++){
            Number[i].setVisibility(View.GONE);
        }
        for(int i=0;i<this.Current+1;i++)places[i].setVisibility(View.VISIBLE);
        for(int i=0;i<this.Computer.Runner_cards.size();i++) imgplace[this.Computer.Runner_cards.get(i)] = i+1;
        for(int i=1;i<this.matched+1;i++)places[imgplace[this.Player.Chased.get(i-1)]].setImageResource(imglist[this.Player.Chased.get(i-1)]);
        for(int i=0;i<this.Player.Chaser_cards.size();i++) Number[this.Player.Chaser_cards.get(i)].setVisibility(View.VISIBLE);
        this.State = 1;
    }

    public void initialize_game(int level){
        for(int i=0;i<43;i++) imgplace[i] = 23;
        this.to14left = 11;
        this.to28left = 14;
        this.to41left = 14;
        this.matched = 0;
        for(int i=1;i<24;i++){
            places[i].setVisibility(View.GONE);
            places[i].setImageResource(R.drawable.back);
        }
        Player = new Chaser();
        Computer = new Runner(2 + level);
        while(Computer.Runner_cards.size() > (24-2*level)) Computer = new Runner(2 + level);
        for(int i=0;i<Computer.Runner_cards.size();i++){
            int cardnum = (int)Computer.Runner_cards.get(i);
            imgplace[cardnum] = i+1;
            if(cardnum<15){
                this.to14left--;
            }
            else if(14<cardnum && cardnum<29){
                this.to28left--;
            }
            else{
                this.to41left--;
            }
        }
        for(int i=4;i<42;i++){
            Number[i].setVisibility(View.GONE);
        }
        Button btn = (Button) findViewById(R.id.guessbutton);
        btn.setText("Guess!");
    }

    public void Play_game() {
        this.State = 0;
        this.cardsleft = 3;
        this.Current = 3;
        places[1].setVisibility(View.VISIBLE);
        places[2].setVisibility(View.VISIBLE);
        places[3].setVisibility(View.VISIBLE);
        Toast.makeText(getApplicationContext(),"Pick Two Cards!",Toast.LENGTH_LONG).show();
    }

    public void storestate(){
        FileOutputStream fos = null;
        try {
            fos = openFileOutput("internal.txt", Context.MODE_PRIVATE);
            StringBuilder tostore = new StringBuilder();
            tostore.append(this.cardsleft.toString()+"|");
            tostore.append(this.Current.toString()+"|");
            tostore.append(this.to14left+"|");
            tostore.append(this.to28left+"|");
            tostore.append(this.to41left+"|");
            tostore.append(this.Computer.Runner_cards.toString()+"|");
            tostore.append(this.Player.Chaser_cards.toString()+"|");
            tostore.append(this.matched.toString()+"|");
            tostore.append(this.todecide.toString()+"|");
            tostore.append(this.Player.Chased.toString());
            fos.write(tostore.toString().getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public native void printtext(int input);
    public native void printled(int input);
}



class Chaser{
    List<Integer> Chaser_cards = new ArrayList<Integer>();
    List<Integer> Chased = new ArrayList<Integer>();
    public Chaser(){

    }
}

class Runner{
    List<Integer> Runner_cards = new ArrayList<Integer>();
    Integer starting;
    public Runner(int level){
        Random rand = new Random();
        Integer Lastcard;
        this.starting = (Integer)rand.nextInt(3) + 1;
        if(starting == 1 ){
            Lastcard = 1;
            this.Runner_cards.add(1);
        }
        else if(starting == 2){
            Lastcard = 2;
            this.Runner_cards.add(2);
        }
        else{
            Lastcard = 3;
            this.Runner_cards.add(3);
        }
        while(Lastcard < 39){
            Integer newcard = Lastcard + ((int)rand.nextInt(level)) + 1;
            this.Runner_cards.add(newcard);
            Lastcard = newcard;
        }
        this.Runner_cards.add(42);
    }
}



/*
public void onClick(View view) throws IOException {
        switch(view.getId()){
            case R.id.read:
                StringBuffer buffer = new StringBuffer();
                String data = null;
                FileInputStream fis = null;
                try {
                    fis = openFileInput("internal.txt");
                    BufferedReader iReader = new BufferedReader(new InputStreamReader((fis)));

                    data = iReader.readLine();
                    while(data != null)
                    {
                        buffer.append(data);
                        data = iReader.readLine();
                    }
                    text.setText(buffer);
                    iReader.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            break;
nteger
            case R.id.write:
                FileOutputStream fos = null;
                try {
                    fos = openFileOutput("internal.txt", Context.MODE_PRIVATE);
                    fos.write("testing".getBytes());
                    fos.close();;

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            break;
        }
    }
 */
