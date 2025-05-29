package br.guessing.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public abstract class BaseScreen extends ScreenAdapter {
    protected final GuessMaster game;
    protected final GameFacade facade;
    protected final Stage stage;
    protected final Skin skin;
    protected final BitmapFont kenneyFont;

    private Image avatarImage;

    public BaseScreen(GuessMaster game, Jogador jogador) {
        this.game = game;
        this.facade = game.getFacade();
        this.stage = new Stage(new ScreenViewport());


        kenneyFont = new BitmapFont(Gdx.files.internal("kenney.fnt"));
        skin = new Skin(Gdx.files.internal("uiskin.json"));


        if (jogador != null) {
            Texture avatar = jogador.getAvatarTexture();
            avatarImage = new Image(avatar);
            avatarImage.setSize(64, 64);
            avatarImage.setPosition(10, stage.getViewport().getWorldHeight() - avatarImage.getHeight() - 10);
            stage.addActor(avatarImage);
        }
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        if (avatarImage != null) {
            avatarImage.setPosition(10, stage.getViewport().getWorldHeight() - avatarImage.getHeight() - 10);
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        kenneyFont.dispose();
    }
}
