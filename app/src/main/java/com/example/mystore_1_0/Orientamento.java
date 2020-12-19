package com.example.mystore_1_0;

public enum Orientamento {
     verticale, orizzontale;

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


