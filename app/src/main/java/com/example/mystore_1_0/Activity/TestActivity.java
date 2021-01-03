package com.example.mystore_1_0.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.mystore_1_0.Fragments.AddProductFragment;
import com.example.mystore_1_0.Orientamento;
import com.example.mystore_1_0.Prodotto.Posizione;
import com.example.mystore_1_0.Prodotto.Prodotto;
import com.example.mystore_1_0.R;
import com.example.mystore_1_0.Utente;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

import static com.example.mystore_1_0.Prodotto.Posizione.getPosition;

public class TestActivity extends AppCompatActivity {

    public Boolean isClicked = false;
    public int indicePrecedente;
    public int indiceSuccessivo = 500;
    public Boolean is2Clicked = false;
    public Posizione posizione;
    public int lunghezza = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Intent intent = getIntent();
        String negozio = "store1";
        GridLayout grid = findViewById(R.id.gridTest);

        StorageReference mapReference = FirebaseStorage.getInstance().getReference("Mappe_Negozi/" + negozio + ".png");
        try {
            File localFile = File.createTempFile(negozio, "png");
            mapReference.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
                grid.setBackground(bitmapDrawable);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        TextInputLayout text_posizione = findViewById(R.id.test);
        MaterialButton button = findViewById(R.id.testBtn);

        int childCount = grid.getChildCount();

        MaterialButton btn = new MaterialButton(this);
        Drawable background = btn.getBackground();

        for (int i = 0; i < childCount; i++) {

            final int finalI = i;
            int finalI1 = i;
            grid.getChildAt(i).setOnClickListener(view1 -> {
                if (!is2Clicked) {  // SE NON E' STATO CLICCATO UN SECONDO BOTTONE
                    if (!isClicked) {  // SE NON E' STATO CLICCATO NULLA, QUINDI PRIMO CLICK
                        isClicked = true;
                        text_posizione.getEditText().setText(getPosition(finalI).getIndiceRiga() + ", " + getPosition(finalI).getIndiceColonna());
                        indicePrecedente = finalI;
                        grid.getChildAt(finalI).setBackgroundResource(R.drawable.button_shape);
                        posizione = getPosition(indicePrecedente);

                    } else { // E' STATO CLICCATO GIA' UN BOTTONE, QUINDI CODICE PER IL SECONDO BOTTONE
                        lunghezza = 0;
                        is2Clicked = true;
                        indiceSuccessivo = finalI;
                        if (getPosition(indicePrecedente).getIndiceRiga() == getPosition(finalI).getIndiceRiga()) {
                            // SE IL SECONDO BOTTONE E' SULLA STESSA RIGA DEL PRIMO
                            if (indicePrecedente < indiceSuccessivo) { // SE IL 2 BOTTONE E' A DESTRA DEL 1 BOTTONE
                                for (int j = indicePrecedente; j <= finalI; j++) {
                                    grid.getChildAt(j).setBackgroundResource(R.drawable.button_shape);
                                    text_posizione.getEditText().setText(getPosition(indicePrecedente).getIndiceRiga() + ", " + getPosition(indicePrecedente).getIndiceColonna() + " -> " + getPosition(finalI).getIndiceRiga() + ", " + getPosition(finalI).getIndiceColonna());
                                    lunghezza++;
                                }
                                //posizione = getPosition(finalI);
                            } else if (indicePrecedente > indiceSuccessivo) {
                                for (int j = indicePrecedente; j >= indiceSuccessivo; j--) {   // SE IL 2 BOTTONE E' A SINISTRA DEL 1 BOTTONE
                                    grid.getChildAt(j).setBackgroundResource(R.drawable.button_shape);
                                    text_posizione.getEditText().setText(getPosition(indicePrecedente).getIndiceRiga() + ", " + getPosition(indicePrecedente).getIndiceColonna() + " -> " + getPosition(finalI).getIndiceRiga() + ", " + getPosition(finalI).getIndiceColonna());
                                    lunghezza--;
                                }
                            }
                            //posizione.setLunghezza(lunghezza);
                        }
                        if (getPosition(indicePrecedente).getIndiceColonna() == getPosition(indiceSuccessivo).getIndiceColonna()) {
                            // SE IL SECONDO BOTTONE E' SULLA STESSA COLONNA DEL PRIMO
                            posizione.setOrientamento(Orientamento.verticale);

                            if (indicePrecedente < indiceSuccessivo) { // SE IL 2 BOTTONE E' A DESTRA DEL PRIMO

                                for (int j = indicePrecedente; j <= indiceSuccessivo; j = j + 33) {
                                    grid.getChildAt(j).setBackgroundResource(R.drawable.button_shape);
                                    text_posizione.getEditText().setText(getPosition(indicePrecedente).getIndiceRiga() + ", " + getPosition(indicePrecedente).getIndiceColonna() + " -> " + getPosition(finalI).getIndiceRiga() + ", " + getPosition(finalI).getIndiceColonna());
                                    lunghezza++;
                                }
                                posizione.setLunghezza(lunghezza);
                            } else if (indicePrecedente > indiceSuccessivo) { // SE IL SECONDO BOTTONE E' A SINISTRA DEL PRIMO
                                for (int j = indicePrecedente; j >= indiceSuccessivo; j = j - 33) {
                                    grid.getChildAt(j).setBackgroundResource(R.drawable.button_shape);
                                    text_posizione.getEditText().setText(getPosition(indicePrecedente).getIndiceRiga() + ", " + getPosition(indicePrecedente).getIndiceColonna() + " -> " + getPosition(finalI).getIndiceRiga() + ", " + getPosition(finalI).getIndiceColonna());
                                    lunghezza--;
                                }

                            } else {
                                lunghezza = 1;
                                is2Clicked = false;
                            }
                        }

                    }

                }
            });
        }

        button.setOnClickListener(v -> {
            for (int i = 0; i < grid.getChildCount(); i++) {
                grid.getChildAt(i).setBackground(background);
                is2Clicked = false;
                isClicked = false;
                text_posizione.getEditText().getText().clear();
            }
        });

    }
}