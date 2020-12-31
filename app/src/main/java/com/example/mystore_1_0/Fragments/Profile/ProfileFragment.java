package com.example.mystore_1_0.Fragments.Profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.mystore_1_0.R;
import com.example.mystore_1_0.Utente;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class ProfileFragment extends Fragment implements EditPhoneDialog.EditPhoneDialogListener {

    MaterialTextView showId, showNome, showCognome, showPermessi, showDataNascita, showTelefono;
    MaterialButton pwEdit, phoneEdit;
    CardView showUsersCardView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

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
        phoneEdit = view.findViewById(R.id.phoneEdit);
        showUsersCardView = view.findViewById(R.id.showusers_cardview);

        showNome.setText(utente.getNome());
        showCognome.setText(utente.getCognome());
        showId.setText(utente.getId());
        showPermessi.setText(utente.getPermessi());
        showTelefono.setText(utente.getTelefono());
        showDataNascita.setText(utente.getDataNascita());

        pwEdit.setOnClickListener(v -> {
            EditPasswordDialog editPasswordDialog = new EditPasswordDialog();
            editPasswordDialog.show(getActivity().getSupportFragmentManager(), "Change Password Dialog");
        });

        phoneEdit.setOnClickListener(v -> {
            EditPhoneDialog editPhoneDialog = new EditPhoneDialog();
            editPhoneDialog.setTargetFragment(this, 0);
            editPhoneDialog.show(getActivity().getSupportFragmentManager(), "Change Phone Dialog");
        });

        return view;
    }

    @Override
    public void changePhoneNumber(String phone) {
        showTelefono.setText(phone);
        Toast.makeText(getContext(), "Numero di telefono modificato correttamente", Toast.LENGTH_SHORT).show();
    }
}

