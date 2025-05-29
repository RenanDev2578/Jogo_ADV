package br.guessing.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.Arrays;

public class CharacterSelectionScreen implements Screen {
    private final Game game;
    private final GameFacade gameFacade;
    private final Stage stage;
    private final Skin skin;
    private final TextField nameField;
    private final Image avatarImage;
    private final Image avatarImageTop;
    private final Texture[] avatarTextures;
    private final String[] avatarNames;
    private int currentAvatarIndex = 0;
    private final Label messageLabel;

    public CharacterSelectionScreen(Game game, GameFacade gameFacade) {
        this.game = game;
        this.gameFacade = gameFacade;
        this.stage = new Stage(new ScreenViewport());
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        Gdx.input.setInputProcessor(stage);


        Texture backgroundTexture = new Texture(Gdx.files.internal("telaAV.png"));
        Image background = new Image(backgroundTexture);
        background.setFillParent(true);
        stage.addActor(background);

        FileHandle file = Gdx.files.internal("avatars/avatar_list.txt");
        avatarNames = Arrays.stream(file.readString().split("\\r?\\n"))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .toArray(String[]::new);

        if (avatarNames.length == 0) {
            throw new IllegalStateException("Nenhum avatar foi encontrado em avatar_list.txt.");
        }


        avatarTextures = new Texture[avatarNames.length];
        for (int i = 0; i < avatarNames.length; i++) {
            avatarTextures[i] = new Texture(Gdx.files.internal("avatars/" + avatarNames[i]));
        }

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label label = new Label("Digite seu nome:", skin);
        nameField = new TextField("", skin);
        nameField.setMessageText("Seu nome aqui");

        avatarImage = new Image(new TextureRegionDrawable(new TextureRegion(avatarTextures[currentAvatarIndex])));
        avatarImageTop = new Image(new TextureRegionDrawable(new TextureRegion(avatarTextures[currentAvatarIndex])));
        avatarImageTop.setSize(64, 64);
        stage.addActor(avatarImageTop);

        TextButton previousButton = new TextButton("<", skin);
        TextButton nextButton = new TextButton(">", skin);
        TextButton confirmButton = new TextButton("Confirmar", skin);

        previousButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentAvatarIndex = (currentAvatarIndex - 1 + avatarTextures.length) % avatarTextures.length;
                updateAvatar();
            }
        });

        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                currentAvatarIndex = (currentAvatarIndex + 1) % avatarTextures.length;
                updateAvatar();
            }
        });

        confirmButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String nome = nameField.getText().trim();
                if (nome.isEmpty()) {
                    showMessage("Por favor, digite um nome.");
                    return;
                }
                String avatarSelecionado = avatarNames[currentAvatarIndex];
                Jogador jogador = new Jogador(nome, avatarSelecionado);
                gameFacade.iniciarFaseComJogador(jogador);
            }
        });

        messageLabel = new Label("", skin);
        messageLabel.setColor(1, 0, 0, 1);
        messageLabel.setWrap(true);

        // Layout
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
        table.row();
        table.add(messageLabel).width(300).pad(10);
    }

    private void updateAvatar() {
        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(avatarTextures[currentAvatarIndex]));
        avatarImage.setDrawable(drawable);
        avatarImageTop.setDrawable(drawable);
    }

    private void showMessage(String message) {
        messageLabel.setText(message);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        avatarImageTop.setPosition(
            stage.getViewport().getWorldWidth() - avatarImageTop.getWidth() - 10,
            stage.getViewport().getWorldHeight() - avatarImageTop.getHeight() - 10
        );
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
        for (Texture texture : avatarTextures) {
            texture.dispose();
        }
        skin.dispose();
    }
}


