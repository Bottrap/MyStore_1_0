package com.example.mystore_1_0.Fragments.ShowUsers;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mystore_1_0.R;
import com.example.mystore_1_0.ShowUsersAdapter;
import com.example.mystore_1_0.Utente;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowUsersFragment extends Fragment {

    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_showusers, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("store1").child("Users");
        Query retrieveAll = reference.orderByKey();
        retrieveAll.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    List<Utente> listaUtenti = new ArrayList<>();
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        Utente utente = ds.getValue(Utente.class);
                        listaUtenti.add(utente);
                    }

                    ShowUsersAdapter userAdapter = new ShowUsersAdapter(listaUtenti);
                    recyclerView.setAdapter(userAdapter);


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
