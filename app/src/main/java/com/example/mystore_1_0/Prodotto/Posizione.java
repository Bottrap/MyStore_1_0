package com.example.mystore_1_0.Prodotto;

public class Posizione {
    int indiceRiga, indiceColonna, lunghezza;
    Orientamento orientamento;

    public Posizione(int indiceRiga, int indiceColonna, int lunghezza, Orientamento orientamento) {
        this.indiceRiga = indiceRiga;
        this.indiceColonna = indiceColonna;
        this.lunghezza = lunghezza;
        this.orientamento = orientamento;
    }

    public int getIndiceRiga() {
        return indiceRiga;
    }

    public void setIndiceRiga(int indiceRiga) {
        this.indiceRiga = indiceRiga;
    }

    public int getIndiceColonna() {
        return indiceColonna;
    }

    public void setIndiceColonna(int indiceColonna) {
        this.indiceColonna = indiceColonna;
    }

    public int getLunghezza() {
        return lunghezza;
    }

    public void setLunghezza(int lunghezza) {
        this.lunghezza = lunghezza;
    }

    public Orientamento getOrientamento() {
        return orientamento;
    }

    public void setOrientamento(Orientamento orientamento) {
        this.orientamento = orientamento;
    }
}

enum Orientamento { verticale, orizzontale }