package com.example.mystore_1_0.Fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.mystore_1_0.AutoCompleteProductAdapter;
import com.example.mystore_1_0.Orientamento;
import com.example.mystore_1_0.Prodotto.Posizione;
import com.example.mystore_1_0.Prodotto.Prodotto;
import com.example.mystore_1_0.R;
import com.example.mystore_1_0.Utente;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShowProductFragment extends Fragment {
    Prodotto prodInSospeso;
    Boolean isRestarted;
    List<Prodotto> listaProdotti;
    public Boolean isClicked = false;
    public int indicePrecedente;
    public int indiceSuccessivo = 500;
    public Boolean is2Clicked = false;
    public int lunghezza = 1;
    private final int NumeroColonne = 33;
    public Posizione posizione;
    String oldCode;

    public ShowProductFragment(){
        this.isRestarted = false;
    }

    public ShowProductFragment(Prodotto prodInSospeso, String oldCode){
        this.prodInSospeso = prodInSospeso;
        this.isRestarted = true;
        this.oldCode = oldCode;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_showproduct, container, false);

        Utente utenteLoggato = getActivity().getIntent().getParcelableExtra("utente");

        MaterialAutoCompleteTextView autoComplete = view.findViewById(R.id.autoCompleteTextView);
        GridLayout gridLayout = view.findViewById(R.id.gridShowProduct);

        /*
        //Rendo tutti i bottoni invisibili appena il fragment viene creato
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            gridLayout.getChildAt(i).setVisibility(View.INVISIBLE);
        }
        */

        TextInputLayout name_editText = view.findViewById(R.id.name_editText);
        TextInputLayout code_editText = view.findViewById(R.id.code_editText);
        TextInputLayout price_editText = view.findViewById(R.id.price_editText);
        TextInputLayout position_editText = view.findViewById(R.id.position_editText);

        MaterialButton confirmBtn = view.findViewById(R.id.confirmBtn);

        if(isRestarted){
            autoComplete.setText(prodInSospeso.getNome());
            if (!prodInSospeso.getNome().equals("null")){
                name_editText.getEditText().setText(prodInSospeso.getNome());
            }
            if (!prodInSospeso.getNome().equals("null")){
                code_editText.getEditText().setText(prodInSospeso.getCodice());
            }
            if (!prodInSospeso.getNome().equals("null")){
                price_editText.getEditText().setText(prodInSospeso.getPrezzo());
            }

            for (int i = 0; i < gridLayout.getChildCount(); i++) {

                final int finalI = i;
                gridLayout.getChildAt(i).setOnClickListener(view1 -> {
                    // your click code here
                    if (!is2Clicked) {  // SE NON E' STATO CLICCATO UN SECONDO BOTTONE
                        if (!isClicked) {  // SE NON E' STATO CLICCATO NULLA, QUINDI PRIMO CLICK
                            isClicked = true;
                            position_editText.getEditText().setText(getPosition(finalI).getIndiceRiga() + ", " + getPosition(finalI).getIndiceColonna());
                            indicePrecedente = finalI;
                            gridLayout.getChildAt(finalI).setBackgroundResource(R.drawable.button_shape);
                            posizione = getPosition(indicePrecedente);

                        } else { // E' STATO CLICCATO GIA' UN BOTTONE, QUINDI CODICE PER IL SECONDO BOTTONE
                            lunghezza = 0;
                            is2Clicked = true;
                            indiceSuccessivo = finalI;
                            if (getPosition(indicePrecedente).getIndiceRiga() == getPosition(finalI).getIndiceRiga()) {
                                // SE IL SECONDO BOTTONE E' SULLA STESSA RIGA DEL PRIMO
                                if (indicePrecedente < indiceSuccessivo) { // SE IL 2 BOTTONE E' A DESTRA DEL 1 BOTTONE
                                    for (int j = indicePrecedente; j <= finalI; j++) {
                                        gridLayout.getChildAt(j).setBackgroundResource(R.drawable.button_shape);
                                        position_editText.getEditText().setText(getPosition(indicePrecedente).getIndiceRiga() + ", " + getPosition(indicePrecedente).getIndiceColonna() + " -> " + getPosition(finalI).getIndiceRiga() + ", " + getPosition(finalI).getIndiceColonna());
                                        lunghezza++;
                                    }
                                    //posizione = getPosition(finalI);
                                } else if (indicePrecedente > indiceSuccessivo) {
                                    for (int j = indicePrecedente; j >= indiceSuccessivo; j--) {   // SE IL 2 BOTTONE E' A SINISTRA DEL 1 BOTTONE
                                        gridLayout.getChildAt(j).setBackgroundResource(R.drawable.button_shape);
                                        position_editText.getEditText().setText(getPosition(indicePrecedente).getIndiceRiga() + ", " + getPosition(indicePrecedente).getIndiceColonna() + " -> " + getPosition(finalI).getIndiceRiga() + ", " + getPosition(finalI).getIndiceColonna());
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
                                        gridLayout.getChildAt(j).setBackgroundResource(R.drawable.button_shape);
                                        position_editText.getEditText().setText(getPosition(indicePrecedente).getIndiceRiga() + ", " + getPosition(indicePrecedente).getIndiceColonna() + " -> " + getPosition(finalI).getIndiceRiga() + ", " + getPosition(finalI).getIndiceColonna());
                                        lunghezza++;
                                    }
                                    posizione.setLunghezza(lunghezza);
                                } else if (indicePrecedente > indiceSuccessivo) { // SE IL SECONDO BOTTONE E' A SINISRA DEL PRIMO
                                    for (int j = indicePrecedente; j >= indiceSuccessivo; j = j - 33) {
                                        gridLayout.getChildAt(j).setBackgroundResource(R.drawable.button_shape);
                                        position_editText.getEditText().setText(getPosition(indicePrecedente).getIndiceRiga() + ", " + getPosition(indicePrecedente).getIndiceColonna() + " -> " + getPosition(finalI).getIndiceRiga() + ", " + getPosition(finalI).getIndiceColonna());
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

        }


        CheckBox checkBox = view.findViewById(R.id.editCheck);

        checkBox.setOnClickListener(v -> {
            if (checkBox.isChecked()) {     //CheckBox selezionata
                confirmBtn.setBackgroundColor(getResources().getColor(R.color.scanStatusBarColor));
                confirmBtn.setClickable(true);

                name_editText.getEditText().setClickable(true);
                name_editText.getEditText().setLongClickable(true);
                name_editText.getEditText().setFocusable(true);

                code_editText.getEditText().setClickable(true);
                code_editText.getEditText().setLongClickable(true);
                code_editText.getEditText().setFocusable(true);

                price_editText.getEditText().setClickable(true);
                price_editText.getEditText().setLongClickable(true);
                price_editText.getEditText().setFocusable(true);

            } else {    //CheckBox non selezionata
                confirmBtn.setBackgroundColor(getResources().getColor(R.color.gray));
                confirmBtn.setClickable(false);

                name_editText.getEditText().setClickable(false);
                name_editText.getEditText().setLongClickable(false);
                name_editText.getEditText().setFocusable(false);

                code_editText.getEditText().setClickable(false);
                code_editText.getEditText().setLongClickable(false);
                code_editText.getEditText().setFocusable(false);

                price_editText.getEditText().setClickable(false);
                price_editText.getEditText().setLongClickable(false);
                price_editText.getEditText().setFocusable(false);
            }
        });
        // CONFERMA MODIFICA
        confirmBtn.setOnClickListener(v -> {
            Boolean isEmpty = false;
            Prodotto prodotto = new Prodotto();

            //MANCA IL CONTROLLO SULLA POSIZIONE E L'AGGIUNTA DI POSIZIONE A PRODOTTO

            if (isClicked) { // SE E' STATO CLICCATO ALMENO UN BOTTONE SULLA MAPPA
                posizione.setLunghezza(lunghezza);
                prodotto.setPosizione(posizione);
                prodotto.setNome(position_editText.getEditText().getText().toString().trim());
            } else {
                position_editText.getEditText().setError("Questo campo non può essere vuoto");
                position_editText.getEditText().requestFocus();
                isEmpty = true;
            }

            if (name_editText.getEditText().getText().toString().trim().isEmpty()) {
                name_editText.getEditText().setError("Questo campo non può essere vuoto");
                name_editText.getEditText().requestFocus();
                isEmpty = true;
            } else {
                String nome = name_editText.getEditText().getText().toString().trim();
                prodotto.setNome(nome);
            }
            if (code_editText.getEditText().getText().toString().trim().isEmpty()) {
                code_editText.getEditText().setError("Questo campo non può essere vuoto");
                code_editText.getEditText().requestFocus();
                isEmpty = true;
            } else {
                String codice = code_editText.getEditText().getText().toString().trim();
                prodotto.setCodice(codice);
            }
            if (price_editText.getEditText().getText().toString().trim().isEmpty()) {
                price_editText.getEditText().setError("Questo campo non può essere vuoto");
                price_editText.getEditText().requestFocus();
                isEmpty = true;
            } else {
                String prezzo = price_editText.getEditText().getText().toString().trim();
                prodotto.setPrezzo(prezzo);
            }

            if(!isEmpty){
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(utenteLoggato.getNegozio());
                Query checkId = reference.child("Products").orderByChild("codice").equalTo(prodotto.getCodice());

                checkId.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (oldCode.equals(prodotto.getCodice())){
                            reference.child("Products").child(prodotto.getCodice()).setValue(prodotto);
                            Toast.makeText(getActivity(), "Registrazione effettuata", Toast.LENGTH_SHORT).show();
                            name_editText.getEditText().getText().clear();
                            code_editText.getEditText().getText().clear();
                            position_editText.getEditText().getText().clear();
                            price_editText.getEditText().getText().clear();
                        } else if (dataSnapshot.exists()){
                            code_editText.getEditText().setError("È stato inserito un id già esistente");
                            code_editText.getEditText().requestFocus();
                        }
                        else{
                            reference.child("Products").child(prodotto.getCodice()).setValue(prodotto);
                            reference.child("Products").child(oldCode).removeValue();
                            Toast.makeText(getActivity(), "Registrazione effettuata", Toast.LENGTH_SHORT).show();
                            name_editText.getEditText().getText().clear();
                            code_editText.getEditText().getText().clear();
                            position_editText.getEditText().getText().clear();
                            price_editText.getEditText().getText().clear();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            });

            String negozio = utenteLoggato.getNegozio();

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
                        listaProdotti = new ArrayList<>();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            Prodotto prodotto = ds.getValue(Prodotto.class);
                            listaProdotti.add(prodotto);
                        }
                        AutoCompleteProductAdapter adapter = new AutoCompleteProductAdapter(getContext(), listaProdotti);
                        autoComplete.setAdapter(adapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }

            });


            autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //CLOSE KEYBOARD
                    View vista = getActivity().getCurrentFocus();
                    if (vista != null) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(vista.getWindowToken(), 0);
                    }

                    //prima di visualizzare il nuovo prodotto, rendo invisibili tutti i bottoni del grid layout
                    for (int i = 0; i < gridLayout.getChildCount(); i++) {
                        gridLayout.getChildAt(i).setVisibility(View.INVISIBLE);
                    }

                    Object item = parent.getItemAtPosition(position);
                    if (item instanceof Prodotto) {
                        Prodotto prodotto = (Prodotto) item;
                        oldCode = prodotto.getCodice();
                        name_editText.getEditText().setText(prodotto.getNome());
                        code_editText.getEditText().setText(prodotto.getCodice());
                        price_editText.getEditText().setText(prodotto.getPrezzo());

                        Posizione posizione = prodotto.getPosizione();

                        int indice = prodotto.getIndex(prodotto.getPosizione().getIndiceRiga(), prodotto.getPosizione().getIndiceColonna());
                        if (prodotto.getPosizione().getOrientamento().equals(Orientamento.orizzontale)) {
                            //ORENTAMENTO ORIZZONTALE
                            if (posizione.getLunghezza() > 0) { // LUNGHEZZA MAGGIORE DI ZERO QUINDI A DESTRA
                                for (int i = indice; i < indice + prodotto.getPosizione().getLunghezza(); i++) {
                                    gridLayout.getChildAt(i).setBackgroundResource(R.drawable.button_shape);
                                    gridLayout.getChildAt(i).setVisibility(View.VISIBLE);
                                }
                                if (prodotto.getPosizione().getLunghezza() == 1) {
                                    position_editText.getEditText().setText(posizione.getIndiceRiga() + ", " + posizione.getIndiceColonna());
                                } else {
                                    position_editText.getEditText().setText(posizione.getIndiceRiga() + ", " + posizione.getIndiceColonna() + " -> " + posizione.getIndiceRiga() + ", " + (posizione.getIndiceColonna() + posizione.getLunghezza() - 1));
                                }
                            } else { // LUNGHEZZA MINORE DI ZERO QUINDI A SINISTRA
                                for (int i = indice; i > indice + prodotto.getPosizione().getLunghezza(); i--) {
                                    gridLayout.getChildAt(i).setBackgroundResource(R.drawable.button_shape);
                                    gridLayout.getChildAt(i).setVisibility(View.VISIBLE);
                                }
                                if (prodotto.getPosizione().getLunghezza() == 1) {
                                    position_editText.getEditText().setText(posizione.getIndiceRiga() + ", " + posizione.getIndiceColonna());
                                } else {
                                    position_editText.getEditText().setText(posizione.getIndiceRiga() + ", " + posizione.getIndiceColonna() + " -> " + posizione.getIndiceRiga() + ", " + (posizione.getIndiceColonna() - posizione.getLunghezza() + 1));
                                }
                            }
                        } else { // ORIENTAMENTO VERTICALE
                            if (posizione.getLunghezza() > 0) { // LUNGHEZZA MAGGIORE DI ZERO QUINDI VERSO IL BASSO
                                for (int i = indice; i < indice + (prodotto.getPosizione().getLunghezza() * 33); i = i + 33) {
                                    gridLayout.getChildAt(i).setBackgroundResource(R.drawable.button_shape);
                                    gridLayout.getChildAt(i).setVisibility(View.VISIBLE);
                                }
                                if (prodotto.getPosizione().getLunghezza() == 1) {
                                    position_editText.getEditText().setText(posizione.getIndiceRiga() + ", " + posizione.getIndiceColonna());
                                } else {
                                    position_editText.getEditText().setText(posizione.getIndiceRiga() + ", " + posizione.getIndiceColonna() + " -> " + (posizione.getIndiceRiga() + posizione.getLunghezza() - 1) + ", " + posizione.getIndiceColonna());
                                }
                            } else { // LUNGHEZZA MINORE DI ZERO QUINDI VERSO L'ALTO
                                for (int i = indice; i > indice + (prodotto.getPosizione().getLunghezza() * 33); i = i - 33) {
                                    gridLayout.getChildAt(i).setBackgroundResource(R.drawable.button_shape);
                                    gridLayout.getChildAt(i).setVisibility(View.VISIBLE);
                                }
                                if (prodotto.getPosizione().getLunghezza() == 1) {
                                    position_editText.getEditText().setText(posizione.getIndiceRiga() + ", " + posizione.getIndiceColonna());
                                } else {
                                    position_editText.getEditText().setText(posizione.getIndiceRiga() + ", " + posizione.getIndiceColonna() + " -> " + (posizione.getIndiceRiga() - posizione.getLunghezza() + 1) + ", " + posizione.getIndiceColonna());
                                }
                            }
                        }


                    }
                }


            });

        // CLICK SU CANCELLA POSIZIONE
        position_editText.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //RelativeLayout relativ = view.findViewById(R.id.relative_progress_add_prod);
                //relativ.setVisibility(View.VISIBLE);

                Prodotto prodInSospeso = new Prodotto();
                if (!name_editText.getEditText().getText().toString().trim().isEmpty()) {
                    prodInSospeso.setNome(name_editText.getEditText().getText().toString().trim());
                } else{
                    prodInSospeso.setNome("null");
                }
                if (!code_editText.getEditText().getText().toString().trim().isEmpty()) {
                    prodInSospeso.setCodice(code_editText.getEditText().getText().toString().trim());
                } else{
                    prodInSospeso.setCodice("null");
                }
                if (!price_editText.getEditText().getText().toString().trim().isEmpty()) {
                    prodInSospeso.setPrezzo(price_editText.getEditText().getText().toString().trim());
                } else{
                    prodInSospeso.setPrezzo("null");
                }
                AppCompatActivity activity = (AppCompatActivity) getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShowProductFragment(prodInSospeso, oldCode)).commit();

            }
        });

            return view;
        }
    }
