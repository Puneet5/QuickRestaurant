package com.example.hp.quick_rest;

import android.content.Intent;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;



public class Launch extends AppCompatActivity {



    Handler handler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);


        handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                Intent intent = new Intent(Launch.this,First_page.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        };
        handler.postDelayed(r, 3000);
    }
}





