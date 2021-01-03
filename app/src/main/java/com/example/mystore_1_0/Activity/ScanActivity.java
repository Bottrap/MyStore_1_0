package com.example.mystore_1_0.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.mystore_1_0.Capture;
import com.example.mystore_1_0.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScanActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
    }

    public void clickAdminBtn(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

        // PER FAR PARTIRE MAP ACTIVITY SENZA QR CODE
        /*
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("negozio", "store1");
        startActivity(intent);

         */
    }


    public void clickScanBtn(View v) {
        Intent intent = new Intent(getApplicationContext(), TestActivity.class);  //AVVIA L'ACTIVITY TEST
        startActivity(intent);
        /*IntentIntegrator intentIntegrator = new IntentIntegrator(this);
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
        if (intentResult.getContents() != null) {
            Intent intent = new Intent(getApplicationContext(), TestActivity.class);
            String idNegozio = intentResult.getContents();

            final String[] negozio = new String[1];

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("StoresEncoding");
            Query store = databaseReference.orderByChild("codice").equalTo(idNegozio);
            store.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        negozio[0] = dataSnapshot.child(idNegozio).child("nome").getValue().toString();
                        intent.putExtra("negozio", negozio[0]);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ScanActivity.this, "Negozio non trovato", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        } else {
            Toast.makeText(this, "La scansione non è andata a buon fine!", Toast.LENGTH_SHORT).show();
        }
    }
}