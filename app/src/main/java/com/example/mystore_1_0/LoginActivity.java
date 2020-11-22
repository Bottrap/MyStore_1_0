package com.example.mystore_1_0;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity {

    EditText editId, editPassw;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editId = findViewById(R.id.editId);
        editPassw = findViewById(R.id.editPassw);

    }

    public void clickLogin(View view){

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("Users");

        String id = editId.getText().toString().trim();
        String password = editPassw.getText().toString().trim();

        Utente data = new Utente(id, password);
        reference.child(id).setValue(data);

        Toast.makeText(this, "Registrazione effettuata", Toast.LENGTH_SHORT).show();
    }

    public void clickBtnProva(View view) {

        String id = editId.getText().toString().trim();
        String password = editPassw.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        Query checkId = reference.orderByChild("id").equalTo(id);
        checkId.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String passwFromDB = dataSnapshot.child(id).child("password").getValue().toString();
                    if (passwFromDB.equals(password)){
                        String idFromDB = dataSnapshot.child(id).child("id").getValue().toString();
                        /*String permessiFromDB = dataSnapshot.child(id).child("permessi").getValue().toString();
                        String nomeFromDB = dataSnapshot.child(id).child("nome").getValue().toString();
                        String cognomeFromDB = dataSnapshot.child(id).child("cognome").getValue().toString();
                        String dataNascitaFromDB = dataSnapshot.child(id).child("dataNascita").getValue().toString();
                        String telefonoFromDB = dataSnapshot.child(id).child("telefono").getValue().toString();
                        Utente utente = new Utente(idFromDB, password, permessiFromDB, nomeFromDB, cognomeFromDB, dataNascitaFromDB, telefonoFromDB);
                        */
                        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                        intent.putExtra("id", idFromDB);
                        /*intent.putExtra("utente", utente);


                        intent.putExtra("permessi", permessiFromDB);
                        intent.putExtra("nome", nomeFromDB);
                        intent.putExtra("cognome", cognomeFromDB);
                        intent.putExtra("telefono", telefonoFromDB);
                        intent.putExtra("dataNascita", dataNascitaFromDB);
                        intent.putExtra("permessi", permessiFromDB);
                        */
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Password errata", Toast.LENGTH_SHORT).show();
                        //password.setError("È stata inserita una password errata");
                        //password.requestFocus();
                    }
                }
                else{
                    Toast.makeText(LoginActivity.this, "Id non trovato", Toast.LENGTH_SHORT).show();
                    //id.setError("È stato inserito un Id non esistente");
                    //id.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
}