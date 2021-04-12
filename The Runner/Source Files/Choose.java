package com.vogella.android.therunner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Choose extends AppCompatActivity{
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose);

        Button imageButton1 = (Button) findViewById(R.id.easy);
        Button imageButton2 = (Button) findViewById(R.id.normal);
        Button imageButton3 = (Button) findViewById(R.id.hard);
        imageButton1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), game_manual.class);
                intent.putExtra(Intent.EXTRA_TEXT,"1");
                startActivity(intent);
            }
        });

        imageButton2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), game_manual.class);
                intent.putExtra(Intent.EXTRA_TEXT,"2");
                startActivity(intent);
            }
        });

        imageButton3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), game_manual.class);
                intent.putExtra(Intent.EXTRA_TEXT,"3");
                startActivity(intent);
            }
        });
    }
}
