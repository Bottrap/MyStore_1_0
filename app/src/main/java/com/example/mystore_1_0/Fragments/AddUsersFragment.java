package com.example.mystore_1_0.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mystore_1_0.ProfileActivity;
import com.example.mystore_1_0.R;

public class AddUsersFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Toast.makeText(getActivity(), "Aggiungi Dipendenti", Toast.LENGTH_SHORT).show();

        return inflater.inflate(R.layout.fragment_addusers, container, false);
    }
}
