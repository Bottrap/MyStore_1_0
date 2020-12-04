package com.example.mystore_1_0.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mystore_1_0.R;
import com.example.mystore_1_0.Utente;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText editId, editPassw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editId = findViewById(R.id.editId);
        editPassw = findViewById(R.id.editPassw);
    }

    public void clickLogin(View view){

        String id = editId.getText().toString().trim();
        String password = editPassw.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("store1").child("Users");

        Query checkId = reference.orderByChild("id").equalTo(id);
        checkId.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String passwFromDB = dataSnapshot.child(id).child("password").getValue().toString();
                    if (passwFromDB.equals(password)){

                        String nomeFromDB = dataSnapshot.child(id).child("nome").getValue().toString();
                        String permessiFromDB = dataSnapshot.child(id).child("permessi").getValue().toString();
                        String cognomeFromDB = dataSnapshot.child(id).child("cognome").getValue().toString();
                        String dataNascitaFromDB = dataSnapshot.child(id).child("dataNascita").getValue().toString();
                        String telefonoFromDB = dataSnapshot.child(id).child("telefono").getValue().toString();


                        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                        Utente utente = new Utente(id, password, permessiFromDB, nomeFromDB, cognomeFromDB, dataNascitaFromDB, telefonoFromDB);
                        intent.putExtra("utente", utente);
                        startActivity(intent);
                    }
                    else{
                        editPassw.setError("È stata inserita una password errata");
                        editPassw.requestFocus();
                    }
                }
                else{
                    editId.setError("È stato inserito un Id non esistente");
                    editId.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        editId.getText().clear();
        editPassw.getText().clear();
    }

/* public void clickBtnRegistrati(View view) {
         rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("Users");

        String id = editId.getText().toString().trim();
        String password = editPassw.getText().toString().trim();

        Utente data = new Utente(id, password);
        reference.child(id).setValue(data);

        Toast.makeText(this, "Registrazione effettuata", Toast.LENGTH_SHORT).show();
    } */
}