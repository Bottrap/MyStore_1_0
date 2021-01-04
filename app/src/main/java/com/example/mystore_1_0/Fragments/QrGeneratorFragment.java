package com.example.mystore_1_0.Fragments;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.mystore_1_0.IOnBackPressed;
import com.example.mystore_1_0.R;
import com.example.mystore_1_0.Utente;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QrGeneratorFragment extends Fragment implements IOnBackPressed {

    Bitmap bitmap, bitmapsave;
    Button btn_qr_gen;
    Button btn_save;
    ImageView qr_img, qr_img2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qrgenerator, container, false);

        Utente utenteLoggato = getActivity().getIntent().getParcelableExtra("utente");

        btn_qr_gen = view.findViewById(R.id.btn_qr_gen);
        btn_save = view.findViewById(R.id.btn_save);
        qr_img = view.findViewById(R.id.qr_img);
        qr_img2 = view.findViewById(R.id.qr_img2);

        String store = utenteLoggato.getNegozio(); //store in cui far accedere i clienti
        final String[] encodedStore = new String[1];

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("StoresEncoding");
        Query query = reference.orderByChild("nome").equalTo(store);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    encodedStore[0] = ds.child("codice").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_qr_gen.setOnClickListener(v -> {
            QRGEncoder qrgEncoderShow = new QRGEncoder(encodedStore[0], QRGContents.Type.TEXT, 700);
            qrgEncoderShow.setColorWhite(Color.TRANSPARENT);
            try {
                // Getting QR-Code as Bitmap
                bitmap = qrgEncoderShow.getBitmap();
                // Setting Bitmap to ImageView
                qr_img.setImageBitmap(bitmap);
                btn_save.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        btn_save.setOnClickListener(v -> {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            QRGEncoder qrgEncoderSave = new QRGEncoder(encodedStore[0], QRGContents.Type.TEXT, 1000);
            try {
                // Getting QR-Code as Bitmap
                bitmap = qrgEncoderSave.getBitmap();
                qr_img2.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            bitmapsave = ((BitmapDrawable) qr_img2.getDrawable()).getBitmap();
            String filename = String.format("%d.png", System.currentTimeMillis());
            // Salvo l'immagine nella galleria
            MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmapsave, "QR Code", null);
            Toast.makeText(getActivity(), "Immagine salvata", Toast.LENGTH_LONG).show();
        });
        return view;
    }

    @Override
    public boolean onBackPressed() {
        AppCompatActivity activity = (AppCompatActivity) getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DashboardFragment()).commit();
        return true;
    }
}

