package com.example.mystore_1_0;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

        String id = editId.getText().toString();
        String password = editPassw.getText().toString();

        UserData data = new UserData(id, password);
        reference.setValue(data);

        Toast.makeText(this, "registrazione effettuata", Toast.LENGTH_SHORT).show();
    }
}