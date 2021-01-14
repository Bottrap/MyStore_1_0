package com.example.mystore_1_0.Prodotto;


public class Prodotto {
    final static int NumeroColonne = 33;
    String nome, codice, prezzo, urlimmagine;
    int quantita;
    Posizione posizione;

    public String getURLImmagine() {
        return urlimmagine;
    }

    public void setURLImmagine(String urlimmagine) {
        this.urlimmagine = urlimmagine;
    }

    public Prodotto(String nome, String codice, String prezzo, String urlimmagine, Posizione posizione, int quantita) {
        this.nome = nome;
        this.codice = codice;
        this.prezzo = prezzo;
        this.urlimmagine = urlimmagine;
        this.posizione = posizione;
        this.quantita = quantita;
    }

    public Prodotto() {
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
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

    public int getIndex(int x, int y) {
        int indice = ((NumeroColonne) * x) + y;
        return indice;
    }




}
