package br.guessing.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class FaseCompletaScreen implements Screen {

    private MainGame game;
    private Stage stage;
    private int acertos;
    private int total;
    private int proximaFase;

    private Texture backgroundTexture;

    public FaseCompletaScreen(MainGame game, int acertos, int total, int proximaFase) {
        this.game = game;
        this.acertos = acertos;
        this.total = total;
        this.proximaFase = proximaFase;

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));


        backgroundTexture = new Texture(Gdx.files.internal("background.png"));
        Image background = new Image(backgroundTexture);
        background.setFillParent(true);
        stage.addActor(background);


        Table table = new Table();
        table.setFillParent(true);
        table.center();


        Label titulo = new Label("🎉 Fase Completa! 🎉", skin);
        titulo.setFontScale(1.5f);


        Label resultado = new Label("Você acertou " + acertos + " de " + total + " perguntas.", skin);
        resultado.setFontScale(1.2f);


        TextButton proximaButton = new TextButton("Próxima Fase", skin);
        proximaButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((GameFacade) game.getFacade()).trocarParaProximaFase(acertos, total);
            }
        });










        table.add(titulo).padBottom(20).row();
        table.add(resultado).padBottom(30).row();
        table.add(proximaButton).width(220).height(60);


        stage.addActor(table);
    }

    @Override
    public void show() {}

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
        backgroundTexture.dispose();
    }
}







