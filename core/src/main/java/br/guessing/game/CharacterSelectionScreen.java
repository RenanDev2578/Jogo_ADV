package br.guessing.game;

import java.util.Arrays;
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

public class CharacterSelectionScreen implements Screen {
    private Game game;
    private Stage stage;
    private Skin skin;
    private TextField nameField;
    private Image avatarImage;
    private Texture[] avatarTextures;
    private String[] avatarNames;
    private int currentAvatarIndex = 0;

    public CharacterSelectionScreen(Game game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("uiskin/uiskin.json"));
        Gdx.input.setInputProcessor(stage);

        // Carregar nomes dos arquivos dos avatares
        FileHandle file = Gdx.files.internal("avatars/avatar_list.txt");
        avatarNames = file.readString().split("\\r?\\n");
        avatarNames = Arrays.stream(avatarNames)
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .toArray(String[]::new);

        if (avatarNames.length == 0) {
            throw new IllegalStateException("Nenhum avatar foi encontrado em avatar_list.txt.");
        }

        // Carregar texturas dos avatares
        avatarTextures = new Texture[avatarNames.length];
        for (int i = 0; i < avatarNames.length; i++) {
            try {
                avatarTextures[i] = new Texture(Gdx.files.internal("avatars/" + avatarNames[i]));
            } catch (Exception e) {
                System.err.println("Erro ao carregar avatar: " + avatarNames[i]);
                avatarTextures[i] = new Texture(Gdx.files.internal("avatars/default.png")); // fallback
            }
        }

        // UI
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label label = new Label("Digite seu nome:", skin);
        nameField = new TextField("", skin);
        nameField.setMessageText("Seu nome aqui");

        avatarImage = new Image(new TextureRegionDrawable(new TextureRegion(avatarTextures[currentAvatarIndex])));

        TextButton previousButton = new TextButton("<", skin);
        TextButton nextButton = new TextButton(">", skin);

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

        TextButton confirmButton = new TextButton("Confirmar", skin);
        confirmButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String nome = nameField.getText();
                System.out.println("Nome confirmado: " + nome);
                System.out.println("Avatar selecionado: " + avatarNames[currentAvatarIndex]);

                // Proxima tela poderia ser chamada aqui
                // game.setScreen(new ProximaTela(game, nome, avatarNames[currentAvatarIndex]));
            }
        });

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
    }

    private void updateAvatar() {
        avatarImage.setDrawable(new TextureRegionDrawable(new TextureRegion(avatarTextures[currentAvatarIndex])));
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
