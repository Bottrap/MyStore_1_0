package com.example.mystore_1_0.Fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.mystore_1_0.Orientamento;
import com.example.mystore_1_0.Prodotto.Posizione;
import com.example.mystore_1_0.Prodotto.Prodotto;
import com.example.mystore_1_0.R;
import com.example.mystore_1_0.Utente;
import com.google.android.gms.tasks.Task;
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

import static android.app.Activity.RESULT_OK;

public class AddProductFragment extends Fragment {

    private boolean isRestarted = false;
    private Prodotto prodInSospeso;

    private final int NumeroColonne = 33;
    private Uri imageUri;
    private static final int PICK_IMAGE = 1;

    private int getIndex(int x, int y) {
        int indice = ((NumeroColonne) * x) + y;
        return indice;
    }

    public AddProductFragment() {
        this.isRestarted = false;
    }

    public AddProductFragment(Prodotto prodInSospeso) {
        this.isRestarted = true;
        this.prodInSospeso = prodInSospeso;
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            imageUri = data.getData();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addproduct, container, false);

        Utente utenteLoggato = getActivity().getIntent().getParcelableExtra("utente");

        TextInputLayout text_posizione = view.findViewById(R.id.text_position);
        TextInputLayout text_nome = view.findViewById(R.id.text_NomeProdotto);
        TextInputLayout text_codice = view.findViewById(R.id.text_codice_prodotto);
        TextInputLayout text_prezzo = view.findViewById(R.id.text_price);

        GridLayout grid = (GridLayout) view.findViewById(R.id.gridProduct);
        int childCount = grid.getChildCount();

        if (isRestarted) {
            if (!prodInSospeso.getNome().equals("null")) {
                text_nome.getEditText().setText(prodInSospeso.getNome());
            }
            if (!prodInSospeso.getNome().equals("null")) {
                text_codice.getEditText().setText(prodInSospeso.getCodice());
            }
            if (!prodInSospeso.getNome().equals("null")) {
                text_prezzo.getEditText().setText(prodInSospeso.getPrezzo());
            }
        }

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

                    }

                }
            });
        }


        // CLICK SU CANCELLA POSIZIONE
        text_posizione.setEndIconOnClickListener(v -> {
            RelativeLayout relativ = view.findViewById(R.id.relative_progress_add_prod);
            relativ.setVisibility(View.VISIBLE);
            Prodotto prodInSospeso = new Prodotto();
            if (!text_nome.getEditText().getText().toString().trim().isEmpty()) {
                prodInSospeso.setNome(text_nome.getEditText().getText().toString().trim());
            } else {
                prodInSospeso.setNome("null");
            }
            if (!text_codice.getEditText().getText().toString().trim().isEmpty()) {
                prodInSospeso.setCodice(text_codice.getEditText().getText().toString().trim());
            } else {
                prodInSospeso.setCodice("null");
            }
            if (!text_prezzo.getEditText().getText().toString().trim().isEmpty()) {
                prodInSospeso.setPrezzo(text_prezzo.getEditText().getText().toString().trim());
            } else {
                prodInSospeso.setPrezzo("null");
            }
            AppCompatActivity activity = (AppCompatActivity) getContext();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddProductFragment(prodInSospeso)).commit();

        });

        // CLICK SU AGGIUNGI IMMAGINE
        Button btn_imagePick = view.findViewById(R.id.pickImageBtn);
        btn_imagePick.setOnClickListener(v -> {
            //prendo l'immagine dalla galleria come uri
            Intent galleria = new Intent();
            galleria.setType("image/*");
            galleria.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(galleria, "Seleziona un'immagine"), PICK_IMAGE);
        });

        // CLICK SU AGGIUNGI PRODOTTO
        Button btn_add_prod = view.findViewById(R.id.btn_add_prod);
        btn_add_prod.setOnClickListener(v -> {
            boolean isEmpty = false;
            Prodotto prodotto = new Prodotto();

            if (isClicked) { // SE E' STATO CLICCATO ALMENO UN BOTTONE SULLA MAPPA
                posizione.setLunghezza(lunghezza);
                prodotto.setPosizione(posizione);
            } else {
                text_posizione.getEditText().setError("Questo campo non può essere vuoto");
                text_posizione.getEditText().requestFocus();
                isEmpty = true;
            }

            if (text_nome.getEditText().getText().toString().trim().isEmpty()) {
                text_nome.getEditText().setError("Questo campo non può essere vuoto");
                text_nome.getEditText().requestFocus();
                isEmpty = true;
            } else {
                String nome = text_nome.getEditText().getText().toString().trim();
                prodotto.setNome(nome);
            }
            if (text_codice.getEditText().getText().toString().trim().isEmpty()) {
                text_codice.getEditText().setError("Questo campo non può essere vuoto");
                text_codice.getEditText().requestFocus();
                isEmpty = true;
            } else {
                String codice = text_codice.getEditText().getText().toString().trim();
                prodotto.setCodice(codice);
            }
            if (text_prezzo.getEditText().getText().toString().trim().isEmpty()) {
                text_prezzo.getEditText().setError("Questo campo non può essere vuoto");
                text_prezzo.getEditText().requestFocus();
                isEmpty = true;
            } else {
                String prezzo = text_prezzo.getEditText().getText().toString().trim();
                prodotto.setPrezzo(prezzo);
            }

            if (!isEmpty) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(utenteLoggato.getNegozio());
                Query checkId = reference.child("Products").orderByChild("codice").equalTo(prodotto.getCodice());

                checkId.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            text_codice.getEditText().setError("È stato inserito un id già esistente");
                            text_codice.getEditText().requestFocus();
                        } else {
                            if (imageUri == null) {
                                Toast.makeText(getContext(), "Immagine non selezionata", Toast.LENGTH_SHORT).show();
                            } else {
                                //reference.child("Products").child(prodotto.getCodice()).setValue(prodotto);
                                final StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("Immagini_Prodotti/" + prodotto.getCodice() + ".jpg");
                                UploadTask uploadTask = imageRef.putFile(imageUri);
                                uploadTask.addOnSuccessListener(taskSnapshot -> {
                                    Task<Uri> downloadUrl = imageRef.getDownloadUrl();
                                    downloadUrl.addOnSuccessListener(uri -> {
                                        String imageReference = uri.toString();
                                        //reference.child("Products").child(prodotto.getCodice()).child("URLImmagine").setValue(imageReference);
                                        prodotto.setURLImmagine(imageReference);
                                        reference.child("Products").child(prodotto.getCodice()).setValue(prodotto);
                                        Toast.makeText(getActivity(), "Registrazione effettuata", Toast.LENGTH_SHORT).show();
                                        text_nome.getEditText().getText().clear();
                                        text_codice.getEditText().getText().clear();
                                        text_posizione.getEditText().getText().clear();
                                        text_prezzo.getEditText().getText().clear();
                                    });
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }

        });


        return view;
    }
}
