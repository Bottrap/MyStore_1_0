package com.example.mystore_1_0.Prodotto;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;


public class Prodotto {
    final int NumeroColonne = 33;
    String nome, codice, prezzo, URLImmagine;
    Posizione posizione;

    public String getURLImmagine() {
        return URLImmagine;
    }

    public void setURLImmagine(String URLImmagine) {
        this.URLImmagine = URLImmagine;
    }

    public Prodotto(String nome, String codice, String prezzo, String URLImmagine) {
        this.nome = nome;
        this.codice = codice;
        this.prezzo = prezzo;
        this.URLImmagine = URLImmagine;
    }

    public Prodotto() {
    }

    public Prodotto(String nome, String codice, String prezzo) {  //costruttore utile solo per le prove col database
        this.nome = nome;
        this.codice = codice;
        this.prezzo = prezzo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(String prezzo) {
        this.prezzo = prezzo;
    }

    public Posizione getPosizione() {
        return posizione;
    }

    public void setPosizione(Posizione posizione) {
        this.posizione = posizione;
    }

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


}
