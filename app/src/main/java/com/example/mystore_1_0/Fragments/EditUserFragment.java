package com.example.mystore_1_0.Fragments;

import android.os.Bundle;
import android.util.Log;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.mystore_1_0.Fragments.ShowUsers.ShowUsersFragment;
import com.example.mystore_1_0.IOnBackPressed;
import com.example.mystore_1_0.R;
import com.example.mystore_1_0.Utente;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class EditUserFragment extends Fragment implements IOnBackPressed {

    Utente utente;

    public EditUserFragment(Utente utente) {
        this.utente = utente;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edituser, container, false);

        Log.d("utente", utente.getNome());

        //aggancio variabili
        TextInputLayout text_mod_nome = view.findViewById(R.id.text_mod_nome);
        TextInputLayout text_mod_cognome = view.findViewById(R.id.text_mod_cognome);
        TextInputLayout text_mod_id = view.findViewById(R.id.text_mod_id);
        TextInputLayout text_mod_password = view.findViewById(R.id.text_mod_password);
        TextInputLayout text_mod_phone = view.findViewById(R.id.text_mod_phone);
        DatePicker text_mod_date = view.findViewById(R.id.text_mod_date);
        Button btn_mod = view.findViewById(R.id.btn_mod);
        AutoCompleteTextView text_mod_permessi = view.findViewById(R.id.text_mod_permessi);

        //dropdown menu permessi
        String [] tipopermessi = new String[] {"1","2","3"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, tipopermessi);
        text_mod_permessi.setText(utente.getPermessi());
        text_mod_permessi.setAdapter(adapter);

        text_mod_nome.getEditText().setText(utente.getNome());
        text_mod_cognome.getEditText().setText(utente.getCognome());
        text_mod_id.getEditText().setText(utente.getId());
        text_mod_password.getEditText().setText(utente.getPassword());
        text_mod_phone.getEditText().setText(utente.getTelefono());
        text_mod_date.init(utente.getYear(utente.getDataNascita()), utente.getMonth(utente.getDataNascita())-1, utente.getDay(utente.getDataNascita()), null);

        btn_mod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isEmpty = false;
                String id, password, permessi, nome, cognome, telefono;
                Utente newUtente = new Utente();

                if(text_mod_id.getEditText().getText().toString().trim().isEmpty()) {
                    text_mod_id.getEditText().setError("Questo campo non può essere vuoto");
                    text_mod_id.getEditText().requestFocus();
                    isEmpty = true;
                } else {
                    id = text_mod_id.getEditText().getText().toString().trim();
                    newUtente.setId(id);
                }

                if(text_mod_nome.getEditText().getText().toString().trim().isEmpty()) {
                    text_mod_nome.getEditText().setError("Questo campo non può essere vuoto");
                    text_mod_nome.getEditText().requestFocus();
                    isEmpty = true;
                } else {
                    nome = text_mod_nome.getEditText().getText().toString().trim();
                    newUtente.setNome(nome);
                }

                if(text_mod_cognome.getEditText().getText().toString().trim().isEmpty()) {
                    text_mod_cognome.getEditText().setError("Questo campo non può essere vuoto");
                    text_mod_cognome.getEditText().requestFocus();
                    isEmpty = true;
                } else {
                    cognome = text_mod_cognome.getEditText().getText().toString().trim();
                    newUtente.setCognome(cognome);
                }

                if(text_mod_password.getEditText().getText().toString().trim().isEmpty()) {
                    text_mod_password.getEditText().setError("Questo campo non può essere vuoto");
                    text_mod_password.getEditText().requestFocus();
                    isEmpty = true;

                } else {
                    password = text_mod_password.getEditText().getText().toString().trim();
                    newUtente.setPassword(password);
                }
                    permessi = text_mod_permessi.getText().toString().trim();
                    newUtente.setPermessi(permessi);

                if(text_mod_phone.getEditText().getText().toString().trim().isEmpty()) {
                    text_mod_phone.getEditText().setError("Questo campo non può essere vuoto");
                    text_mod_phone.getEditText().requestFocus();
                    isEmpty = true;
                } else {
                    telefono = text_mod_phone.getEditText().getText().toString().trim();
                    newUtente.setTelefono(telefono);
                }

                if (!isEmpty) {
                    newUtente.setDataNascita(String.valueOf(text_mod_date.getDayOfMonth()) + "/" + String.valueOf(text_mod_date.getMonth() + 1) + "/" + String.valueOf(text_mod_date.getYear()));

                    //database
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("store1");
                    Query checkId = reference.child("Users").orderByChild("id").equalTo(newUtente.getId());

                    checkId.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!(newUtente.getId().equals(utente.getId()))) {
                                if (dataSnapshot.exists()) {
                                    text_mod_id.setError("È stato inserito un id già esistente");
                                    text_mod_id.requestFocus();
                                } else {
                                    reference.child("Users").child(newUtente.getId()).setValue(newUtente);
                                    reference.child("Users").child(utente.getId()).removeValue();
                                    Toast.makeText(getActivity(), "Modifica effettuata", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                reference.child("Users").child(utente.getId()).setValue(newUtente);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

        return view;
    }

    @Override
    public boolean onBackPressed() {
        AppCompatActivity activity = (AppCompatActivity) getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShowUsersFragment()).commit();
        return true;
    }
}
