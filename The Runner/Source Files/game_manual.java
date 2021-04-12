package com.vogella.android.therunner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class game_manual extends AppCompatActivity{
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_manual);
        Bundle getdata = getIntent().getExtras();
        if(getdata == null) Toast.makeText(getApplicationContext(),"load fail",Toast.LENGTH_SHORT).show();
        final String todecide = getdata.getString(Intent.EXTRA_TEXT);
        TextView leveltext = findViewById(R.id.level);
        switch(Integer.parseInt(todecide)){
            case 1:
                leveltext.setText("필드에 놓인 카드 사이 간격은 최대 <이지모드> 3칸 입니다");
                break;
            case 2:
                leveltext.setText("필드에 놓인 카드 사이 간격은 최대 <노멀모드> 4칸 입니다");
                break;
            case 3:
                leveltext.setText("필드에 놓인 카드 사이 간격은 최대 <하드모드> 5칸 입니다");
                break;
        }
        Button imageButton = (Button) findViewById(R.id.startgame);
        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Game.class);
                intent.putExtra(Intent.EXTRA_TEXT,todecide);
                startActivity(intent);
            }
        });
    }
}
