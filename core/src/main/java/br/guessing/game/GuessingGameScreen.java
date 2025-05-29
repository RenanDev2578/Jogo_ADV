package br.guessing.game;

import br.guessing.game.handlers.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
    private Label perguntaLabel;
    private Label feedbackLabel;
    private Label acertosLabel;
    private Label timerLabel;
    private final TextButton[] botoesOpcoes = new TextButton[5];

    private float tempoRestante = 30f;
    private boolean tempoEsgotado = false;

    private Texture backgroundTexture;
    private final int totalPerguntas;
    private String[] alternativas;

    private Texture avatarTexture;
    private Image avatarImage;

    private Skin skin;
    private Table table;

    public GuessingGameScreen(GuessMaster game, GameFacade facade, Jogador jogador, Advinha advinha, int fase) {
        this.game = game;
        this.facade = facade;
        this.jogador = jogador;
        this.advinha = advinha;
        this.faseAtual = fase;
        this.totalPerguntas = advinha.getQuantidadePerguntas(faseAtual);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

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

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.BLACK;
        buttonStyle.overFontColor = Color.DARK_GRAY;
        buttonStyle.downFontColor = Color.DARK_GRAY;
        buttonStyle.checkedFontColor = Color.BLACK;
        buttonStyle.disabledFontColor = Color.GRAY;
        buttonStyle.up = skin.getDrawable("default-round");
        buttonStyle.down = skin.getDrawable("default-round-down");

        table = new Table();
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

        try {
            avatarTexture = new Texture(Gdx.files.internal("avatars/" + jogador.getAvatar()));
        } catch (Exception e) {
            avatarTexture = new Texture(Gdx.files.internal("avatars/default.png"));
        }
        avatarImage = new Image(new TextureRegionDrawable(new TextureRegion(avatarTexture)));
        avatarImage.setSize(100, 100);
        atualizarPosicaoAvatar();
        stage.addActor(avatarImage);

        carregarPergunta();
    }

    private void carregarPergunta() {
        if (perguntaAtual < totalPerguntas) {
            perguntaLabel.setText(advinha.getPergunta(faseAtual, perguntaAtual));
            feedbackLabel.setText("");
            feedbackLabel.setColor(1, 1, 1, 1);
            respostaRespondida = false;
            tempoEsgotado = false;

            alternativas = advinha.getOpcoes(faseAtual, perguntaAtual);

            for (int i = 0; i < botoesOpcoes.length; i++) {
                botoesOpcoes[i].setText((char) ('A' + i) + ": " + alternativas[i]);
                botoesOpcoes[i].setDisabled(false);
                botoesOpcoes[i].setVisible(true);
            }
        } else {
            finalizarFase();
        }

        tempoRestante = 30f;
    }

    private void verificarResposta(int indiceEscolhido) {
        respostaRespondida = true;

        AnswerContext context = new AnswerContext(this, faseAtual, perguntaAtual, alternativas, indiceEscolhido);

        AnswerHandler chain = new VerifyAnswerHandler();
        chain
            .setProximo(new UpdatePontuationHandler())
            .setProximo(new ShowFeedbackHandler())
            .setProximo(new AvancarPerguntaHandler());

        chain.handle(context);

        for (TextButton btn : botoesOpcoes) {
            btn.setDisabled(true);
        }
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

    private void atualizarPosicaoAvatar() {
        avatarImage.setPosition(
            stage.getViewport().getWorldWidth() - avatarImage.getWidth() - 10,
            stage.getViewport().getWorldHeight() - avatarImage.getHeight() - 10
        );
    }

    private void tratarTempoEsgotado() {
        if (respostaRespondida) {
            return;
        }

        respostaRespondida = true;
        tempoEsgotado = true;
        mostrarFeedback("Tempo esgotado!", 1, 0, 0);

        for (TextButton btn : botoesOpcoes) {
            btn.setDisabled(true);
        }

        AnswerContext context = new AnswerContext(this, faseAtual, perguntaAtual, alternativas, -1);
        AnswerHandler chain = new VerifyAnswerHandler();
        chain
            .setProximo(new UpdatePontuationHandler())
            .setProximo(new ShowFeedbackHandler());

        chain.handle(context);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                carregarProximaPergunta();
            }
        }, 2);
    }

    @Override
    public void show() {
        timerLabel = new Label("Tempo: " + (int) tempoRestante + "s", skin);
        table.add(timerLabel).padBottom(20).row();
    }

    @Override
    public void render(float delta) {
        if (!respostaRespondida && !tempoEsgotado) {
            tempoRestante -= delta;
            if (tempoRestante <= 0) {
                tempoRestante = 0;
                tratarTempoEsgotado();
            }
            timerLabel.setText("Tempo: " + (int) tempoRestante + "s");
        }

        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        atualizarPosicaoAvatar();
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
        avatarTexture.dispose();
        skin.dispose();
    }

    public void incrementarAcertos() {
        acertosNaFase++;
    }

    public void atualizarAcertosLabel() {
        acertosLabel.setText("Acertos: " + acertosNaFase + "/" + totalPerguntas);
    }

    public void mostrarFeedback(String mensagem, float r, float g, float b) {
        feedbackLabel.setText(mensagem);
        feedbackLabel.setColor(r, g, b, 1);
    }

    public void carregarProximaPergunta() {
        perguntaAtual++;
        carregarPergunta();
    }

    public Advinha getAdvinha() {
        return advinha;
    }
}

