package com.example.mystore_1_0.Prodotto;

public class Prodotto {
    final int NumeroColonne = 33;
    String nome;
    int codice;
    float prezzo;
    Posizione posizione;

    public Prodotto(String nome, int codice, float prezzo, Posizione posizione) {
        this.nome = nome;
        this.codice = codice;
        this.prezzo = prezzo;
        this.posizione = posizione;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCodice() {
        return codice;
    }

    public void setCodice(int codice) {
        this.codice = codice;
    }

    public float getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(float prezzo) {
        this.prezzo = prezzo;
    }

    public Posizione getPosizione() {
        return posizione;
    }

    public void setPosizione(Posizione posizione) {
        this.posizione = posizione;
    }

    private int getIndex(int x, int y){
        int indice = ((NumeroColonne)*x)+y;
        return indice;
    }

    private Posizione getPosition(int index){
        int x = 0;
        while(index >= NumeroColonne){
            index = index - NumeroColonne;
            x = x + 1;
        }
        Posizione posizione = new Posizione(x, index);
        return posizione;
    }


}
