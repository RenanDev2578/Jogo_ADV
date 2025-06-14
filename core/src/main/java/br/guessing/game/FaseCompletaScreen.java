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

    private Texture backgroundTexture;
    private Image backgroundImage;

    public FaseCompletaScreen(GuessMaster game, GameFacade facade, int acertos, int total, int proximaFase) {
        super(game, facade.getJogador());

        this.acertos = acertos;
        this.total = total;
        this.proximaFase = proximaFase;


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

        Label titulo = new Label("Fase Completa!", skin);
        titulo.setFontScale(1.5f);
        titulo.setAlignment(Align.center);

        Label resultado = new Label("Você acertou " + acertos + " de " + total + " perguntas.", skin);
        resultado.setFontScale(1.2f);
        resultado.setAlignment(Align.center);

        TextButton proximaButton = new TextButton("Próxima Fase", skin);
        proximaButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                facade.trocarParaProximaFase(acertos, total);
            }
        });

        table.add(titulo).padBottom(20).row();
        table.add(resultado).padBottom(30).row();
        table.add(proximaButton).width(220).height(60);
    }

    @Override
    public void dispose() {
        super.dispose();
        backgroundTexture.dispose();
    }
}
