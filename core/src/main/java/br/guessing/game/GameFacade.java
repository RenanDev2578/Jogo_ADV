package br.guessing.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class GameFacade {
    private GuessMaster game;
    private Jogador jogadorAtual;
    private int faseAtual = 1;
    private Music backgroundMusic;

    public GameFacade(GuessMaster game) {
        this.game = game;

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/AdhesiveWombat - Night Shade.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.3f);
        backgroundMusic.play();
    }

    public void iniciarJogo() {
        game.setScreen(new CharacterSelectionScreen(game, this));
    }

    public void iniciarFaseComJogador(Jogador jogador) {
        this.jogadorAtual = jogador;
        faseAtual = 1;
        game.setScreen(new GuessingGameScreen(game, this, jogadorAtual, new Advinha(), faseAtual));
    }

    public void mostrarFaseCompleta(int acertos, int total, int proximaFase) {

        game.setScreen(new FaseCompletaScreen(game, this, acertos, total, proximaFase));
    }

    public void trocarParaProximaFase(int acertosDaFase, int totalNaFase) {
        faseAtual++;

        if (faseAtual <= new Advinha().getTotalFases()) {
            game.setScreen(new GuessingGameScreen(game, this, jogadorAtual, new Advinha(), faseAtual));
        } else {

            game.setScreen(new VictoryScreen(game, jogadorAtual, jogadorAtual.getAcertosTotais()));
        }
    }

    public void trocarParaGameOver(int acertosDaFase, int totalNaFase) {
        game.setScreen(new GameOverScreen(game, acertosDaFase, totalNaFase, jogadorAtual));
    }

    public Jogador getJogador() {
        return jogadorAtual;
    }

    public void trocarParaVictory() {
        game.setScreen(new VictoryScreen(game, jogadorAtual, jogadorAtual.getAcertosTotais()));
    }

    public void pauseMusic() {
        if(backgroundMusic != null && backgroundMusic.isPlaying()) {
            backgroundMusic.pause();
        }
    }

    public void resumeMusic() {
        if(backgroundMusic != null && backgroundMusic.isPlaying()) {
            backgroundMusic.play();
        }
    }

    public void stopMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }
    }

    public void dispose() {
        if (backgroundMusic != null) {
            backgroundMusic.dispose();
        }
    }

    public Music getBackgroundMusic() {
        return backgroundMusic;
    }
}





