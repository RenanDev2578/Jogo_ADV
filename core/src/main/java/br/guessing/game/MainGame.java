package br.guessing.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.Batch;

public class MainGame extends Game {
    public Batch batch;
    private Jogador jogador;
    private int faseAtual = 1;

    @Override
    public void create() {
        jogador = new Jogador("Jogador");
        setScreen(new GuessingGameScreen(this, jogador, new Advinha(), faseAtual));
    }

    public Jogador getJogador() {
        return jogador;
    }
    public void mostrarFaseCompleta(int acertos, int total, int proximaFase) {
        setScreen(new FaseCompletaScreen(this, acertos, total, proximaFase));
    }

    public void trocarParaProximaFase(int acertosDaFase, int totalNaFase) {
        jogador.adicionarAcertos(acertosDaFase);
        faseAtual++;
        if (faseAtual <= 6) {
            setScreen(new GuessingGameScreen(this, jogador, new Advinha(), faseAtual));
        } else {
            setScreen(new GameOverScreen(this, jogador.getAcertosTotais(), 18));
        }
    }

    public void trocarParaGameOver(int acertosDaFase, int totalNaFase) {

        setScreen(new GameOverScreen(this, acertosDaFase, totalNaFase));
    }
    public int getFaseAtual() {
        return faseAtual;

    }
}







