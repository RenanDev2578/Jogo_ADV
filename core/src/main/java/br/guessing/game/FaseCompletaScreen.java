package br.guessing.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import sun.awt.www.content.image.png;

public class FaseCompletaScreen implements Screen {
    private final MainGame game;
    private final int acertos;
    private final int total;
    private final int proximaFase;
    private Stage stage;
    private Skin skin;
    private Texture backgroundTexture;
    private Texture completeTexture;
    private Image  backgroundImage;
    private Image completeImage;

    public FaseCompletaScreen(MainGame game, int acertos, int total, int proximaFase) {
        this.game = game;
        this.acertos = acertos;
        this.total = total;
        this.proximaFase = proximaFase;


        backgroundTexture = new Texture(Gdx.files.internal("background.png"));
        completeTexture = new Texture(Gdx.files.internal("complete_badge.png"));
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));



        BitmapFont font = new BitmapFont(Gdx.files.internal("font.fnt"));
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, null);


        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setFillParent(true);
        completeImage = new Image(completeTexture);
        completeImage.setSize(200, 200);

        Table table = new Table();
        table.setFillParent(true);

        Label tituloLabel = new Label("FASE " + (proximaFase-1) + " COMPLETA!", labelStyle);
        tituloLabel.setFontScale(1.5f);
        tituloLabel.setAlignment(Align.center);

        Label resultadoLabel = new Label("Você acertou: " + acertos + " de " + total, labelStyle);
        resultadoLabel.setFontScale(1.2f);

        TextButton continuarButton = new TextButton("CONTINUAR PARA FASE " + proximaFase, skin);
        continuarButton.getLabel().setFontScale(1.2f);


        continuarButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GuessingGameScreen(game, game.getJogador(), new Advinha(), proximaFase));
            }
        });


        stage.addActor(backgroundImage);
        stage.addActor(table);


        table.add(tituloLabel).padBottom(30).row();
        table.add(completeImage).size(200).padBottom(20).row();
        table.add(resultadoLabel).padBottom(40).row();
        table.add(continuarButton).width(400).height(60).padBottom(10);


        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    Gdx.app.postRunnable(() -> continuarButton.setColor(1, 0.8f, 0, 1));
                    Thread.sleep(1000);
                    Gdx.app.postRunnable(() -> continuarButton.setColor(1, 1, 1, 1));
                } catch (InterruptedException e) {
                    break;
                }
            }
        }).start();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        backgroundImage.setSize(width, height);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        backgroundTexture.dispose();
        completeTexture.dispose();
    }
}




