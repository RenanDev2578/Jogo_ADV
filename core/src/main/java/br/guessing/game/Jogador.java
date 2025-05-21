package br.guessing.game;

public class Jogador {
    private String nome;
    private int acertosTotais;

    public Jogador(String nome) {
        this.nome = nome;
        this.acertosTotais = 0;
    }

    public String getNome() {
        return nome;
    }

    public int getAcertosTotais() {
        return acertosTotais;
    }

    public void adicionarAcertos(int quantidade) {
        this.acertosTotais += quantidade;
    }

    /*public void resetarAcertos() {
        this.acertosTotais = 0;
    }*/

}





