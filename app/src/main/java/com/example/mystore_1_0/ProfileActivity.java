package com.example.mystore_1_0;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

public class ProfileActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       /* TextView testo;
        @Override
        protected void onCreate (Bundle savedInstanceState){​​​​
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_user);
            Intent intent = getIntent();
            String nomeUtente = intent.getStringExtra("id");
            testo = findViewById(R.id.testo);
            testo.setText(nomeUtente);
        */

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);



        }
    }