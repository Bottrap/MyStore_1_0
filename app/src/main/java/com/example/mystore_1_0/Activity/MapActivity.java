package com.example.mystore_1_0.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.mystore_1_0.AutoCompleteProductAdapter;
import com.example.mystore_1_0.Fragments.ShowUsers.ShowUsersAdapter;
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

        MaterialAutoCompleteTextView boh = findViewById(R.id.boh);

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
                    boh.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        //Button btn = findViewById(R.id.btn_0_0);
        //GridLayout gridLayout = findViewById(R.id.gridlayout);

        //gridLayout.getChildAt(85).setVisibility(View.VISIBLE);

        //btn.setVisibility(View.VISIBLE);


    }
}