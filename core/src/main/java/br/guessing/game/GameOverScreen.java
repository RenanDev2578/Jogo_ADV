package br.guessing.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameOverScreen implements Screen {
    private final Game game;
    private final int acertos;
    private final int total;
    private final Jogador jogador;
    private Stage stage;
    private Skin skin;
    private boolean isFinal;

    public GameOverScreen(Game game, int acertos, int total, Jogador jogador) {
        this.game = game;
        this.acertos = acertos;
        this.total = total;
        this.jogador = jogador;
        this.isFinal = (total == 18);
   }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        if(game instanceof GuessMaster) {
            ((GuessMaster) game).getFacade().getBackgroundMusic().setVolume(0.2f);
        }

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        String mensagem = isFinal ?
            "Fim do jogo! Você acertou " + acertos + " de " + total :
            "Fim da fase! Você acertou " + acertos + " de " + total;

        Label resultado = new Label(mensagem, skin);
        Label nomeJogador = new Label("Jogador: " + jogador.getNome(), skin);

        Image avatarImage = new Image(jogador.getAvatarTexture());
        avatarImage.setSize(100, 100); // Tamanho do avatar na tela

        TextButton jogarNovamente = new TextButton("Jogar Novamente", skin);
        jogarNovamente.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GuessingGameScreen((GuessMaster) game, ((GuessMaster) game).getFacade(), jogador, new Advinha(), 1));
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
        table.add(nomeJogador).pad(10).row();
        table.add(avatarImage).size(100, 100).pad(10).row();
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





