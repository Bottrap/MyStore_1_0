package com.example.mystore_1_0.Fragments;

import android.os.Bundle;
import android.util.Log;
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
        retrieveAll.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Toast.makeText(getActivity(), String.valueOf(dataSnapshot.getChildrenCount()), Toast.LENGTH_LONG).show();
                    ArrayList<Utente> utenti = new ArrayList<>();
                    ArrayList<String> boh = new ArrayList<>();
                    for(DataSnapshot ds : dataSnapshot.getChildren()){

                        Utente utente = ds.getValue(Utente.class);
                        utenti.add(utente);

                    }
                    ArrayAdapter<Utente> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_expandable_list_item_1, utenti);
                    //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, boh);
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
