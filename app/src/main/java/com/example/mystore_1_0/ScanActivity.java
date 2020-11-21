package com.example.mystore_1_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class ScanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        Window window = this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // aggiunge il flag FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS alla window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finalmente cambia il colore
        window.setStatusBarColor(this.getResources().getColor(R.color.scanStatusBarColor));

    }

    public void clickAdminBtn(View v){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void clickScanBtn(View v){
        Toast.makeText(this, "Non hai la fotocamera, coglione!", Toast.LENGTH_SHORT).show();
    }
}