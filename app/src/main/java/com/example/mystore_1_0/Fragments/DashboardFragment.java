package com.example.mystore_1_0.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.mystore_1_0.Fragments.Esposizione.ManageProductFragment;
import com.example.mystore_1_0.Fragments.Esposizione.ShowProductFragment;
import com.example.mystore_1_0.Fragments.Magazzino.AddStorageProductFragment;
import com.example.mystore_1_0.Fragments.Magazzino.ManageStorageProductFragment;
import com.example.mystore_1_0.Fragments.Profile.ProfileFragment;
import com.example.mystore_1_0.Fragments.ShowUsers.ShowUsersFragment;
import com.example.mystore_1_0.R;
import com.example.mystore_1_0.Utente;

public class DashboardFragment extends Fragment {

    CardView addUserCV, showUsersCV, qrScanCV, profileCV, logoutCV, addProdCV, showProdCV, addStorageCV, listStorageCV_MAG, listStorageCV, listProdCV;
    TextView text_Add_storage, text_add_prod, text_manage_prod;
    RelativeLayout relativ;
    LinearLayout linearLayout1, linearLayout2, linearLayout3, linearLayoutDIPENDENTE;
    ConstraintLayout constraintLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        Utente utenteLoggato = getActivity().getIntent().getParcelableExtra("utente");

        addUserCV = view.findViewById(R.id.adduser_cardview);
        showUsersCV = view.findViewById(R.id.showusers_cardview);
        qrScanCV = view.findViewById(R.id.qrGen_cardView);
        profileCV = view.findViewById(R.id.profile_cardView);
        logoutCV = view.findViewById(R.id.logout_cardView);
        addProdCV = view.findViewById(R.id.add_prod_cardView);
        showProdCV = view.findViewById(R.id.list_prod_cardView);
        relativ = view.findViewById(R.id.relative_progress);
        listStorageCV_MAG = view.findViewById(R.id.list_storage_cardView_MAG);
        text_Add_storage = view.findViewById(R.id.text_add_storage);
        listStorageCV = view.findViewById(R.id.list_storage_cardView);
        text_add_prod = view.findViewById(R.id.text_add_prod);
        text_manage_prod = view.findViewById(R.id.text_manage_prod);
        showProdCV = view.findViewById(R.id.show_prod_cardView);
        listProdCV = view.findViewById(R.id.list_prod_cardView);
        addStorageCV = view.findViewById(R.id.add_storage_cardview);

        linearLayout1 = view.findViewById(R.id.linearLayout1);
        linearLayout2 = view.findViewById(R.id.linearLayout2);
        linearLayout3 = view.findViewById(R.id.linearLayout3);
        linearLayoutDIPENDENTE = view.findViewById(R.id.linearLayoutDIPENDENTE);

        // CONTROLLO SUI PERMESSI DELL'UTENTE
        switch (Integer.parseInt(utenteLoggato.getPermessi())) {
            case 1: // CAPO SUPREMO
                //linearLayout1.setVisibility(View.GONE);
                //showUsersCV.setVisibility(View.GONE);
                listStorageCV_MAG.setVisibility(View.GONE);
                showProdCV.setVisibility(View.GONE);
                break;
            case 2: // MAGAZZINIERE
                addUserCV.setVisibility(View.GONE);
                showUsersCV.setVisibility(View.GONE);
                text_Add_storage.setTextSize(12);
                listStorageCV.setVisibility(View.GONE);
                addProdCV.setVisibility(View.GONE);
                listProdCV.setVisibility(View.GONE);
                qrScanCV.setVisibility(View.GONE);
                break;
            case 3: // DIPENDENTE
                linearLayout1.setVisibility(View.GONE);
                listStorageCV.setVisibility(View.GONE);
                showProdCV.setVisibility(View.GONE);
                text_add_prod.setTextSize(12);
                text_manage_prod.setTextSize(12);
                linearLayoutDIPENDENTE.setVisibility(View.VISIBLE);
                qrScanCV.setVisibility(View.GONE);
                break;
        }

        addUserCV.setOnClickListener(v -> {
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddUsersFragment()).commit();
        });

        showUsersCV.setOnClickListener(v -> {
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShowUsersFragment()).commit();
        });

        addProdCV.setOnClickListener(v -> {
            relativ.setVisibility(View.VISIBLE);
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddStorageProductFragment()).commit();
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
            relativ.setVisibility(View.VISIBLE);
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShowProductFragment()).commit();
        });

        listProdCV.setOnClickListener(v -> {
            relativ.setVisibility(View.VISIBLE);
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ManageProductFragment()).commit();
        });

        listStorageCV.setOnClickListener(v -> {
            relativ.setVisibility(View.VISIBLE);
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ManageStorageProductFragment()).commit();
        });
        listStorageCV_MAG.setOnClickListener(v -> {
            relativ.setVisibility(View.VISIBLE);
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ManageStorageProductFragment()).commit();
        });
        addStorageCV.setOnClickListener(v -> {
            relativ.setVisibility(View.VISIBLE);
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ManageStorageProductFragment()).commit();
        });
        logoutCV.setOnClickListener(v -> {
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            activity.finish();
        });
        return view;
    }
}
