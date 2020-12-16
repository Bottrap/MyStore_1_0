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


        //Button button00 = findViewById(R.id.button_prova);
        GridLayout gridLayout = findViewById(R.id.gridlayout);

        gridLayout.getChildAt(1).setVisibility(View.VISIBLE);
        gridLayout.getChildAt(1).setBackgroundColor(getResources().getColor(R.color.transparent));
        gridLayout.getChildAt(23).setVisibility(View.VISIBLE);
        gridLayout.getChildAt(23).setBackgroundColor(getResources().getColor(R.color.transparent));


        //gridLayout.getChildAt(11).setVisibility(View.INVISIBLE);
        //gridLayout.getChildAt(12).setVisibility(View.INVISIBLE);
        //gridLayout.getChildAt(10).getLayoutParams();
        Log.d("X",String.valueOf(gridLayout.getChildAt(10).getLayoutParams()));
        Log.d("material",String.valueOf(gridLayout.getChildAt(23)));
        Log.d("count", String.valueOf(gridLayout.getChildCount()));



    }
}