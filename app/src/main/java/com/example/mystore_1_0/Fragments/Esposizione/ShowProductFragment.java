package com.example.mystore_1_0.Fragments.Esposizione;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.GridLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.mystore_1_0.AutoCompleteProductAdapter;
import com.example.mystore_1_0.Fragments.DashboardFragment;
import com.example.mystore_1_0.IOnBackPressed;
import com.example.mystore_1_0.Orientamento;
import com.example.mystore_1_0.Prodotto.Prodotto;
import com.example.mystore_1_0.R;
import com.example.mystore_1_0.Utente;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
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

public class ShowProductFragment extends Fragment implements IOnBackPressed {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_showproduct, null);

        // UTENTE LOGGATO PER VEDERE IN CHE NEGOZIO E'
        Utente utenteLoggato = getActivity().getIntent().getParcelableExtra("utente");
        String negozio = utenteLoggato.getNegozio();

        MaterialAutoCompleteTextView autoComplete = view.findViewById(R.id.autoCompleteTextView);
        GridLayout gridLayout = view.findViewById(R.id.gridlayout);


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

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(negozio).child("Products").child("Esposizione");
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
                    AutoCompleteProductAdapter adapter = new AutoCompleteProductAdapter(view.getContext(), listaProdotti);
                    autoComplete.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });

        GridLayout grid = view.findViewById(R.id.gridlayout);

        autoComplete.setOnItemClickListener((parent, view1, position, id) -> {
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
                // CHIUSURA TASTIERA
                View vista = getActivity().getCurrentFocus();
                if (vista != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(vista.getWindowToken(), 0);
                }
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

