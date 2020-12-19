package com.example.mystore_1_0.Prodotto;

import com.example.mystore_1_0.Orientamento;

public class Posizione {
    int indiceRiga, indiceColonna, lunghezza;
    Orientamento orientamento;

    public Posizione(int indiceRiga, int indiceColonna, int lunghezza, String orientamento) {
        this.indiceRiga = indiceRiga;
        this.indiceColonna = indiceColonna;
        this.lunghezza = lunghezza;
        this.orientamento = stringToOrientamento(orientamento);
    }

    public Posizione(int indiceRiga, int indiceColonna) {
        this.indiceRiga = indiceRiga;
        this.indiceColonna = indiceColonna;
        this.lunghezza = 1;
        this.orientamento = Orientamento.orizzontale;
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

    public Orientamento stringToOrientamento(String stringa){
        Orientamento orientamento = null;
        if(stringa.equals("orizzontale")){
            orientamento = Orientamento.orizzontale;
        }else if(stringa.equals("verticale")){
            orientamento = Orientamento.verticale;
        }
        return orientamento;
    }

    public String orientamentoToString(Orientamento orientamento){
        String stringa = null;
        if(orientamento == Orientamento.orizzontale) stringa = "orizzontale";
        else if (orientamento == Orientamento.verticale) stringa = "verticale";
        return stringa;
    }


}

