package com.example.mystore_1_0.Fragments;

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
import androidx.fragment.app.Fragment;

import com.example.mystore_1_0.Prodotto.Posizione;
import com.example.mystore_1_0.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddProductFragment extends Fragment {

    private final int NumeroColonne = 33;

    private int getIndex(int x, int y){
        int indice = ((NumeroColonne)*x)+y;
        return indice;
    }

    private Posizione getPosition(int index){
        int x = 0;
        while(index >= NumeroColonne){
            index = index - NumeroColonne;
            x = x + 1;
        }
        Posizione posizione = new Posizione(x, index);
        return posizione;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addproduct, container, false);

        TextInputLayout text_posizione = view.findViewById(R.id.text_position);

        GridLayout grid = (GridLayout) view.findViewById(R.id.gridProduct);
        int childCount = grid.getChildCount();


        for (int i = 0; i < childCount; i++){

            final int finalI = i;
                grid.getChildAt(i).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    // your click code here

                    text_posizione.getEditText().setText(getPosition(finalI).getIndiceRiga()+", "+getPosition(finalI).getIndiceColonna());

                }
            });
        }



        return view;
    }
}
