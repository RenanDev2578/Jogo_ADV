package br.guessing.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameOverScreen implements Screen {
    private final Game game;
    private final int acertos;
    private final int total;
    private Stage stage;
    private Skin skin;
    private boolean isFinal;

    public GameOverScreen(Game game, int acertos, int total) {
        this.game = game;
        this.acertos = acertos;
        this.total = total;
        this.isFinal = (total == 3);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        String mensagem = isFinal ?
            "Fim do jogo! Você acertou " + acertos + " de " + total :
            "Fim da fase! Você acertou " + acertos + " de " + total;

        Label resultado = new Label(mensagem, skin);
        TextButton jogarNovamente = new TextButton("Jogar Novamente", skin);
        jogarNovamente.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Jogador novoJogador = new Jogador("Jogador");
                game.setScreen(new GuessingGameScreen((MainGame) game, novoJogador, new Advinha(), 1));
            }
        });

        TextButton sair = new TextButton("Sair", skin);
        sair.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        table.add(resultado).pad(10).row();
        table.add(jogarNovamente).pad(10).row();
        table.add(sair).pad(10);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}





