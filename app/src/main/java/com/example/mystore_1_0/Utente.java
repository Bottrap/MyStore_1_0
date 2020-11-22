package com.example.mystore_1_0;

public class Utente {
    String id, password, permessi, nome, cognome, dataNascita, telefono;

    public Utente(){ }

    public Utente(String id, String password){
        this.id = id;
        this.password = password;
    }

    public Utente(String id, String password, String permessi, String nome, String cognome, String dataNascita, String telefono) {
        this.id = id;
        this.password = password;
        this.permessi = permessi;
        this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
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
}
