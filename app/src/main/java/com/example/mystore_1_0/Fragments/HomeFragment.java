package com.example.mystore_1_0.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mystore_1_0.R;
import com.example.mystore_1_0.Utente;
import com.google.android.material.textview.MaterialTextView;

public class HomeFragment extends Fragment {

    MaterialTextView showId, showNome, showCognome, showPassword, showPermessi, showDataNascita, showTelefono;
    ImageButton pwCheck;

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
        showPassword = view.findViewById(R.id.showPassword);
        showPermessi = view.findViewById(R.id.showPermessi);
        showDataNascita = view.findViewById(R.id.showDataNascita);
        showTelefono = view.findViewById(R.id.showTelefono);
        pwCheck = view.findViewById(R.id.pwCheck);

        showNome.setText(utente.getNome());
        showCognome.setText(utente.getCognome());
        showId.setText(utente.getId());
        showPassword.setText(utente.getPassword());
        showPermessi.setText(utente.getPermessi());
        showTelefono.setText(utente.getTelefono());
        showDataNascita.setText(utente.getDataNascita());

        pwCheck.setOnClickListener(v -> {
            if(showPassword.getInputType() == 0x00000001) {
                showPassword.setInputType(0x00000081);
            }else if(showPassword.getInputType() == 0x00000081){
                showPassword.setInputType(0x00000001);
            }
        });

        return view;
    }
}
