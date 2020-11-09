package com.example.firstapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class StartScreen extends AppCompatActivity {

    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);

        //SharedPreferences sharepref = getSharedPreferences("bgcolorfile", Context.MODE_PRIVATE);
        //SharedPreferences.Editor editor = sharepref.edit();
        //editor.putInt("color2", getResources().getColor(R.color.colorWhite));

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(StartScreen.this, MainScreen.class);
                startActivity(intent);
                finish();
            }
        }, 3000);





    }



}