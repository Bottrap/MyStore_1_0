package com.example.mystore_1_0.Fragments.Profile;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EditPhoneDialog extends AppCompatDialogFragment{
    EditPhoneDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        Utente utente = getActivity().getIntent().getParcelableExtra("utente");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.editphone_dialog, null);

        dialogBuilder.setView(view)
                .setPositiveButton("Conferma", null)
                .setNeutralButton("Annulla", null);

        TextInputEditText newPhone = view.findViewById(R.id.insertNewPhone);
        AlertDialog dialog = dialogBuilder.create();

        dialog.setOnShowListener(dialog1 -> {
            Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            positiveButton.setTextColor(getResources().getColor(R.color.arancione));
            dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.arancione));
            positiveButton.setOnClickListener(v -> {
                if (newPhone.getText().toString().trim().length() != 10) {
                    newPhone.setError("Inserire un n° di telefono valido");
                    newPhone.requestFocus();
                } else {

                    String phone = newPhone.getText().toString().trim();

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference(utente.getNegozio()).child("Users");
                    Query checkId = reference.orderByChild("id").equalTo(utente.getId());
                    checkId.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            reference.child(utente.getId()).child("telefono").setValue(phone);
                            listener.changePhoneNumber(phone);
                            dialog.dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            });

        });

        return dialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (EditPhoneDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "deve implementare EditPhoneDialogListener");
        }
    }

    public interface EditPhoneDialogListener{
        void changePhoneNumber(String phone);
    }

}




