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
    private Label labelRecompensas;
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
    private Table tablePrincipal;
    private Table tableInferior;

    private ImageButton botaoMusicaIcone;
    private Label labelMusica;

    private TextButton botaoDicaLeve;
    private TextButton botaoDicaMedia;
    private TextButton botaoEliminar;
    private TextButton botaoMaisTempo;

    private int pontuacaoAcumuladaDaFase = 0;

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

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        BitmapFont font = new BitmapFont();
        font.getData().setScale(2.0f);
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);

        perguntaLabel = new Label("", labelStyle);
        feedbackLabel = new Label("", labelStyle);
        acertosLabel = new Label("Acertos: 0/" + totalPerguntas, labelStyle);
        timerLabel = new Label("Tempo: " + (int) tempoRestante + "s", labelStyle);
        timerLabel.setColor(Color.FIREBRICK);

        tablePrincipal = new Table();
        tablePrincipal.setFillParent(true);
        tablePrincipal.top().padTop(50);
        stage.addActor(tablePrincipal);

        tablePrincipal.add(acertosLabel).padBottom(20).colspan(1).row();
        tablePrincipal.add(timerLabel).padBottom(20).colspan(1).row();
        tablePrincipal.add(perguntaLabel).padBottom(20).row();

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.BLACK;
        buttonStyle.up = skin.getDrawable("default-round");
        buttonStyle.down = skin.getDrawable("default-round-down");

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
            tablePrincipal.add(botoesOpcoes[i]).width(300).height(60).pad(10).center().row();
        }

        tablePrincipal.add(feedbackLabel).padTop(10).row();

        tableInferior = new Table();
        tableInferior.setFillParent(true);
        tableInferior.bottom().padBottom(20);
        stage.addActor(tableInferior);

        labelRecompensas = new Label("SUAS RECOMPENSA(S):", new Label.LabelStyle(font, Color.BLACK));
        tableInferior.add(labelRecompensas).colspan(4).padBottom(10).row();

        botaoDicaLeve = new TextButton("Usar Dica Leve", skin);
        botaoDicaMedia = new TextButton("Usar Dica Média", skin);
        botaoEliminar = new TextButton("Eliminar 2 Opções", skin);
        botaoMaisTempo = new TextButton("Tempo Extra", skin);

        botaoDicaLeve.addListener(new ClickListener() {
            @Override public void clicked(InputEvent event, float x, float y) {
                if (jogador.usarRecompensa(TipoRecompensa.DICA_LEVE)) {
                    mostrarFeedback("Dica: " + gerarDica(), 0, 0, 1);
                    botaoDicaLeve.setVisible(false);
                }
            }
        });

        botaoDicaMedia.addListener(new ClickListener() {
            @Override public void clicked(InputEvent event, float x, float y) {
                if (jogador.usarRecompensa(TipoRecompensa.DICA_MEDIA)) {
                    eliminarOpcoesIncorretas(1);
                    botaoDicaMedia.setVisible(false);
                }
            }
        });

        botaoEliminar.addListener(new ClickListener() {
            @Override public void clicked(InputEvent event, float x, float y) {
                if (jogador.usarRecompensa(TipoRecompensa.ELIMINAR_OPCAO)) {
                    eliminarOpcoesIncorretas(2);
                    botaoEliminar.setVisible(false);
                }
            }
        });

        botaoMaisTempo.addListener(new ClickListener() {
            @Override public void clicked(InputEvent event, float x, float y) {
                if (jogador.usarRecompensa(TipoRecompensa.MAIS_TEMPO)) {
                    tempoRestante += 10;
                    mostrarFeedback("Tempo extra ativado!", 0, 0.7f, 0);
                    botaoMaisTempo.setVisible(false);
                }
            }
        });

        tableInferior.add(botaoDicaLeve).pad(5);
        tableInferior.add(botaoDicaMedia).pad(5);
        tableInferior.add(botaoEliminar).pad(5);
        tableInferior.add(botaoMaisTempo).pad(5);

        try {
            avatarTexture = new Texture(Gdx.files.internal("avatars/" + jogador.getAvatar()));
        } catch (Exception e) {
            avatarTexture = new Texture(Gdx.files.internal("avatars/default.png"));
        }

        avatarImage = new Image(new TextureRegionDrawable(new TextureRegion(avatarTexture)));
        avatarImage.setSize(100, 100);
        stage.addActor(avatarImage);

        Texture textureVolumeOn = new Texture(Gdx.files.internal("audio/volume-up.png"));
        Texture textureMute = new Texture(Gdx.files.internal("audio/mute.png"));

        TextureRegionDrawable drawableOn = new TextureRegionDrawable(new TextureRegion(textureVolumeOn));
        TextureRegionDrawable drawableOff = new TextureRegionDrawable(new TextureRegion(textureMute));

        botaoMusicaIcone = new ImageButton(facade.isMusicaLigada() ? drawableOn : drawableOff);
        botaoMusicaIcone.setSize(50, 50);
        stage.addActor(botaoMusicaIcone);

        labelMusica = new Label(facade.isMusicaLigada() ? "Desligar música" : "Ligar música", new Label.LabelStyle(font, Color.BLACK));
        labelMusica.setFontScale(1.2f);
        stage.addActor(labelMusica);

        botaoMusicaIcone.addListener(new ClickListener() {
            @Override public void clicked(InputEvent event, float x, float y) {
                if (facade.isMusicaLigada()) {
                    facade.desligarMusica();
                    botaoMusicaIcone.getStyle().imageUp = drawableOff;
                    labelMusica.setText("Ligar música");
                } else {
                    facade.ligarMusica();
                    botaoMusicaIcone.getStyle().imageUp = drawableOn;
                    labelMusica.setText("Desligar música");
                }
            }
        });

        atualizarPosicaoAvatar();
        carregarPergunta();
    }


    private void carregarPergunta() {
        if (perguntaAtual < totalPerguntas) {
            perguntaLabel.setText(advinha.getPergunta(faseAtual, perguntaAtual));
            feedbackLabel.setText("");
            feedbackLabel.setColor(1, 1, 1, 1);
            respostaRespondida = false;
            tempoEsgotado = false;
            tempoRestante = 30f;

            alternativas = advinha.getOpcoes(faseAtual, perguntaAtual);
            for (int i = 0; i < botoesOpcoes.length; i++) {
                botoesOpcoes[i].setText((char) ('A' + i) + ": " + alternativas[i]);
                botoesOpcoes[i].setDisabled(false);
                botoesOpcoes[i].setVisible(true);
            }

            botaoDicaLeve.setVisible(jogador.temRecompensa(TipoRecompensa.DICA_LEVE));
            botaoDicaMedia.setVisible(jogador.temRecompensa(TipoRecompensa.DICA_MEDIA));
            botaoEliminar.setVisible(jogador.temRecompensa(TipoRecompensa.ELIMINAR_OPCAO));
            botaoMaisTempo.setVisible(jogador.temRecompensa(TipoRecompensa.MAIS_TEMPO));
        } else {
            finalizarFase();
        }
    }

    private void verificarResposta(int indiceEscolhido) {
        respostaRespondida = true;
        String correctAnswer = advinha.getResposta(faseAtual, perguntaAtual);

        AnswerContext context = new AnswerContext(this, faseAtual, perguntaAtual, alternativas, indiceEscolhido, correctAnswer);
        AnswerHandler chain = new VerifyAnswerHandler();
        chain.setProximo(new UpdatePontuationHandler())
            .setProximo(new ShowFeedbackHandler())
            .setProximo(new AvancarPerguntaHandler());
        chain.handle(context);

        if (alternativas[indiceEscolhido].equals(correctAnswer)) {
            int pontos = calcularPontuacao(faseAtual, perguntaAtual);
            pontuacaoAcumuladaDaFase += pontos;
            jogador.adicionarPontuacao(pontos);
        }

        for (TextButton btn : botoesOpcoes) btn.setDisabled(true);
    }

    private int calcularPontuacao(int fase, int pergunta) {
        if (fase >= 1 && fase <= 5) {
            switch (pergunta) {
                case 0:
                case 1:
                    return 25;
                case 2:
                    return 50;
            }
        } else if (fase == 6) {
            switch (pergunta) {
                case 0:
                case 1:
                    return 10;
                case 2:
                    return 25;
                case 3:
                    return 15;
                case 4:
                case 5:
                    return 20;
            }
        }
        return 0;
    }

    private void finalizarFase() {
        if (acertosNaFase >= 2) {
            jogador.adicionarAcertos(acertosNaFase);
            jogador.adicionarPontuacao(pontuacaoAcumuladaDaFase);
            if (faseAtual < 6) {
                facade.mostrarFaseCompleta(acertosNaFase, totalPerguntas, faseAtual + 1, pontuacaoAcumuladaDaFase);
            } else {
                facade.trocarParaVictory();
            }
        } else {
            facade.trocarParaGameOver(acertosNaFase, totalPerguntas);
        }
    }

    private void tratarTempoEsgotado() {
        if (respostaRespondida) return;
        respostaRespondida = true;
        tempoEsgotado = true;
        mostrarFeedback("Tempo esgotado!", 1, 0, 0);
        for (TextButton btn : botoesOpcoes) btn.setDisabled(true);

        String correctAnswer = advinha.getResposta(faseAtual, perguntaAtual);
        AnswerContext context = new AnswerContext(this, faseAtual, perguntaAtual, alternativas, -1, correctAnswer);

        AnswerHandler chain = new VerifyAnswerHandler();
        chain.setProximo(new ShowFeedbackHandler());
        chain.handle(context);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                carregarProximaPergunta();
            }
        }, 2);
    }

    private String gerarDica() {
        String resposta = advinha.getResposta(faseAtual, perguntaAtual);
        return resposta.length() >= 3 ?
            "Começa com: " + resposta.substring(0, 2).toUpperCase() :
            "Tem " + resposta.length() + " letras.";
    }

    private void eliminarOpcoesIncorretas(int quantidade) {
        int removidos = 0;
        String correta = advinha.getResposta(faseAtual, perguntaAtual).toLowerCase();

        for (int i = 0; i < botoesOpcoes.length; i++) {
            String texto = botoesOpcoes[i].getText().toString().toLowerCase();
            if (!texto.contains(correta) && botoesOpcoes[i].isVisible()) {
                botoesOpcoes[i].setVisible(false);
                removidos++;
            }
            if (removidos >= quantidade) break;
        }

        int visiveis = 0;
        for (TextButton btn : botoesOpcoes) if (btn.isVisible()) visiveis++;

        if (visiveis < 2) {
            for (TextButton btn : botoesOpcoes) btn.setVisible(true);
            mostrarFeedback("Erro ao eliminar opções. Tentativa ignorada.", 1, 0.5f, 0);
        }
    }

    private void atualizarPosicaoAvatar() {
        avatarImage.setPosition(stage.getViewport().getWorldWidth() - avatarImage.getWidth() - 10,
            stage.getViewport().getWorldHeight() - avatarImage.getHeight() - 10);
        botaoMusicaIcone.setPosition(10, stage.getViewport().getWorldHeight() - botaoMusicaIcone.getHeight() - 10);
        labelMusica.setPosition(10, stage.getViewport().getWorldHeight() - 90);
    }

    @Override
    public void show() {
        if (facade.isMusicaLigada()) {
            facade.getBackgroundMusic().setVolume(0.3f);
        } else {
            facade.getBackgroundMusic().setVolume(0f);
        }
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

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

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
