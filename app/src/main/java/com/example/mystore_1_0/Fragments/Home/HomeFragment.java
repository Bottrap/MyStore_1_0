package com.example.mystore_1_0.Fragments.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mystore_1_0.R;
import com.example.mystore_1_0.Utente;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

    MaterialTextView showId, showNome, showCognome, showPermessi, showDataNascita, showTelefono;
    MaterialButton pwEdit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        MaterialTextView welcomeTxtView = view.findViewById(R.id.welcomeTxtView);
        Utente utente = getActivity().getIntent().getParcelableExtra("utente");
        String welcome = "Benvenuto ".concat(utente.getNome()).concat(" ").concat(utente.getCognome());
        welcomeTxtView.setText(welcome);

        showNome = view.findViewById(R.id.showNome);
        showCognome = view.findViewById(R.id.showCognome);
        showId = view.findViewById(R.id.showId);
        showPermessi = view.findViewById(R.id.showPermessi);
        showDataNascita = view.findViewById(R.id.showDataNascita);
        showTelefono = view.findViewById(R.id.showTelefono);
        pwEdit = view.findViewById(R.id.pwEdit);

        showNome.setText(utente.getNome());
        showCognome.setText(utente.getCognome());
        showId.setText(utente.getId());
        showPermessi.setText(utente.getPermessi());
        showTelefono.setText(utente.getTelefono());
        showDataNascita.setText(utente.getDataNascita());

        pwEdit.setOnClickListener(v -> {
            ChangePasswordDialog changePasswordDialog = new ChangePasswordDialog();
            changePasswordDialog.show(getActivity().getSupportFragmentManager(), "Change Password Dialog");
        });

        return view;
    }
    }

