package com.example.mystore_1_0.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.mystore_1_0.Activity.ProfileActivity;
import com.example.mystore_1_0.AutoCompleteProductAdapter;
import com.example.mystore_1_0.Orientamento;
import com.example.mystore_1_0.Prodotto.Posizione;
import com.example.mystore_1_0.Prodotto.Prodotto;
import com.example.mystore_1_0.R;
import com.example.mystore_1_0.Utente;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.example.mystore_1_0.Prodotto.Posizione.getPosition;

public class ShowProductFragment extends Fragment {
    List<Prodotto> listaProdotti; // LISTA PRODOTTI PER AUTOCOMPLETE
    Prodotto prodInDb; // VARIABILE PER SALVARE LE INFO DEL PRODOTTO SELEZIONATO PRIMA DELLA MODIFICA
    boolean isClicked = false; // PRIMO CLICK SU UN BOTTONE
    boolean is2Clicked = false; // SECONDO CLICK SU UN BOTTONE
    boolean positionHasChanged = false; // VERIFICO SE LA POSIZIONE E' STATA MODIFICATA
    int indicePrecedente;
    int indiceSuccessivo = 500;
    Posizione posizione;
    int lunghezza = 1;

    private static final int PICK_IMAGE = 1;
    Uri imageUri = null;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            imageUri = data.getData();
            Toast.makeText(getContext(), "Immagine modificata correttamente", Toast.LENGTH_SHORT).show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragment_showproduct, container, false);
        View view = View.inflate(getActivity(), R.layout.fragment_showproduct, null);

        // UTENTE LOGGATO PER VEDERE IN CHE NEGOZIO E'
        Utente utenteLoggato = getActivity().getIntent().getParcelableExtra("utente");
        String negozio = utenteLoggato.getNegozio();

        // DICHIARO ELEMENTI DELL'INTERFACCIA
        MaterialAutoCompleteTextView autoComplete = view.findViewById(R.id.autoCompleteTextView);
        GridLayout gridLayout = view.findViewById(R.id.gridShowProduct);
        TextInputLayout name_editText = view.findViewById(R.id.name_editText);
        TextInputLayout code_editText = view.findViewById(R.id.code_editText);
        TextInputLayout price_editText = view.findViewById(R.id.price_editText);
        TextInputLayout position_editText = view.findViewById(R.id.position_editText);
        MaterialButton confirmBtn = view.findViewById(R.id.confirmBtn);
        CheckBox checkBox = view.findViewById(R.id.editCheck);

        // DICHIARO BOTTONE DEL QUALE MI SERVE IL BACKGROUND, PER RESETTARE IL BACKGROUND DEI BOTTONI PRESENTI NEL GRID LAYOUT
        MaterialButton button = new MaterialButton(getContext());
        Drawable background = button.getBackground();

        //RENDO TUTTI I BOTTONI INVISIBILI APPENA IL FRAGMENT VIENE CREATO
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            gridLayout.getChildAt(i).setVisibility(View.INVISIBLE);
        }

        // CARICO LA MAPPA DEL NEGOZIO RELATIVO ALL'UTENTE LOGGATO
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

        // CARICO I PRODOTTI PRESENTI NEL DATABASE, DA FAR VISUALIZZARE NELLA LISTA (NELL'AUTOCOMPLETE TEXT VIEW)
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
                    AutoCompleteProductAdapter adapter = new AutoCompleteProductAdapter(view.getContext(), listaProdotti);
                    autoComplete.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        // CLICK SUGLI ELEMENTI DELLA LISTA
        autoComplete.setOnItemClickListener((parent, view12, position, id) -> {
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
                prodInDb = prodotto;

                Log.d("img", prodotto.getURLImmagine());
                Log.d("imgDb", prodInDb.getURLImmagine());

                name_editText.getEditText().setText(prodotto.getNome());
                code_editText.getEditText().setText(prodotto.getCodice());
                price_editText.getEditText().setText(prodotto.getPrezzo());
                Posizione posizione = prodotto.getPosizione();

                //VISUALIZZAZIONE IMMAGINE DEL PRODOTTO NELLA IMAGEVIEW
                String imgPath = "Immagini_Prodotti/" + prodotto.getCodice() + ".jpg";
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(imgPath);
                try {
                    final File localFile = File.createTempFile(prodotto.getCodice(), "jpg");
                    storageReference.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        ImageView productImgView = view.findViewById(R.id.product_imgView);
                        productImgView.setImageBitmap(bitmap);
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // VISUALIZZA SULLA MAPPA LA POSIZIONE DEL PRODOTTO
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
        });

        // CLICK SULLA CHECKBOX PER ATTIVARE LE MODIFICHE
        checkBox.setOnClickListener(v -> {
            if (autoComplete.getText().toString().isEmpty()) { // SE NON HO SELEZIONATO ALCUN PRODOTTO DALL'AUTOCOMPLETE
                checkBox.setChecked(false);
                Toast.makeText(getActivity(), "Selezionare prima un prodotto da modificare", Toast.LENGTH_SHORT).show();
            } else {
                if (checkBox.isChecked()) {     //CheckBox selezionata
                    confirmBtn.setBackgroundColor(getResources().getColor(R.color.scanStatusBarColor));
                    confirmBtn.setClickable(true);
                } else {    //CheckBox non selezionata
                    confirmBtn.setBackgroundColor(getResources().getColor(R.color.gray));
                    confirmBtn.setClickable(false);
                }
            }

        });

        // CLICK SU MODIFICA IMMAGINE
        MaterialButton editImg = view.findViewById(R.id.editImg_btn);
        editImg.setOnClickListener(v -> {
            //prendo l'immagine dalla galleria come uri
            Intent galleria = new Intent();
            galleria.setType("image/*");
            galleria.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(galleria, "Seleziona un'immagine"), PICK_IMAGE);
        });

        // CLICK SU CANCELLA POSIZIONE
        position_editText.setEndIconOnClickListener(v -> {
            for (int i = 0; i < gridLayout.getChildCount(); i++) {
                gridLayout.getChildAt(i).setBackground(background);
                gridLayout.getChildAt(i).setVisibility(View.VISIBLE);
                is2Clicked = false;
                isClicked = false;
                position_editText.getEditText().getText().clear();
                positionHasChanged = true;
            }

            // ON CLICK LISTENER SU TUTTI I BOTTONI
            for (int i = 0; i < gridLayout.getChildCount(); i++) {

                final int finalI = i;
                gridLayout.getChildAt(i).setOnClickListener(view1 -> {
                    if (!is2Clicked) {  // SE NON E' STATO CLICCATO UN SECONDO BOTTONE
                        if (!isClicked) {  // SE NON E' STATO CLICCATO NULLA, QUINDI PRIMO CLICK
                            position_editText.getEditText().setError(null);
                            isClicked = true;
                            position_editText.getEditText().setText(getPosition(finalI).getIndiceRiga() + ", " + getPosition(finalI).getIndiceColonna());
                            indicePrecedente = finalI;
                            gridLayout.getChildAt(finalI).setBackgroundResource(R.drawable.button_shape);
                            posizione = getPosition(indicePrecedente);

                        } else { // E' STATO CLICCATO GIA' UN BOTTONE, QUINDI CODICE PER IL SECONDO BOTTONE
                            lunghezza = 0;
                            is2Clicked = true;
                            indiceSuccessivo = finalI;
                            if (getPosition(indicePrecedente).getIndiceRiga() == getPosition(indiceSuccessivo).getIndiceRiga()) {
                                // SE IL SECONDO BOTTONE E' SULLA STESSA RIGA DEL PRIMO
                                if (indicePrecedente < indiceSuccessivo) { // SE IL 2 BOTTONE E' A DESTRA DEL 1 BOTTONE
                                    for (int j = indicePrecedente; j <= indiceSuccessivo; j++) {
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

                                if (indicePrecedente < indiceSuccessivo) { // SE IL 2 BOTTONE E' IN BASSO RISPETTO AL PRIMO

                                    for (int j = indicePrecedente; j <= indiceSuccessivo; j = j + 33) {
                                        gridLayout.getChildAt(j).setBackgroundResource(R.drawable.button_shape);
                                        position_editText.getEditText().setText(getPosition(indicePrecedente).getIndiceRiga() + ", " + getPosition(indicePrecedente).getIndiceColonna() + " -> " + getPosition(finalI).getIndiceRiga() + ", " + getPosition(finalI).getIndiceColonna());
                                        lunghezza++;
                                    }
                                    //posizione.setLunghezza(lunghezza);
                                } else if (indicePrecedente > indiceSuccessivo) { // SE IL SECONDO BOTTONE E' IN ALTO RISPETTO AL PRIMO
                                    for (int j = indicePrecedente; j >= indiceSuccessivo; j = j - 33) {
                                        gridLayout.getChildAt(j).setBackgroundResource(R.drawable.button_shape);
                                        position_editText.getEditText().setText(getPosition(indicePrecedente).getIndiceRiga() + ", " + getPosition(indicePrecedente).getIndiceColonna() + " -> " + getPosition(finalI).getIndiceRiga() + ", " + getPosition(finalI).getIndiceColonna());
                                        lunghezza--;
                                    }

                                } else { // CLICK IN DIAGONALE (NON FA NIENTE)
                                    lunghezza = 1;
                                    is2Clicked = false;
                                }
                            }

                        }

                    }
                });
            }
        });

        // CONFERMA MODIFICA
        confirmBtn.setOnClickListener(v -> {
            boolean isEmpty = false;
            Prodotto prodotto = new Prodotto();

            // CONTROLLO SULLA POSIZIONE
            if (positionHasChanged) { // SE HO CLICCATO SU CANCELLA POSIZIONE
                if (isClicked) { // SE E' STATO CLICCATO ALMENO UN BOTTONE SULLA MAPPA
                    posizione.setLunghezza(lunghezza);
                    prodotto.setPosizione(posizione);
                } else {
                    position_editText.getEditText().setError("Questo campo non può essere vuoto");
                    position_editText.getEditText().requestFocus();
                    isEmpty = true;
                }
            } else { // MODIFICA DATI PRODOTTO SENZA MODIFICARE LA POSIZIONE
                prodotto.setPosizione(prodInDb.getPosizione());
            }

            if (name_editText.getEditText().getText().toString().trim().isEmpty()) {
                name_editText.getEditText().setError("Questo campo non può essere vuoto");
                name_editText.getEditText().requestFocus();
                isEmpty = true;
            } else {
                prodotto.setNome(name_editText.getEditText().getText().toString().trim());
            }
            if (code_editText.getEditText().getText().toString().trim().isEmpty()) {
                code_editText.getEditText().setError("Questo campo non può essere vuoto");
                code_editText.getEditText().requestFocus();
                isEmpty = true;
            } else {
                prodotto.setCodice(code_editText.getEditText().getText().toString().trim());
            }
            if (price_editText.getEditText().getText().toString().trim().isEmpty()) {
                price_editText.getEditText().setError("Questo campo non può essere vuoto");
                price_editText.getEditText().requestFocus();
                isEmpty = true;
            } else {
                prodotto.setPrezzo(price_editText.getEditText().getText().toString().trim());
            }

            if (!isEmpty) {
                Query checkId = reference.orderByChild("codice").equalTo(prodotto.getCodice());

                checkId.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (prodInDb.getCodice().equals(prodotto.getCodice())) { // CODICE UGUALE AL PRECEDENTE (NON MODIFICATO)
                            reference.child(prodotto.getCodice()).setValue(prodotto);
                            // CONTROLLO SULL'IMMAGINE
                            if (imageUri == null) {
                                reference.child(prodotto.getCodice()).child("urlimmagine").setValue(prodInDb.getURLImmagine());
                            } else {
                                final StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("Immagini_Prodotti/" + prodotto.getCodice() + ".jpg");
                                UploadTask uploadTask = imageRef.putFile(imageUri);
                                uploadTask.addOnSuccessListener(taskSnapshot -> {
                                    Task<Uri> downloadUrl = imageRef.getDownloadUrl();
                                    downloadUrl.addOnSuccessListener(uri -> {
                                        String imageReference = uri.toString();
                                        reference.child(prodotto.getCodice()).child("urlimmagine").setValue(imageReference);
                                    });
                                });
                            }
                            Toast.makeText(getActivity(), "Modifica effettuata", Toast.LENGTH_SHORT).show();
                            AppCompatActivity activity = (AppCompatActivity) getContext();
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShowProductFragment()).commit();
                        } else { // CODICE NON UGUALE AL PRECEDENTE (QUINDI MODIFICATO)
                            if (dataSnapshot.exists()) { // IL NUOVO CODICE INSERITO E' GIA' IN USO
                                code_editText.getEditText().setError("È stato inserito un id già esistente");
                                code_editText.getEditText().requestFocus();
                            } else { // IL NUOVO CODICE E' UTILIZZABILE
                                reference.child(prodotto.getCodice()).setValue(prodotto);
                                // CONTROLLO SULL'IMMAGINE
                                if (imageUri == null) {
                                    reference.child(prodotto.getCodice()).child("urlimmagine").setValue(prodInDb.getURLImmagine());
                                } else {
                                    final StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("Immagini_Prodotti/" + prodotto.getCodice() + ".jpg");
                                    UploadTask uploadTask = imageRef.putFile(imageUri);
                                    uploadTask.addOnSuccessListener(taskSnapshot -> {
                                        Task<Uri> downloadUrl = imageRef.getDownloadUrl();
                                        downloadUrl.addOnSuccessListener(uri -> {
                                            String imageReference = uri.toString();
                                            reference.child(prodotto.getCodice()).child("urlimmagine").setValue(imageReference);
                                        });
                                    });
                                }
                                reference.child(prodInDb.getCodice()).removeValue();
                                Toast.makeText(getActivity(), "Registrazione effettuata", Toast.LENGTH_SHORT).show();

                                AppCompatActivity activity = (AppCompatActivity) getContext();
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShowProductFragment()).commit();

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
