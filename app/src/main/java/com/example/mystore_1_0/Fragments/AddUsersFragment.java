package com.example.mystore_1_0.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mystore_1_0.ProfileActivity;
import com.example.mystore_1_0.R;

import java.util.ArrayList;

public class AddUsersFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addusers, container, false);
        Toast.makeText(getActivity(), "Aggiungi Dipendenti", Toast.LENGTH_SHORT).show();

       // val items = listOf("Material", "Design", "Components", "Android")
       // val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)

        String [] tipopermessi = new String[] {"1","2","3"};


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.list_item, tipopermessi);

        AutoCompleteTextView editTextFilledExposedDropdown = view.findViewById(R.id.text_permessi);
        editTextFilledExposedDropdown.setAdapter(adapter);

        return view;
    }
}
