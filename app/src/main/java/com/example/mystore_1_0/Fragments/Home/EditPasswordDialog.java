package com.example.mystore_1_0.Fragments.Home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.mystore_1_0.R;
import com.example.mystore_1_0.Utente;
import com.google.android.material.textfield.TextInputEditText;


public class EditPasswordDialog extends AppCompatDialogFragment {

    TextInputEditText insertOldPw, insertNewPw, confirmNewPw;
    ChangePasswordDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Utente utente = getActivity().getIntent().getParcelableExtra("utente");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.edit_password_dialog_layout, null);

        builder.setView(view).setNeutralButton("Annulla", null).setPositiveButton("Ok", null);

        insertOldPw = view.findViewById(R.id.insertOldPw);
        insertNewPw = view.findViewById(R.id.insertNewPw);
        confirmNewPw = view.findViewById(R.id.confirmNewPw);

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialog1 -> {
            Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            positiveButton.setTextColor(getResources().getColor(R.color.arancione));
            dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.arancione));
            positiveButton.setOnClickListener(v -> {

                String oldPw = insertOldPw.getText().toString().trim();
                String newPw = insertNewPw.getText().toString().trim();
                String confirmedNewPw = confirmNewPw.getText().toString().trim();

                if (oldPw.equals(utente.getPassword())) {
                    if (oldPw.equals(newPw)) {
                        insertNewPw.setError("La nuova password non può essere uguale a quella attuale");
                        insertNewPw.requestFocus();
                    } else {
                        if (newPw.equals(confirmedNewPw)) {
                            listener.changePassword(confirmedNewPw);
                            dialog.dismiss();
                        } else {
                            confirmNewPw.setError("Le due password inserite devono coincidere");
                        }
                    }
                } else {
                    insertOldPw.setError("La password attuale inserita è errata");
                    insertOldPw.requestFocus();
                }

            });
        });

        return dialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (ChangePasswordDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "deve implementare ChangePasswordDialogListener");
        }
    }

    public interface ChangePasswordDialogListener {
        void changePassword(String password);
    }
}
