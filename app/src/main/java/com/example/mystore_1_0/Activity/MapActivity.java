package com.example.mystore_1_0.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import com.example.mystore_1_0.R;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mappa);


        Button btn = findViewById(R.id.btn_0_0);
        GridLayout gridLayout = findViewById(R.id.gridlayout);
        Log.d("childat", String.valueOf(gridLayout.getChildAt(1)));
        gridLayout.getChildAt(3).setVisibility(View.VISIBLE);

        btn.setVisibility(View.VISIBLE);



    }
}