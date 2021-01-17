package com.example.mystore_1_0.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.mystore_1_0.Fragments.Esposizione.ManageProductFragment;
import com.example.mystore_1_0.Fragments.Esposizione.MoveProductFragment;
import com.example.mystore_1_0.Fragments.Esposizione.ShowProductFragment;
import com.example.mystore_1_0.Fragments.Magazzino.AddStorageProductFragment;
import com.example.mystore_1_0.Fragments.Magazzino.ManageStorageProductFragment;
import com.example.mystore_1_0.Fragments.Magazzino.ShowStorageProductFragment;
import com.example.mystore_1_0.R;

public class LoadingFragment extends Fragment {

    private int frag;
    public LoadingFragment(int frag){
        this.frag = frag;
    }
    private static int DELAY = 350;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loading, container, false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                switch (frag) {
                    case 1:
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddStorageProductFragment()).commit();
                        break;
                    case 2:

                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ManageProductFragment()).commit();
                        break;
                    case 3:

                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ManageStorageProductFragment()).commit();
                        break;
                    case 4:

                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MoveProductFragment()).commit();
                        break;
                    case 5:
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShowProductFragment()).commit();
                        break;
                    case 6:
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShowStorageProductFragment()).commit();
                        break;
                }
            }
        },DELAY);



        return view;
    }
}
