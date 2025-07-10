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
        table.top().padTop(415);
        stage.addActor(table);

        Label titulo = new Label("Fase Completa!", skin);
        titulo.setFontScale(2f);
        titulo.setAlignment(Align.center);
        table.add(titulo).padBottom(40).row();

        Label resultado = new Label("Voce acertou " + acertos + " de " + total + " perguntas.", skin);
        resultado.setFontScale(1.6f);
        resultado.setAlignment(Align.center);
        table.add(resultado).padBottom(30).row();

        Label pontosLabel = new Label("Pontuação da fase: " + pontuacaoDaFase + " pts", skin);
        pontosLabel.setFontScale(1.6f);
        pontosLabel.setAlignment(Align.center);
        table.add(pontosLabel).padBottom(30).row();

        // Exibir mensagem de recompensa, se houver
        if (proximaFase > 1) {
            Jogador jogador = facade.getJogador();
            boolean ganhouRecompensa = false;

            if (pontuacaoDaFase == 100) {
                jogador.adicionarRecompensa(TipoRecompensa.MAIS_TEMPO);
                jogador.adicionarRecompensa(TipoRecompensa.ELIMINAR_OPCAO);
                ganhouRecompensa = true;
            } else if (pontuacaoDaFase == 75) {
                jogador.adicionarRecompensa(TipoRecompensa.ELIMINAR_OPCAO);
                ganhouRecompensa = true;
            } else if (pontuacaoDaFase == 50) {
                jogador.adicionarRecompensa(TipoRecompensa.DICA_MEDIA);
                ganhouRecompensa = true;
            }

            if (ganhouRecompensa) {
                Label tituloRecompensaLabel = new Label("Suas recompensa(s):", skin);
                tituloRecompensaLabel.setFontScale(1.6f);
                tituloRecompensaLabel.setAlignment(Align.center);
                table.add(tituloRecompensaLabel).padBottom(15).row();

                Label recompensaLabel = new Label("Voce ganhou recompensa(s)!", skin);
                recompensaLabel.setFontScale(1.5f);
                recompensaLabel.setAlignment(Align.center);
                table.add(recompensaLabel).padBottom(40).row();
            } else {
                table.add().height(40).row();
            }
        } else {
            table.add().height(40).row();
        }

        TextButton proximaButton = new TextButton("Proxima Fase", skin);
        proximaButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                facade.trocarParaProximaFase(acertos, total);
            }
        });
        table.add(proximaButton).width(240).height(70);
    }

    @Override
    public void dispose() {
        super.dispose();
        backgroundTexture.dispose();
    }
}
