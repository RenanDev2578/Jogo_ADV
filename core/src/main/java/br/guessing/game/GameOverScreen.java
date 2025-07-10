package br.guessing.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
    private final boolean isFinal;

    private Stage stage;
    private Skin skin;
    private Texture backgroundTexture;
    private BitmapFont fontGrande;

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


        if (game instanceof GuessMaster) {
            ((GuessMaster) game).getFacade().getBackgroundMusic().setVolume(0.2f);
        }


        backgroundTexture = new Texture(Gdx.files.internal("GameOver.png"));
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);


        Image avatarImage = new Image(jogador.getAvatarTexture());
        avatarImage.setSize(80, 80);
        avatarImage.setPosition(Gdx.graphics.getWidth() - 90, Gdx.graphics.getHeight() - 90);
        stage.addActor(avatarImage);

        fontGrande = new BitmapFont();
        fontGrande.getData().setScale(2f);

        Label.LabelStyle labelStyle = new Label.LabelStyle(fontGrande, Color.WHITE);
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle(skin.get(TextButton.TextButtonStyle.class));
        buttonStyle.font = fontGrande;
        buttonStyle.fontColor = Color.YELLOW;


        String mensagem = isFinal ?
            "Fim do jogo! Acertos: " + acertos + " de " + total :
            "Fim da fase! Acertos: " + acertos + " de " + total;

        Label resultado = new Label(mensagem, labelStyle);
        Label nomeJogador = new Label("Jogador: " + jogador.getNome(), labelStyle);

        TextButton voltarInicio = new TextButton("Voltar para o Início", buttonStyle);
        voltarInicio.pad(20);
        voltarInicio.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((GuessMaster) game).getFacade().iniciarJogo();
            }
        });


        Table table = new Table();
        table.setFillParent(true);
        table.bottom().padBottom(50);
        stage.addActor(table);

        table.add(resultado).pad(10).row();
        table.add(nomeJogador).pad(5).row();
        table.add(voltarInicio).pad(15);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(delta, 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        backgroundTexture.dispose();
        fontGrande.dispose();
    }
}
