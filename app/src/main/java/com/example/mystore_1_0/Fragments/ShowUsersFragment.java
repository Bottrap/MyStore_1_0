package com.example.mystore_1_0.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mystore_1_0.R;
import com.example.mystore_1_0.Utente;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowUsersFragment extends Fragment {

    ListView listaDipendenti;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_showusers, container, false);

        listaDipendenti = view.findViewById(R.id.listView);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("store1").child("Users");
        Query retrieveAll = reference.orderByKey();
        retrieveAll.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    ArrayList<Utente> utenti = new ArrayList<>();
                    Iterable<DataSnapshot> userschild = dataSnapshot.getChildren();
                    for(DataSnapshot ds : userschild){

                        String id = (ds.child("id").getValue().toString());
                        String password = (ds.child("password").getValue().toString());
                        String permessi = (ds.child("permessi").getValue().toString());
                        String nome = (ds.child("nome").getValue().toString());
                        String cognome = (ds.child("cognome").getValue().toString());
                        String dataNascita = (ds.child("dataNascita").getValue().toString());
                        String telefono = (ds.child("telefono").getValue().toString());

                        Utente utente = new Utente(id, password, permessi, nome, cognome, dataNascita, telefono);
                        utenti.add(utente);
                    }
                    ArrayAdapter<Utente> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, utenti);
                    listaDipendenti.setAdapter(arrayAdapter);
                }
                else{
                    Toast.makeText(getActivity(), "Dati non trovati", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        return view;
    }

}
