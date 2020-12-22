package com.example.mystore_1_0.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.mystore_1_0.Fragments.Profile.ProfileFragment;
import com.example.mystore_1_0.Fragments.ShowUsers.ShowUsersFragment;
import com.example.mystore_1_0.R;

public class DashboardFragment extends Fragment {

    CardView addUserCV, showUsersCV, qrScanCV, profileCV, logoutCV, addProdCV, showProdCV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        addUserCV = view.findViewById(R.id.adduser_cardview);
        showUsersCV = view.findViewById(R.id.showusers_cardview);
        qrScanCV = view.findViewById(R.id.qrGen_cardView);
        profileCV = view.findViewById(R.id.profile_cardView);
        //logoutCV = view.findViewById(R.id.logout_cardView);
        addProdCV = view.findViewById(R.id.add_prod_cardView);
        showProdCV = view.findViewById(R.id.list_prod_cardView);

        addUserCV.setOnClickListener(v -> {
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddUsersFragment()).commit();
        });

        showUsersCV.setOnClickListener(v -> {
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShowUsersFragment()).commit();
        });

        addProdCV.setOnClickListener(v -> {
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddProductFragment()).commit();
        });

        qrScanCV.setOnClickListener(v -> {
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new QrGeneratorFragment()).commit();
        });

        profileCV.setOnClickListener(v -> {
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
        });

        showProdCV.setOnClickListener(v -> {
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShowProductFragment()).commit();
        });
/*
        logoutCV.setOnClickListener(v -> {
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            activity.finish();
        });

 */
        return view;
    }
}
