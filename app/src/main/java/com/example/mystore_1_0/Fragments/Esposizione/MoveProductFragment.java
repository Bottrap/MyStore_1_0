    package com.example.mystore_1_0.Fragments.Esposizione;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.mystore_1_0.AutoCompleteProductAdapter;
import com.example.mystore_1_0.Fragments.DashboardFragment;
import com.example.mystore_1_0.Fragments.Magazzino.ManageStorageProductFragment;
import com.example.mystore_1_0.IOnBackPressed;
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

import static com.example.mystore_1_0.Prodotto.Posizione.getPosition;

public class MoveProductFragment extends Fragment implements IOnBackPressed {

    Prodotto prodInMagazzino;
    public Boolean isClicked = false;
    public int indicePrecedente;
    public int indiceSuccessivo = 500;
    public Boolean is2Clicked = false;
    public Posizione posizione;
    public int lunghezza = 1;
    long oldQuantita;
    List<Prodotto> listaProdottiMagazzino = new ArrayList<>();
    List<Prodotto> listaProdottiEsposizione = new ArrayList<>();
    List<Prodotto> listaProdotti = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_moveproduct, null);

        isClicked = false;
        indiceSuccessivo=500;
        is2Clicked=false;
        lunghezza=1;



        // UTENTE LOGGATO PER VEDERE IN CHE NEGOZIO E'
        Utente utenteLoggato = getActivity().getIntent().getParcelableExtra("utente");
        String negozio = utenteLoggato.getNegozio();

        // DICHIARO BOTTONE DEL QUALE MI SERVE IL BACKGROUND, PER RESETTARE IL BACKGROUND DEI BOTTONI PRESENTI NEL GRID LAYOUT
        MaterialButton btn = new MaterialButton(getActivity());
        Drawable background = btn.getBackground();

        // DICHIARO ELEMENTI DELL'INTERFACCIA
        GridLayout grid = view.findViewById(R.id.gridProduct);
        MaterialAutoCompleteTextView autoComplete = view.findViewById(R.id.autoCompleteTextView);
        TextInputLayout text_posizione = view.findViewById(R.id.position_editText);
        TextInputLayout text_quantita = view.findViewById(R.id.quantity_editText);
        MaterialButton addBtn = view.findViewById(R.id.addBtn);



        // CARICO LA MAPPA DEL NEGOZIO RELATIVO ALL'UTENTE LOGGATO
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

        // CARICO I PRODOTTI NELLA AUTOCOMPLETE (CREO UNA LISTA DIFFERENZA DEI PRODOTTI PRESENTI NEL MAGAZZINO CHE NON SONO ANCORA STATI ESPOSTI
        DatabaseReference referenceBug = FirebaseDatabase.getInstance().getReference(negozio).child("Products");
        Query query = referenceBug.orderByKey();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ArrayList<DataSnapshot> dataSnapshots = new ArrayList<>();

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        dataSnapshots.add(ds);
                    }
                    for (int i = 0; i < dataSnapshots.size(); i++) {
                        if (i == 0) {
                            DataSnapshot esposizione = dataSnapshots.get(i);
                            for (DataSnapshot ds : esposizione.getChildren()) {
                                Prodotto prodotto = ds.getValue(Prodotto.class);
                                listaProdottiEsposizione.add(prodotto);
                            }
                        } else if (i == 1) {
                            DataSnapshot magazzino = dataSnapshots.get(i);
                            for (DataSnapshot ds : magazzino.getChildren()) {
                                Prodotto prodotto = ds.getValue(Prodotto.class);
                                listaProdottiMagazzino.add(prodotto);
                            }
                        }
                    }
                    for (int i = 0; i < listaProdottiMagazzino.size(); i++) {
                        boolean flag = false;
                        for (int j = 0; j < listaProdottiEsposizione.size(); j++) {
                            if (listaProdottiMagazzino.get(i).getCodice().equals(listaProdottiEsposizione.get(j).getCodice())) {
                                flag = true;
                            }
                        }
                        if (!flag) {
                            listaProdotti.add(listaProdottiMagazzino.get(i));
                        }
                    }
                    AutoCompleteProductAdapter adapter = new AutoCompleteProductAdapter(view.getContext(), listaProdotti);
                    autoComplete.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*
        // CARICO I PRODOTTI PRESENTI NEL MAGAZZINO
            DatabaseReference referenceMagazzino = FirebaseDatabase.getInstance().getReference(negozio).child("Products").child("Magazzino");
            Query prodottiMagazzino = referenceMagazzino.orderByKey();
            prodottiMagazzino.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            Prodotto prodotto = ds.getValue(Prodotto.class);
                            listaProdottiMagazzino.add(prodotto);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

        // CARICO I PRODOTTI PRESENTI NELLA ZONA ESPOSIZIONE
            DatabaseReference referenceEsposizione = FirebaseDatabase.getInstance().getReference(negozio).child("Products").child("Esposizione");
            Query prodottiEsposizione = referenceEsposizione.orderByKey();
            prodottiEsposizione.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            Prodotto prodotto = ds.getValue(Prodotto.class);
                            listaProdottiEsposizione.add(prodotto);
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

        // GENERO LA LISTA "DIFFERENZA" DA MOSTRARE NELL'AUTOCOMPLETE
        for (int i = 0; i < listaProdottiMagazzino.size(); i++) {
            boolean flag = false;
            for (int j = 0; j < listaProdottiEsposizione.size(); j++) {
                if (listaProdottiMagazzino.get(i).getCodice().equals(listaProdottiEsposizione.get(j).getCodice())) {
                    flag = true;
                }
            }
            if (!flag) {
                listaProdotti.add(listaProdottiMagazzino.get(i));
            }
        }
        AutoCompleteProductAdapter adapter = new AutoCompleteProductAdapter(view.getContext(), listaProdotti);
        autoComplete.setAdapter(adapter);

         */

        // INSERIRE LA POSIZIONE DEL PRODOTTO NELLA ZONA ESPOSIZIONE
        int childCount = grid.getChildCount();

        for (int i = 0; i < childCount; i++) {

            final int finalI = i;
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
                            } else if (indicePrecedente > indiceSuccessivo) {
                                for (int j = indicePrecedente; j >= indiceSuccessivo; j--) {   // SE IL 2 BOTTONE E' A SINISTRA DEL 1 BOTTONE
                                    grid.getChildAt(j).setBackgroundResource(R.drawable.button_shape);
                                    text_posizione.getEditText().setText(getPosition(indicePrecedente).getIndiceRiga() + ", " + getPosition(indicePrecedente).getIndiceColonna() + " -> " + getPosition(finalI).getIndiceRiga() + ", " + getPosition(finalI).getIndiceColonna());
                                    lunghezza--;
                                }
                            }
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
                            } else if (indicePrecedente > indiceSuccessivo) { // SE IL SECONDO BOTTONE E' A SINISRA DEL PRIMO
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
                        if (getPosition(indicePrecedente).getIndiceRiga() != getPosition(finalI).getIndiceRiga() & getPosition(indicePrecedente).getIndiceColonna() != getPosition(indiceSuccessivo).getIndiceColonna()) {
                            lunghezza = 1;
                            is2Clicked = false;
                        }

                    }

                }
            });
        }

        // CLICK SUGLI ELEMENTI DELLA LISTA
        autoComplete.setOnItemClickListener((parent, view12, position, id) -> {
            //CLOSE KEYBOARD
            View vista = getActivity().getCurrentFocus();
            if (vista != null) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(vista.getWindowToken(), 0);
            }

            Object item = parent.getItemAtPosition(position);
            if (item instanceof Prodotto) {
                Prodotto prodotto = (Prodotto) item;
                prodInMagazzino = prodotto;
                oldQuantita = prodInMagazzino.getQuantita();
                text_quantita.getEditText().setHint("Quantità (MAX: " + oldQuantita + " )");
            }
        });

        // CLICK SU CANCELLA POSIZIONE
        text_posizione.setEndIconOnClickListener(v -> {
            for (int i = 0; i < grid.getChildCount(); i++) {
                grid.getChildAt(i).setBackground(background);
                is2Clicked = false;
                isClicked = false;
                text_posizione.getEditText().getText().clear();
            }
        });

        // CLICK SU AGGIUNGI PRODOTTO
        addBtn.setOnClickListener(v -> {
            boolean isEmpty = false;

            if (isClicked) { // SE E' STATO CLICCATO ALMENO UN BOTTONE SULLA MAPPA
                posizione.setLunghezza(lunghezza);
                prodInMagazzino.setPosizione(posizione);
            } else {
                text_posizione.getEditText().setError("Questo campo non può essere vuoto");
                text_posizione.getEditText().requestFocus();
                isEmpty = true;
            }
            if (text_quantita.getEditText().getText().toString().trim().isEmpty()) {
                text_quantita.getEditText().setError("Questo campo non può essere vuoto");
                text_quantita.getEditText().requestFocus();
                isEmpty = true;
            } else {
                if (Long.parseLong(text_quantita.getEditText().getText().toString().trim()) > prodInMagazzino.getQuantita()) {
                    text_quantita.getEditText().setError("Sono presenti in magazzino solo " + prodInMagazzino.getQuantita() + " pezzi");
                    text_quantita.getEditText().requestFocus();
                    isEmpty = true;
                }
                int quantita = Integer.parseInt(text_quantita.getEditText().getText().toString().trim());
                prodInMagazzino.setQuantita(quantita);
            }
            if (!isEmpty) {

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(negozio).child("Products");
                Query query1 = reference;
                query1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        reference.child("Esposizione").child(prodInMagazzino.getCodice()).setValue(prodInMagazzino);
                        reference.child("Magazzino").child(prodInMagazzino.getCodice()).child("quantita").setValue(oldQuantita - prodInMagazzino.getQuantita());

                        //AppCompatActivity activity = (AppCompatActivity) getActivity();
                        //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MoveProductFragment()).commit();

                        //getFragmentManager().beginTransaction().replace(R.id.fragment_container, new MoveProductFragment()).commit();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                Toast.makeText(getActivity(), "Prodotto spostato correttamente", Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MoveProductFragment()).commit();
            }

        });


        return view;
    }

    @Override
    public boolean onBackPressed() {
        AppCompatActivity activity = (AppCompatActivity) getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DashboardFragment()).commit();
        return true;
    }
}
