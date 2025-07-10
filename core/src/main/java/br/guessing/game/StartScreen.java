package br.guessing.game;

import br.guessing.game.GameFacade;
import br.guessing.game.GuessMaster;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class StartScreen implements Screen {
    private final GuessMaster game;
    private final GameFacade facade;
    private final Stage stage;
    private final Texture backgroundTexture;

    public StartScreen(GuessMaster game, GameFacade facade) {
        this.game = game;
        this.facade = facade;

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Carrega a textura de fundo
        backgroundTexture = new Texture(Gdx.files.internal("start.png"));
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setFillParent(true); // ocupa a tela inteira
        stage.addActor(backgroundImage); // adiciona primeiro, para ficar atrás

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table); // a tabela ficará sobre a imagem

        Label.LabelStyle titleStyle = new Label.LabelStyle(skin.get(Label.LabelStyle.class));
        titleStyle.font.getData().setScale(2.0f); // dobra o tamanho da fonte

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle(skin.get(TextButton.TextButtonStyle.class));
        buttonStyle.font.getData().setScale(1.5f);

        Label titulo = new Label("Bem-vindo ao Guessing Game", titleStyle);
        TextButton botaoJogar = new TextButton("Iniciar", buttonStyle);

        botaoJogar.addListener(event -> {
            if (event.toString().equals("touchDown")) {
                facade.trocarParaSelecaoDePersonagem();
            }
            return true;
        });

        table.add(titulo).padBottom(100).row();
        table.add(botaoJogar);
    }

    @Override public void show() {}
    @Override public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }
    @Override public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        stage.dispose();
      backgroundTexture.dispose();
    }
}
