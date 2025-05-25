package br.guessing.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GuessingGameScreen implements Screen {
    private final GameFacade facade;
    private final GuessMaster game;
    private final Jogador jogador;
    private final Advinha advinha;
    private final int faseAtual;
    private int perguntaAtual = 0;
    private int acertosNaFase = 0;
    private boolean respostaRespondida = false;

    private final Stage stage;
    private final Label perguntaLabel;
    private final Label feedbackLabel;
    private final Label acertosLabel;
    private final TextButton[] botoesOpcoes = new TextButton[5];
    private Texture backgroundTexture;
    private final int totalPerguntas;
    private String[] alternativas;

    private Image avatarImage;
    private Texture avatarTexture;

    public GuessingGameScreen(GuessMaster game, GameFacade facade, Jogador jogador, Advinha advinha, int fase) {
        this.game = game;
        this.facade = facade;
        this.jogador = jogador;
        this.advinha = advinha;
        this.faseAtual = fase;
        this.totalPerguntas = advinha.getQuantidadePerguntas(faseAtual);

        game.batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Fundo
        backgroundTexture = new Texture(Gdx.files.internal("telaF1.png"));
        Image background = new Image(backgroundTexture);
        background.setFillParent(true);
        stage.addActor(background);

        BitmapFont font = new BitmapFont();
        font.getData().setScale(2.0f);
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);

        perguntaLabel = new Label("", labelStyle);
        feedbackLabel = new Label("", labelStyle);
        acertosLabel = new Label("Acertos: 0/" + totalPerguntas, labelStyle);

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.BLACK;
        buttonStyle.overFontColor = Color.DARK_GRAY;
        buttonStyle.downFontColor = Color.DARK_GRAY;
        buttonStyle.checkedFontColor = Color.BLACK;
        buttonStyle.disabledFontColor = Color.GRAY;
        buttonStyle.up = skin.getDrawable("default-round");
        buttonStyle.down = skin.getDrawable("default-round-down");

        Table table = new Table();
        table.setFillParent(true);
        table.center().padTop(50);
        stage.addActor(table);

        table.add(acertosLabel).padBottom(20).row();
        table.add(perguntaLabel).padBottom(20).row();

        for (int i = 0; i < 5; i++) {
            final int indice = i;
            botoesOpcoes[i] = new TextButton("Opção " + (char) ('A' + i), buttonStyle);
            botoesOpcoes[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (!respostaRespondida) {
                        verificarResposta(indice);
                    }
                }
            });
            table.add(botoesOpcoes[i]).width(300).height(60).pad(10).center().row();
        }

        table.add(feedbackLabel).padTop(10).row();

        // Carregar avatar do jogador
        try {
            avatarTexture = new Texture(Gdx.files.internal("avatars/" + jogador.getAvatar()));
        } catch (Exception e) {
            avatarTexture = new Texture(Gdx.files.internal("avatars/default.png"));
        }
        avatarImage = new Image(new TextureRegionDrawable(new TextureRegion(avatarTexture)));
        avatarImage.setSize(100, 100);
        avatarImage.setPosition(stage.getViewport().getWorldWidth() - avatarImage.getWidth() - 10,
            stage.getViewport().getWorldHeight() - avatarImage.getHeight() - 10);
        stage.addActor(avatarImage);

        carregarPergunta();
    }

    private void carregarPergunta() {
        if (perguntaAtual < totalPerguntas) {
            perguntaLabel.setText(advinha.getPergunta(faseAtual, perguntaAtual));
            feedbackLabel.setText("");
            feedbackLabel.setColor(1, 1, 1, 1);
            respostaRespondida = false;

            alternativas = advinha.getOpcoes(faseAtual, perguntaAtual);

            for (int i = 0; i < botoesOpcoes.length; i++) {
                botoesOpcoes[i].setText((char) ('A' + i) + ": " + alternativas[i]);
                botoesOpcoes[i].setDisabled(false);
                botoesOpcoes[i].setVisible(true);
            }
        } else {
            finalizarFase();
        }
    }

    private void verificarResposta(int indiceEscolhido) {
        respostaRespondida = true;
        String respostaCerta = advinha.getResposta(faseAtual, perguntaAtual).toLowerCase();
        String respostaJogador = alternativas[indiceEscolhido].toLowerCase();

        boolean acertou = respostaJogador.equals(respostaCerta);

        if (acertou) {
            feedbackLabel.setText("Correto! Resposta: " + respostaCerta);
            feedbackLabel.setColor(0, 1, 0, 1);
            acertosNaFase++;
            acertosLabel.setText("Acertos: " + acertosNaFase + "/" + totalPerguntas);
        } else {
            feedbackLabel.setText("Errado! Resposta correta: " + respostaCerta);
            feedbackLabel.setColor(1, 0, 0, 1);
        }

        for (TextButton btn : botoesOpcoes) {
            btn.setDisabled(true);
        }

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                perguntaAtual++;
                carregarPergunta();
            }
        }, 2);
    }

    private void finalizarFase() {
        if (acertosNaFase >= 2) {
            jogador.adicionarAcertos(acertosNaFase);
            if (faseAtual < 6) {
                facade.mostrarFaseCompleta(acertosNaFase, totalPerguntas, faseAtual + 1);
            } else {
                facade.trocarParaVictory();
            }
        } else {
            facade.trocarParaGameOver(acertosNaFase, totalPerguntas);
        }
    }

    @Override public void show() {}
    @Override public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }
    @Override public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

        // Atualiza posição do avatar no canto superior direito após o resize
        avatarImage.setPosition(stage.getViewport().getWorldWidth() - avatarImage.getWidth() - 10,
            stage.getViewport().getWorldHeight() - avatarImage.getHeight() - 10);
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        stage.dispose();
        game.batch.dispose();
        backgroundTexture.dispose();
        avatarTexture.dispose();
    }
}





