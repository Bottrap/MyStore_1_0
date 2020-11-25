package com.example.mystore_1_0.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mystore_1_0.ProfileActivity;
import com.example.mystore_1_0.R;
import com.example.mystore_1_0.Utente;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddUsersFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addusers, container, false);
        Toast.makeText(getActivity(), "Aggiungi Dipendenti", Toast.LENGTH_SHORT).show();

        //dropdown menu permessi
        String [] tipopermessi = new String[] {"1","2","3"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, tipopermessi);
        AutoCompleteTextView text_permessi = view.findViewById(R.id.text_permessi);
        text_permessi.setAdapter(adapter);

        //aggancio variabili
        TextInputLayout text_nome = view.findViewById(R.id.text_nome);
        TextInputLayout text_cognome = view.findViewById(R.id.text_cognome);
        TextInputLayout text_id = view.findViewById(R.id.text_id);
        TextInputLayout text_password = view.findViewById(R.id.text_password);
        TextInputLayout text_phone = view.findViewById(R.id.text_phone);
        DatePicker text_date = view.findViewById(R.id.text_date);
        Button btn_add = view.findViewById(R.id.btn_add);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get data
                String id = (text_id.getEditText().getText().toString().trim());
                String password = (text_password.getEditText().getText().toString().trim());
                String permessi = (text_permessi.getText().toString().trim());
                String nome = (text_nome.getEditText().getText().toString().trim());
                String cognome = (text_cognome.getEditText().getText().toString().trim());
                String dataNascita = (String.valueOf(text_date.getDayOfMonth()) + "/" + String.valueOf(text_date.getMonth()+1) + "/" + String.valueOf(text_date.getYear()));
                String telefono = (text_phone.getEditText().getText().toString().trim());
                //crea utente
                Utente utente = new Utente(id, password, permessi, nome, cognome, dataNascita, telefono);

                //database
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("store1");
                Query checkId = reference.child("Users").orderByChild("id").equalTo(id);

                checkId.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            text_id.setError("È stato inserito un id già esistente");
                            text_id.requestFocus();
                        }
                        else{
                            reference.child("Users").child(utente.getId()).setValue(utente);
                            Toast.makeText(getActivity(), "Registrazione effettuata", Toast.LENGTH_SHORT).show();
                            text_nome.getEditText().getText().clear();
                            text_cognome.getEditText().getText().clear();
                            text_id.getEditText().getText().clear();
                            text_password.getEditText().getText().clear();
                            text_permessi.clearListSelection();
                            text_phone.getEditText().getText().clear();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });







                Toast.makeText(getActivity(), permessi +" uela "+ dataNascita, Toast.LENGTH_SHORT).show();

            }
        });

        return view;




    }
}
