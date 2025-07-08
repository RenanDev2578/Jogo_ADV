package br.guessing.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class VictoryScreen extends BaseScreen {
    private final Jogador jogador;
    private final int pontuacaoFinal;
    private Skin skin;
    private Music victoryMusic;

    public VictoryScreen(GuessMaster game, Jogador jogador, int pontuacaoFinal) {
        super(game, jogador);
        this.jogador = jogador;
        this.pontuacaoFinal = pontuacaoFinal;
    }

    @Override
    public void show() {
        super.show();

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(120);
        stage.addActor(table);

        Label congratsLabel = new Label("Parabéns! Você venceu!", skin);
        Label scoreLabel = new Label("Pontuação final: " + pontuacaoFinal + " pts", skin);

        Label rankingTitle = new Label("Ranking (simulado)", skin);
        Label ranking1 = new Label("1. Jogador A - 300 pts", skin);
        Label ranking2 = new Label("2. Jogador B - 250 pts", skin);

        table.add(congratsLabel).padBottom(20).row();
        table.add(scoreLabel).padBottom(30).row();
        table.add(rankingTitle).padBottom(10).row();
        table.add(ranking1).row();
        table.add(ranking2).row();

        game.getFacade().stopMusic();
        victoryMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/victory.mp3"));
        victoryMusic.setVolume(0.5f);
        victoryMusic.play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render(delta);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (skin != null) skin.dispose();
        if (victoryMusic != null) victoryMusic.dispose();
    }
}

