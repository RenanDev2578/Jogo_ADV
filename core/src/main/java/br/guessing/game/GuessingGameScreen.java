package br.guessing.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GuessingGameScreen implements Screen {

    private final MainGame game;
    private final Jogador jogador;
    private final Advinha advinha;
    private final int faseAtual;
    private int perguntaAtual = 0;
    private int acertosNaFase = 0;
    private boolean respostaRespondida = false;

    private final Stage stage;
    private final Label perguntaLabel;
    private final TextField respostaField;
    private final Label feedbackLabel;
    private final TextButton responderButton;
    private final Label acertosLabel;
    private final TextButton proximaButton;

    public GuessingGameScreen(MainGame game, Jogador jogador, Advinha advinha, int fase) {
        this.game = game;
        this.jogador = jogador;
        this.advinha = advinha;
        this.faseAtual = fase;

        game.batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        BitmapFont font = new BitmapFont();

        perguntaLabel = new Label("", new Label.LabelStyle(font, null));
        respostaField = new TextField("", skin);
        feedbackLabel = new Label("", new Label.LabelStyle(font, null));
        responderButton = new TextButton("Responder", skin);
        acertosLabel = new Label("Acertos: 0/3", new Label.LabelStyle(font, null));
        proximaButton = new TextButton("Próxima Pergunta", skin);
        proximaButton.setVisible(false);

        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(50);
        table.add(acertosLabel).padBottom(20).row();
        table.add(perguntaLabel).padBottom(20).row();
        table.add(respostaField).width(300).padBottom(10).row();
        table.add(responderButton).width(200).padBottom(10).row();
        table.add(proximaButton).width(200).padBottom(10).row();
        table.add(feedbackLabel).padTop(10).row();

        stage.addActor(table);

        carregarPergunta();

        responderButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!respostaRespondida) {
                    verificarResposta();
                }
            }
        });

        proximaButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                perguntaAtual++;
                respostaRespondida = false;
                carregarPergunta();
                proximaButton.setVisible(false);
                responderButton.setVisible(true);
                respostaField.setDisabled(false);
            }
        });
    }

    private void carregarPergunta() {
        if (perguntaAtual < 3) {
            perguntaLabel.setText(advinha.getPergunta(1, perguntaAtual));
            respostaField.setText("");
            feedbackLabel.setText("");
            feedbackLabel.setColor(1, 1, 1, 1);
            respostaField.setDisabled(false);
        } else {
            finalizarFase();
        }
    }

    private void verificarResposta() {
        String resposta = respostaField.getText().trim().toLowerCase();
        String respostaCorreta = advinha.getResposta(1, perguntaAtual).toLowerCase();
        respostaRespondida = true;

        if (resposta.equals(respostaCorreta)) {
            feedbackLabel.setText("Correto! Resposta: " + respostaCorreta);
            feedbackLabel.setColor(0, 1, 0, 1);
            acertosNaFase++;
            acertosLabel.setText("Acertos: " + acertosNaFase + "/3");
        } else {
            feedbackLabel.setText("Errado! Resposta correta: " + respostaCorreta);
            feedbackLabel.setColor(1, 0, 0, 1);
        }

        responderButton.setVisible(false);
        respostaField.setDisabled(true);

        if (perguntaAtual < 2) {
            proximaButton.setVisible(true);
        } else {

            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Gdx.app.postRunnable(() -> finalizarFase());
            }).start();
        }
    }

    private void finalizarFase() {
        if (acertosNaFase >= 2) {
            game.mostrarFaseCompleta(acertosNaFase, 3, faseAtual + 1);
        } else {
            feedbackLabel.setText("Você perdeu! Acertos: " + acertosNaFase + "/3");
            feedbackLabel.setColor(1, 0, 0, 1);
            game.trocarParaGameOver(acertosNaFase, 3);
        }
    }



    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
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
        game.batch.dispose();
    }
}
