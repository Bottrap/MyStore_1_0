package com.example.mystore_1_0.Fragments;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.mystore_1_0.Fragments.ShowUsers.ShowUsersFragment;
import com.example.mystore_1_0.Orientamento;
import com.example.mystore_1_0.Prodotto.Orientamento;
import com.example.mystore_1_0.Prodotto.Posizione;
import com.example.mystore_1_0.Prodotto.Prodotto;
import com.example.mystore_1_0.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddProductFragment extends Fragment {

    private final int NumeroColonne = 33;

    private int getIndex(int x, int y) {
        int indice = ((NumeroColonne) * x) + y;
        return indice;
    }

    private Posizione getPosition(int index) {
        int x = 0;
        while (index >= NumeroColonne) {
            index = index - NumeroColonne;
            x = x + 1;
        }
        Posizione posizione = new Posizione(x, index);
        return posizione;
    }

    public Boolean isClicked = false;
    public int indicePrecedente;
    public int indiceSuccessivo = 500;
    public Boolean is2Clicked = false;
    public Posizione posizione;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addproduct, container, false);

        TextInputLayout text_posizione = view.findViewById(R.id.text_position);

        GridLayout grid = (GridLayout) view.findViewById(R.id.gridProduct);
        int childCount = grid.getChildCount();


        for (int i = 0; i < childCount; i++) {

            final int finalI = i;
            grid.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // your click code here
                    if (!is2Clicked) {
                        if (!isClicked) {
                            isClicked = true;
                            text_posizione.getEditText().setText(getPosition(finalI).getIndiceRiga() + ", " + getPosition(finalI).getIndiceColonna());
                            indicePrecedente = finalI;
                            grid.getChildAt(finalI).setBackgroundResource(R.drawable.button_shape);

                        } else {
                            is2Clicked = true;
                            indiceSuccessivo = finalI;
                            if (getPosition(indicePrecedente).getIndiceRiga() == getPosition(finalI).getIndiceRiga()) {
                                if (indicePrecedente < indiceSuccessivo) {
                                    for (int j = indicePrecedente; j <= finalI; j++) {
                                        grid.getChildAt(j).setBackgroundResource(R.drawable.button_shape);
                                        text_posizione.getEditText().setText(getPosition(indicePrecedente).getIndiceRiga() + ", " + getPosition(indicePrecedente).getIndiceColonna() + " -> " + getPosition(finalI).getIndiceRiga() + ", " + getPosition(finalI).getIndiceColonna());
                                    }
                                } else {
                                    for (int j = indicePrecedente; j >= indiceSuccessivo; j--) {
                                        grid.getChildAt(j).setBackgroundResource(R.drawable.button_shape);
                                        text_posizione.getEditText().setText(getPosition(indicePrecedente).getIndiceRiga() + ", " + getPosition(indicePrecedente).getIndiceColonna() + " -> " + getPosition(finalI).getIndiceRiga() + ", " + getPosition(finalI).getIndiceColonna());
                                    }
                                }
                            }
                            if (getPosition(indicePrecedente).getIndiceColonna() == getPosition(indiceSuccessivo).getIndiceColonna()) {
                                posizione = getPosition(indicePrecedente);
                                Orientamento orientamento = Orientamento.verticale;
                                if (indicePrecedente < indiceSuccessivo) {
                                    for (int j = indicePrecedente; j <= indiceSuccessivo; j = j + 33) {
                                        grid.getChildAt(j).setBackgroundResource(R.drawable.button_shape);
                                        text_posizione.getEditText().setText(getPosition(indicePrecedente).getIndiceRiga() + ", " + getPosition(indicePrecedente).getIndiceColonna() + " -> " + getPosition(finalI).getIndiceRiga() + ", " + getPosition(finalI).getIndiceColonna());
                                    }
                                } else {
                                    for (int j = indicePrecedente; j >= indiceSuccessivo; j = j - 33) {
                                        grid.getChildAt(j).setBackgroundResource(R.drawable.button_shape);
                                        text_posizione.getEditText().setText(getPosition(indicePrecedente).getIndiceRiga() + ", " + getPosition(indicePrecedente).getIndiceColonna() + " -> " + getPosition(finalI).getIndiceRiga() + ", " + getPosition(finalI).getIndiceColonna());
                                    }
                                }
                            }

                        }

                    }
                }
            });
        }


        text_posizione.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatActivity activity = (AppCompatActivity) getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddProductFragment()).commit();

            }
        });

        Button btn_add_prod = view.findViewById(R.id.btn_add_prod);
        btn_add_prod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Prodotto prodotto = new Prodotto;
                if (isClicked){
                    if (getPosition(indicePrecedente).getIndiceRiga() == getPosition(indiceSuccessivo).getIndiceRiga()){
                        Posizione posizione = getPosition(indicePrecedente);
                        posizione.setOrientamento();
                        prodotto.
                    }
                }
            }
        });


        return view;
    }
}
