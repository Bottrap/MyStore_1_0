package com.example.mystore_1_0;

import android.os.Parcel;
import android.os.Parcelable;

public class Utente implements Parcelable {
    private String id, password, permessi, nome, cognome, dataNascita, telefono;
    private boolean expanded;

    /** Legenda permessi
     *
     * Permesso = 1 --> Capo supremo
     * Permesso = 2 --> Responsabile magazzino
     * Permesso = 3 --> Dipendente (zona esposizione)
     *
     */

    public Utente(){
        this.expanded = false;
    }

    public Utente(String id, String password){
        this.id = id;
        this.password = password;
        this.expanded = false;
    }

    public Utente(String id, String password, String permessi, String nome, String cognome, String dataNascita, String telefono) {
        this.id = id;
        this.password = password;
        this.permessi = permessi;
        this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
        this.telefono = telefono;
        this.expanded = false;
    }

    protected Utente(Parcel in) {
        id = in.readString();
        password = in.readString();
        permessi = in.readString();
        nome = in.readString();
        cognome = in.readString();
        dataNascita = in.readString();
        telefono = in.readString();
    }

    public static final Creator<Utente> CREATOR = new Creator<Utente>() {
        @Override
        public Utente createFromParcel(Parcel in) {
            return new Utente(in);
        }

        @Override
        public Utente[] newArray(int size) {
            return new Utente[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(password);
        dest.writeString(permessi);
        dest.writeString(nome);
        dest.writeString(cognome);
        dest.writeString(dataNascita);
        dest.writeString(telefono);
    }

    public String getPermessi() {
        return permessi;
    }

    public void setPermessi(String permessi) {
        this.permessi = permessi;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(String dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
