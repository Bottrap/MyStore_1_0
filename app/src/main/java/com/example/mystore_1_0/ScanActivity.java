package com.example.mystore_1_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class ScanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //elimina barra di stato dal layout del cell
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_scan);
    }

    public void clickAdminBtn(View v){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void clickScanBtn(View v){
        Toast.makeText(this, "Non hai la fotocamera, coglione!", Toast.LENGTH_SHORT).show();
    }
}