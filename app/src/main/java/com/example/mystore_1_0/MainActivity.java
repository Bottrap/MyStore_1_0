package com.example.mystore_1_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static int DELAY = 5000;

    public void runActivityLogin(){
        Intent intent = new Intent (this, MainActivityLogin.class);
        startActivity(intent);
    }

    //Variabili
    Animation topAnim, bottomAnim;
    ImageView logo;
    TextView benv, store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //Animation
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        //
        logo = findViewById(R.id.logo);
        benv = findViewById(R.id.benv);
        store = findViewById(R.id.myStore);

        logo.setAnimation(topAnim);
        benv.setAnimation(bottomAnim);
        store.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runActivityLogin();
                finish();
            }
        },DELAY);

    }
}