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
                        /*
                        String nomeFromDB = dataSnapshot.child(id).child("nome").getValue().toString();
                        String permessiFromDB = dataSnapshot.child(id).child("permessi").getValue().toString();
                        String cognomeFromDB = dataSnapshot.child(id).child("cognome").getValue().toString();
                        String dataNascitaFromDB = dataSnapshot.child(id).child("dataNascita").getValue().toString();
                        String telefonoFromDB = dataSnapshot.child(id).child("telefono").getValue().toString();

                        */
                        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                        /*
                        intent.putExtra("permessi", permessiFromDB);
                        intent.putExtra("cognome", cognomeFromDB);
                        intent.putExtra("telefono", telefonoFromDB);
                        intent.putExtra("dataNascita", dataNascitaFromDB);
                        intent.putExtra("permessi", permessiFromDB);
                        */
                        Utente utente = new Utente(id, password, null, null, null, null, null);
                        intent.putExtra("utente", utente);
                        intent.putExtra("id", id);
                        intent.putExtra("password", password);
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