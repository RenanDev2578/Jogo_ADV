package br.guessing.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class VictoryScreen extends BaseScreen {
    private int acertosTotais;
    private Skin skin;
    private Music victoryMusic;

    public VictoryScreen(GuessMaster game, Jogador jogador, int acertosTotais) {
        super(game, jogador);
        this.acertosTotais = acertosTotais;
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
        Label scoreLabel = new Label("Acertos totais: " + acertosTotais, skin);

        table.add(congratsLabel).padBottom(20).row();
        table.add(scoreLabel);

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
        if (skin != null) {
            skin.dispose();
        }

        if(victoryMusic != null) {
            victoryMusic.dispose();
        }
    }
}



