package com.foodo.foodo;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class startup extends AppCompatActivity {
    private static int time_out = 800;
    private TextView logo1,logo2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        logo1 = findViewById(R.id.logo1);
        logo2 = findViewById(R.id.logo2);
        Animation fade_in = AnimationUtils.loadAnimation(this,R.anim.fadein);
        logo1.startAnimation(fade_in);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent intent = new Intent(startup.this, Main.class);
                startActivity(intent);
                finish();
            }
        },time_out);
        logo2.startAnimation(fade_in);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent intent = new Intent(startup.this, Main.class);
                startActivity(intent);
                finish();
            }
        },time_out);
    }
}
