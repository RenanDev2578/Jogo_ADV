package br.guessing.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class CharacterSelectionScreen implements Screen {
    private Game game;
    private Stage stage;
    private Skin skin;
    private TextField nameField;
    private Image avatarImage;
    private Texture[] avatarTextures;
    private int currentAvatarIndex = 0;

    public CharacterSelectionScreen(Game game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin/uiskin.json"));


        avatarTextures = new Texture[] {
            new Texture(Gdx.files.internal("avatars/avatar1.png")),
            new Texture(Gdx.files.internal("avatars/avatar2.png")),
            new Texture(Gdx.files.internal("avatars/avatar3.png"))
        };

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label label = new Label("Digite seu nome:", skin);
        nameField = new TextField("", skin);
        nameField.setMessageText("Seu nome aqui");

        avatarImage = new Image(new TextureRegionDrawable(new TextureRegion(avatarTextures[currentAvatarIndex])));


        TextButton previousButton = new TextButton("<", skin);
        TextButton nextButton = new TextButton(">", skin);

        previousButton.addListener(event -> {
            if (event.toString().equals("touchDown")) {
                currentAvatarIndex = (currentAvatarIndex - 1 + avatarTextures.length) % avatarTextures.length;
                updateAvatar();
            }
            return false;
        });

        nextButton.addListener(event -> {
            if (event.toString().equals("touchDown")) {
                currentAvatarIndex = (currentAvatarIndex + 1) % avatarTextures.length;
                updateAvatar();
            }
            return false;
        });

        TextButton confirmButton = new TextButton("Confirmar", skin);
        confirmButton.addListener(event -> {
            if (event.toString().equals("touchDown")) {
                String nome = nameField.getText();
                System.out.println("Nome confirmado: " + nome);
                System.out.println("Avatar selecionado: avatar" + (currentAvatarIndex + 1) + ".png");


            }
            return false;
        });

        table.add(label).pad(10);
        table.row();
        table.add(nameField).width(300).pad(10);
        table.row();

        Table avatarRow = new Table();
        avatarRow.add(previousButton).padRight(10);
        avatarRow.add(avatarImage).size(128);
        avatarRow.add(nextButton).padLeft(10);

        table.add(avatarRow).pad(20);
        table.row();
        table.add(confirmButton).pad(10);
    }

    private void updateAvatar() {
        avatarImage.setDrawable(new TextureRegionDrawable(new TextureRegion(avatarTextures[currentAvatarIndex])));
    }

    @Override public void show() {}
    @Override public void render(float delta) {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.15f, 1);
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

    @Override
    public void dispose() {
        stage.dispose();
        for (Texture texture : avatarTextures) {
            texture.dispose();
        }
        skin.dispose();
    }
}
