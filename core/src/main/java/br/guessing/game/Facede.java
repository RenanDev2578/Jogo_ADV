package br.guessing.game;


public class Facede {
    private Jogo jogo;

    public Facede(String nomeJogador) {
        Jogador jogador = new Jogador(nomeJogador);
        Advinha advinha = new Advinha();
        this.jogo = new Jogo(advinha, jogador);
    }

    public void iniciar() {
        jogo.iniciarJogo();
    }
}
