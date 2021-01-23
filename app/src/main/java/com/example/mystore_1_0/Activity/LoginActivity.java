package com.example.mystore_1_0.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mystore_1_0.Prodotto.Prodotto;
import com.example.mystore_1_0.R;
import com.example.mystore_1_0.Utente;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText editId, editPassw;
    MaterialAutoCompleteTextView editStore;
    List<String> negoziDisponibili;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editId = findViewById(R.id.editId);
        editPassw = findViewById(R.id.editPassw);
        editStore = findViewById(R.id.autoCompleteStores);

        Log.d("data", String.valueOf(Calendar.getInstance().getTime() ));

        negoziDisponibili = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query retrieveStores = reference;
        retrieveStores.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    negoziDisponibili.add(ds.getKey());
                }
                negoziDisponibili.remove("StoresEncoding");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, negoziDisponibili);
        editStore.setAdapter(adapter);
    }


    public void clickLogin(View view) {

        String id = editId.getText().toString().trim();
        String password = editPassw.getText().toString().trim();
        String negozio = editStore.getText().toString();

        boolean checkNegozio = false;

        for (int i = 0; i < negoziDisponibili.size(); i++) {
            if (negoziDisponibili.get(i).equals(negozio)) {

                checkNegozio = true;

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(negozio).child("Users");

                Query checkId = reference.orderByChild("id").equalTo(id);
                checkId.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String passwFromDB = dataSnapshot.child(id).child("password").getValue().toString();
                            if (passwFromDB.equals(password)) {

                                String nomeFromDB = dataSnapshot.child(id).child("nome").getValue().toString();
                                String permessiFromDB = dataSnapshot.child(id).child("permessi").getValue().toString();
                                String cognomeFromDB = dataSnapshot.child(id).child("cognome").getValue().toString();
                                String dataNascitaFromDB = dataSnapshot.child(id).child("dataNascita").getValue().toString();
                                String telefonoFromDB = dataSnapshot.child(id).child("telefono").getValue().toString();

                                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                                Utente utente = new Utente(id, password, permessiFromDB, nomeFromDB, cognomeFromDB, dataNascitaFromDB, telefonoFromDB, negozio);
                                intent.putExtra("utente", utente);
                                startActivity(intent);
                            } else {
                                editPassw.setError("È stata inserita una password errata");
                                editPassw.requestFocus();
                            }
                        } else {
                            editId.setError("È stato inserito un Id non esistente");
                            editId.requestFocus();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        }
        if (checkNegozio == false) {
            editStore.setError("Negozio non valido");
            editStore.requestFocus();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

        editId.getText().clear();
        editPassw.getText().clear();
        editStore.getText().clear();
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