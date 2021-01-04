package com.example.mystore_1_0.Activity;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;

import android.widget.GridLayout;
import android.widget.HorizontalScrollView;

import com.bumptech.glide.Glide;
import com.example.mystore_1_0.AutoCompleteProductAdapter;
import com.example.mystore_1_0.Fragments.ShowUsers.ShowUsersAdapter;
import com.example.mystore_1_0.Orientamento;
import com.example.mystore_1_0.Prodotto.Prodotto;
import com.example.mystore_1_0.R;
import com.example.mystore_1_0.Utente;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mappa);

        MaterialAutoCompleteTextView autoComplete = findViewById(R.id.autoCompleteTextView);
        GridLayout gridLayout = findViewById(R.id.gridlayout);

        Intent intent = getIntent();
        String negozio = intent.getStringExtra("negozio");

        StorageReference mapReference = FirebaseStorage.getInstance().getReference("Mappe_Negozi/" + negozio + ".png");
        try {
            File localFile = File.createTempFile(negozio, "png");
            mapReference.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
                gridLayout.setBackground(bitmapDrawable);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(negozio).child("Products");
        Query retrieveAll = reference.orderByKey();
        retrieveAll.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<Prodotto> listaProdotti = new ArrayList<>();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Prodotto prodotto = ds.getValue(Prodotto.class);
                        listaProdotti.add(prodotto);
                    }
                    AutoCompleteProductAdapter adapter = new AutoCompleteProductAdapter(getApplicationContext(), listaProdotti);
                    autoComplete.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });

        GridLayout grid = findViewById(R.id.gridlayout);
        autoComplete.setOnItemClickListener((parent, view, position, id) -> {
            Object item = parent.getItemAtPosition(position);
            if (item instanceof Prodotto) {
                Prodotto prodotto = (Prodotto) item;

                //prima di visualizzare il nuovo prodotto, rendo invisibili tutti i bottoni del grid layout
                for (int i = 0; i < gridLayout.getChildCount(); i++) {
                    gridLayout.getChildAt(i).setVisibility(View.INVISIBLE);
                }

                int indice = prodotto.getIndex(prodotto.getPosizione().getIndiceRiga(), prodotto.getPosizione().getIndiceColonna());
                if (prodotto.getPosizione().getOrientamento().equals(Orientamento.orizzontale)) {
                    //ORENTAMENTO ORIZZONTALE
                    if (prodotto.getPosizione().getLunghezza() > 0) { // LUNGHEZZA MAGGIORE DI ZERO QUINDI A DESTRA
                        for (int i = indice; i < indice + prodotto.getPosizione().getLunghezza(); i++) {
                            grid.getChildAt(i).setBackgroundResource(R.drawable.button_shape);
                            grid.getChildAt(i).setVisibility(View.VISIBLE);
                        }
                    } else { // LUNGHEZZA MINORE DI ZERO QUINDI A SINISTRA
                        for (int i = indice; i > indice + prodotto.getPosizione().getLunghezza(); i--) {
                            grid.getChildAt(i).setBackgroundResource(R.drawable.button_shape);
                            grid.getChildAt(i).setVisibility(View.VISIBLE);

                        }
                    }
                } else { // ORIENTAMENTO VERTICALE
                    if (prodotto.getPosizione().getLunghezza() > 0) { // LUNGHEZZA MAGGIORE DI ZERO QUINDI VERSO IL BASSO
                        for (int i = indice; i < indice + (prodotto.getPosizione().getLunghezza() * 33); i = i + 33) {
                            grid.getChildAt(i).setBackgroundResource(R.drawable.button_shape);
                            grid.getChildAt(i).setVisibility(View.VISIBLE);

                        }
                    } else { // LUNGHEZZA MINORE DI ZERO QUINDI VERSO L'ALTO
                        for (int i = indice; i > indice + (prodotto.getPosizione().getLunghezza() * 33); i = i - 33) {
                            grid.getChildAt(i).setBackgroundResource(R.drawable.button_shape);
                            grid.getChildAt(i).setVisibility(View.VISIBLE);

                        }
                    }
                }
                closeKeyboard();
            }
        });


    }

    private void closeKeyboard() {
        View vista = this.getCurrentFocus();
        if (vista != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(vista.getWindowToken(), 0);
        }

    }
}