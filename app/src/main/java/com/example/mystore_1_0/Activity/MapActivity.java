package com.example.mystore_1_0.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridLayout;

import com.example.mystore_1_0.AutoCompleteProductAdapter;
import com.example.mystore_1_0.Fragments.ShowUsers.ShowUsersAdapter;
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

import java.util.ArrayList;
import java.util.List;


public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mappa);

        MaterialAutoCompleteTextView autoComplete = findViewById(R.id.boh);
        autoComplete.setThreshold(1);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("store1").child("Products");
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
        autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                if (item instanceof Prodotto){
                    Prodotto prodotto = (Prodotto) item;
                    Log.d("prodott",prodotto.getNome());
                    int indice = prodotto.getIndex(prodotto.getPosizione().getIndiceRiga(), prodotto.getPosizione().getIndiceColonna());
                    if (prodotto.getPosizione().getOrientamento().equals(Orientamento.orizzontale)){
                        //ORENTAMENTO ORIZZONTALE
                        if (prodotto.getPosizione().getLunghezza() > 0){ // LUNGHEZZA MAGGIORE DI ZERO QUINDI A DESTRA
                            for (int i = indice; i < indice + prodotto.getPosizione().getLunghezza(); i++) {
                                grid.getChildAt(i).setBackgroundResource(R.drawable.button_shape);
                                grid.getChildAt(i).setVisibility(View.VISIBLE);
                            }
                        }else{ // LUNGHEZZA MINORE DI ZERO QUINDI A SINISTRA
                            for (int i = indice; i > indice + prodotto.getPosizione().getLunghezza(); i--) {
                                grid.getChildAt(i).setBackgroundResource(R.drawable.button_shape);
                                grid.getChildAt(i).setVisibility(View.VISIBLE);

                            }
                        }
                    }else{ // ORIENTAMENTO VERTICALE
                        if (prodotto.getPosizione().getLunghezza() > 0){ // LUNGHEZZA MAGGIORE DI ZERO QUINDI VERSO IL BASSO
                            for (int i = indice; i < indice + (prodotto.getPosizione().getLunghezza()*33); i=i+33) {
                                grid.getChildAt(i).setBackgroundResource(R.drawable.button_shape);
                                grid.getChildAt(i).setVisibility(View.VISIBLE);

                            }
                        }else{ // LUNGHEZZA MINORE DI ZERO QUINDI VERSO L'ALTO
                            for (int i = indice; i > indice + (prodotto.getPosizione().getLunghezza()*33); i=i-33) {
                                grid.getChildAt(i).setBackgroundResource(R.drawable.button_shape);
                                grid.getChildAt(i).setVisibility(View.VISIBLE);

                            }
                        }
                    }


                }
            }
        });



    }
}