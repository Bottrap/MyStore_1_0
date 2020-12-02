package com.example.mystore_1_0.Fragments;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.mystore_1_0.R;




import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QrGeneratorFragment extends Fragment {

    Bitmap bitmap, bitmapsave;
    Button btn_qr_gen;
    Button btn_save;
    ImageView qr_img, qr_img2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qrgenerator, container, false);

        btn_qr_gen = view.findViewById(R.id.btn_qr_gen);
        btn_save = view.findViewById(R.id.btn_save);
        qr_img = view.findViewById(R.id.qr_img);
        qr_img2 = view.findViewById(R.id.qr_img2);

        btn_qr_gen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String store = "store1"; //store in cui far accedere i clienti
                QRGEncoder qrgEncoderShow = new QRGEncoder(store, QRGContents.Type.TEXT, 700);
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
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

                String store = "store1";
                QRGEncoder qrgEncoderSave = new QRGEncoder(store, QRGContents.Type.TEXT, 1000);
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
            }
        });

        return view;
    }
}
