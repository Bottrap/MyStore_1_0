package com.example.mystore_1_0.Fragments;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.mystore_1_0.Fragments.ShowUsers.ShowUsersFragment;
import com.example.mystore_1_0.Orientamento;
import com.example.mystore_1_0.Prodotto.Posizione;
import com.example.mystore_1_0.Prodotto.Prodotto;
import com.example.mystore_1_0.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddProductFragment extends Fragment {

    private final int NumeroColonne = 33;

    private int getIndex(int x, int y) {
        int indice = ((NumeroColonne) * x) + y;
        return indice;
    }

    private Posizione getPosition(int index) {
        int x = 0;
        while (index >= NumeroColonne) {
            index = index - NumeroColonne;
            x = x + 1;
        }
        Posizione posizione = new Posizione(x, index);
        return posizione;
    }

    public Boolean isClicked = false;
    public int indicePrecedente;
    public int indiceSuccessivo = 500;
    public Boolean is2Clicked = false;
    public Posizione posizione;
    public int lunghezza = 1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addproduct, container, false);

        TextInputLayout text_posizione = view.findViewById(R.id.text_position);
        TextInputLayout text_nome = view.findViewById(R.id.text_NomeProdotto);
        TextInputLayout text_codice = view.findViewById(R.id.text_codice_prodotto);
        TextInputLayout text_prezzo = view.findViewById(R.id.text_price);

        GridLayout grid = (GridLayout) view.findViewById(R.id.gridProduct);
        int childCount = grid.getChildCount();


        for (int i = 0; i < childCount; i++) {

            final int finalI = i;
            grid.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // your click code here
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
                                    for (int j = indicePrecedente; j >= indiceSuccessivo; j--) { // SE IL 2 BOTTONE E' A SINISTRA DEL 1 BOTTONE
                                        grid.getChildAt(j).setBackgroundResource(R.drawable.button_shape);
                                        text_posizione.getEditText().setText(getPosition(indicePrecedente).getIndiceRiga() + ", " + getPosition(indicePrecedente).getIndiceColonna() + " -> " + getPosition(finalI).getIndiceRiga() + ", " + getPosition(finalI).getIndiceColonna());
                                        lunghezza--;
                                    }
                                }
                                //posizione.setLunghezza(lunghezza);
                            }
                            if (getPosition(indicePrecedente).getIndiceColonna() == getPosition(indiceSuccessivo).getIndiceColonna()) {
                                // SE IL SECONDO BOTTONE E' SULLA STESSA COLONA DEL PRIMO
                                posizione.setOrientamento(Orientamento.verticale);

                                if (indicePrecedente < indiceSuccessivo) { // SE IL 2 BOTTONE E' A DESTRA DEL PRIMO

                                    for (int j = indicePrecedente; j <= indiceSuccessivo; j = j + 33) {
                                        grid.getChildAt(j).setBackgroundResource(R.drawable.button_shape);
                                        text_posizione.getEditText().setText(getPosition(indicePrecedente).getIndiceRiga() + ", " + getPosition(indicePrecedente).getIndiceColonna() + " -> " + getPosition(finalI).getIndiceRiga() + ", " + getPosition(finalI).getIndiceColonna());
                                        lunghezza++;
                                    }
                                    posizione.setLunghezza(lunghezza);
                                } else  if (indicePrecedente > indiceSuccessivo) { // SE IL SECONDO BOTTONE E' A SINISRA DEL PRIMO
                                    for (int j = indicePrecedente; j >= indiceSuccessivo; j = j - 33) {
                                        grid.getChildAt(j).setBackgroundResource(R.drawable.button_shape);
                                        text_posizione.getEditText().setText(getPosition(indicePrecedente).getIndiceRiga() + ", " + getPosition(indicePrecedente).getIndiceColonna() + " -> " + getPosition(finalI).getIndiceRiga() + ", " + getPosition(finalI).getIndiceColonna());
                                        lunghezza--;
                                    }

                                } else {
                                    lunghezza = 1;
                                }
                            }

                        }

                    }
                }
            });
        }


        text_posizione.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatActivity activity = (AppCompatActivity) getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddProductFragment()).commit();

            }
        });

        Button btn_add_prod = view.findViewById(R.id.btn_add_prod);
        btn_add_prod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isEmpty = false;
                Prodotto prodotto = new Prodotto();
                if (isClicked) { // SE E' STATO CLICCATO ALMENO UN BOTTONE SULLA MAPPA
                        posizione.setLunghezza(lunghezza);
                        prodotto.setPosizione(posizione);
                        Log.d("orientamento", String.valueOf(posizione.getOrientamento()));
                        Log.d("lunghezza", String.valueOf(posizione.getLunghezza()));
                        Log.d("indice Riga", String.valueOf(posizione.getIndiceRiga() +", indice Colonna: "+ String.valueOf(posizione.getIndiceColonna())));
                        prodotto.setNome(text_posizione.getEditText().getText().toString().trim());
                } else{
                    text_posizione.getEditText().setError("Questo campo non può essere vuoto");
                    text_posizione.getEditText().requestFocus();
                    isEmpty = true;
                }

                if(text_nome.getEditText().getText().toString().trim().isEmpty()) {
                    text_nome.getEditText().setError("Questo campo non può essere vuoto");
                    text_nome.getEditText().requestFocus();
                    isEmpty = true;
                } else {
                    String nome = text_nome.getEditText().getText().toString().trim();
                    prodotto.setNome(nome);
                }
                if(text_codice.getEditText().getText().toString().trim().isEmpty()) {
                    text_codice.getEditText().setError("Questo campo non può essere vuoto");
                    text_codice.getEditText().requestFocus();
                    isEmpty = true;
                } else {
                    String codice = text_codice.getEditText().getText().toString().trim();
                    prodotto.setCodice(codice);
                }
                if(text_prezzo.getEditText().getText().toString().trim().isEmpty()) {
                    text_prezzo.getEditText().setError("Questo campo non può essere vuoto");
                    text_prezzo.getEditText().requestFocus();
                    isEmpty = true;
                } else {
                    String prezzo = text_prezzo.getEditText().getText().toString().trim();
                    prodotto.setPrezzo(prezzo);
                }


                if (!isEmpty){
                    // codice database :)
                }

            }
        });


        return view;
    }
}
