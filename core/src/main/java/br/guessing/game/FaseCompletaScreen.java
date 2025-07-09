package br.guessing.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class FaseCompletaScreen extends BaseScreen {

    private int acertos;
    private int total;
    private int proximaFase;
    private int pontuacaoDaFase;

    private Texture backgroundTexture;
    private Image backgroundImage;

    public FaseCompletaScreen(GuessMaster game, GameFacade facade, int acertos, int total, int proximaFase, int pontuacaoDaFase) {
        super(game, facade.getJogador());

        this.acertos = acertos;
        this.total = total;
        this.proximaFase = proximaFase;
        this.pontuacaoDaFase = pontuacaoDaFase;

        backgroundTexture = new Texture(Gdx.files.internal("fase" + proximaFase + ".png"));
        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);
    }

    @Override

    public void show() {
        super.show();
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(500);
        stage.addActor(table);

        Label pontosLabel = new Label("Pontuação da fase: " + pontuacaoDaFase + " pts", skin);
        pontosLabel.setFontScale(1.2f);
        table.add(pontosLabel).padBottom(30).row();

        Label titulo = new Label("Fase Completa!", skin);
        titulo.setFontScale(1.5f);
        titulo.setAlignment(Align.center);
        table.add(titulo).padBottom(20).row();

        Label resultado = new Label("Você acertou " + acertos + " de " + total + " perguntas.", skin);
        resultado.setFontScale(1.2f);
        resultado.setAlignment(Align.center);
        table.add(resultado).padBottom(30).row();

        // Lógica de recompensa
      /*  if (proximaFase > 1) {
            Jogador jogador = facade.getJogador();
            if (pontuacaoDaFase >= 90) {
                jogador.adicionarRecompensa(TipoRecompensa.MAIS_TEMPO);
            } else if (pontuacaoDaFase >= 80) {
                jogador.adicionarRecompensa(TipoRecompensa.ELIMINAR_OPCAO);
            } else if (pontuacaoDaFase >= 60) {
                jogador.adicionarRecompensa(TipoRecompensa.DICA_MEDIA);
            } else if (pontuacaoDaFase >= 40) {
                jogador.adicionarRecompensa(TipoRecompensa.DICA_LEVE);
            }
        }*/ if (proximaFase > 1) {
            Jogador jogador = facade.getJogador();

            if (pontuacaoDaFase == 100) {
                // Acertou tudo → ganha 2 recompensas
                jogador.adicionarRecompensa(TipoRecompensa.MAIS_TEMPO);
                jogador.adicionarRecompensa(TipoRecompensa.ELIMINAR_OPCAO);
            } else if (pontuacaoDaFase == 75) {
                jogador.adicionarRecompensa(TipoRecompensa.ELIMINAR_OPCAO);
            } else if (pontuacaoDaFase == 50) {
                jogador.adicionarRecompensa(TipoRecompensa.DICA_MEDIA);
            }
            // Nota: abaixo de 50 não recebe recompensa
        }

        TextButton proximaButton = new TextButton("Próxima Fase", skin);
        proximaButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                facade.trocarParaProximaFase(acertos, total);
            }
        });
        table.add(proximaButton).width(220).height(60);
    }


    @Override
    public void dispose() {
        super.dispose();
        backgroundTexture.dispose();
    }
}
