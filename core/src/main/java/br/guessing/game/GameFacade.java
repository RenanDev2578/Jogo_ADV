package br.guessing.game;


import com.badlogic.gdx.Screen;

public class GameFacade {
    private MainGame game;
    private Jogador jogador;
    private int faseAtual = 1;

    public GameFacade(MainGame game) {
        this.game = game;
        this.jogador = new Jogador("Jogador");
    }

    public void iniciarJogo() {
        faseAtual = faseAtual;
        game.setScreen(new GuessingGameScreen(game, this, jogador, new Advinha(), faseAtual));
    }

    public void mostrarFaseCompleta(int acertos, int total, int proximaFase) {
        game.setScreen(new FaseCompletaScreen(game, acertos, total, proximaFase));
    }

    public void trocarParaProximaFase(int acertosDaFase, int totalNaFase) {
        jogador.adicionarAcertos(acertosDaFase);
        faseAtual++;
        if (faseAtual <= new Advinha().getTotalFases()) {
            game.setScreen(new GuessingGameScreen(game, this, jogador, new Advinha(), faseAtual));
        } else {

            game.setScreen((Screen) new VictoryScreen(game, jogador.getAcertosTotais(), 18));
        }
    }

    public void trocarParaGameOver(int acertosDaFase, int totalNaFase) {
        game.setScreen(new GameOverScreen(game, acertosDaFase, totalNaFase));
    }

    public Jogador getJogador() {
        return jogador;

    }
    public void trocarParaVictory() {
        game.setScreen(new VictoryScreen(game, jogador.getAcertosTotais(), 0));
    }



}

