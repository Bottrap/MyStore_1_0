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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

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
import java.util.Date;

public class AddUsersFragment extends Fragment implements IOnBackPressed {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addusers, container, false);

        Utente utenteLoggato = getActivity().getIntent().getParcelableExtra("utente");

        //dropdown menu permessi
        String [] tipopermessi = new String[] {"1","2","3"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, tipopermessi);
        AutoCompleteTextView text_permessi = view.findViewById(R.id.text_permessi);
        text_permessi.setAdapter(adapter);
        text_permessi.setOnItemClickListener((parent, viewboh, position, id) -> {
            text_permessi.setError(null);
        });

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

                boolean isEmpty = false;
                String id, password, permessi, nome, cognome, telefono;
                Utente utente = new Utente();

                if(text_id.getEditText().getText().toString().trim().isEmpty()) {
                    text_id.getEditText().setError("Questo campo non può essere vuoto");
                    text_id.getEditText().requestFocus();
                    isEmpty = true;
                } else {
                    id = text_id.getEditText().getText().toString().trim();
                    utente.setId(id);
                }

                if(text_nome.getEditText().getText().toString().trim().isEmpty()) {
                    text_nome.getEditText().setError("Questo campo non può essere vuoto");
                    text_nome.getEditText().requestFocus();
                    isEmpty = true;
                } else {
                    nome = text_nome.getEditText().getText().toString().trim();
                    utente.setNome(nome);
                }

                if(text_cognome.getEditText().getText().toString().trim().isEmpty()) {
                    text_cognome.getEditText().setError("Questo campo non può essere vuoto");
                    text_cognome.getEditText().requestFocus();
                    isEmpty = true;
                } else {
                    cognome = text_cognome.getEditText().getText().toString().trim();
                    utente.setCognome(cognome);
                }

                if(text_password.getEditText().getText().toString().trim().isEmpty()) {
                    text_password.getEditText().setError("Questo campo non può essere vuoto");
                    text_password.getEditText().requestFocus();
                    isEmpty = true;
                } else {
                    password = text_password.getEditText().getText().toString().trim();
                    utente.setPassword(password);
                }

                if(text_permessi.getText().toString().trim().isEmpty()) {
                    text_permessi.setError("Questo campo non può essere vuoto");
                    text_permessi.requestFocus();
                    isEmpty = true;
                } else {
                    permessi = text_permessi.getText().toString().trim();
                    utente.setPermessi(permessi);
                }

                if(text_phone.getEditText().getText().toString().trim().isEmpty()) {
                    text_phone.getEditText().setError("Questo campo non può essere vuoto");
                    text_phone.getEditText().requestFocus();
                    isEmpty = true;
                } else if (text_phone.getEditText().getText().toString().trim().length() != 10){
                    text_phone.getEditText().setError("Inserisci un n° di telefono valido");
                    text_phone.getEditText().requestFocus();
                    isEmpty = true;
                } else {
                    telefono = text_phone.getEditText().getText().toString().trim();
                    utente.setTelefono(telefono);
                }

                if(text_date.getYear() >= getCurrentYear() ){
                    Toast.makeText(getActivity(), "Inserisci una data valida", Toast.LENGTH_SHORT).show();
                    isEmpty = true;
                }


                if (!isEmpty){
                    utente.setDataNascita(String.valueOf(text_date.getDayOfMonth()) + "/" + String.valueOf(text_date.getMonth()+1) + "/" + String.valueOf(text_date.getYear()));

                    //database
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference(utenteLoggato.getNegozio());
                    Query checkId = reference.child("Users").orderByChild("id").equalTo(text_id.getEditText().getText().toString().trim());

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
                                text_permessi.getText().clear();
                                text_phone.getEditText().getText().clear();
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

    public static int getCurrentYear() {
        String[] parse = String.valueOf(Calendar.getInstance().getTime()).split("\\s+");
        int year = Integer.parseInt(parse[5]);
        return year;
    }

    @Override
    public boolean onBackPressed() {
        AppCompatActivity activity = (AppCompatActivity) getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DashboardFragment()).commit();
        return true;
    }
}
