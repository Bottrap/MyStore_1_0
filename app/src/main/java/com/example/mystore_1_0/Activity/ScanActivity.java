package com.example.mystore_1_0.Activity;

import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.mystore_1_0.Capture;
import com.example.mystore_1_0.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
    }

    public void clickAdminBtn(View v){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void clickScanBtn(View v){
        Intent intent = new Intent(getApplicationContext(), MapActivity.class);    //temporaneamente il bottone apre solo l'activity mappa
        startActivity(intent);
        /*
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("Scansiona il QR code");
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setCaptureActivity(Capture.class);
        intentIntegrator.initiateScan();
         */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult.getContents() != null){
            Toast.makeText(this, "Il risultato della scansione è " + intentResult.getContents(), Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "La scansione non è andata a buon fine!", Toast.LENGTH_SHORT).show();
        }
    }

    public void apriGridLayout(View view) {
        Intent intent = new Intent(getApplicationContext(), MapActivity.class);
        startActivity(intent);
    }
}