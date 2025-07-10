package br.guessing.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.List;

public class VictoryScreen extends BaseScreen {
    private final Jogador jogador;
    private final int pontuacaoFinal;
    private final RankingManager rankingManager;
    private Skin skin;
    private Music victoryMusic;
    private Texture backgroundTexture;

    public VictoryScreen(GuessMaster game, Jogador jogador, int pontuacaoFinal, RankingManager rankingManager) {
        super(game, jogador);
        this.jogador = jogador;
        this.pontuacaoFinal = pontuacaoFinal;
        this.rankingManager = rankingManager;
    }

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));


        Label.LabelStyle labelStyle = new Label.LabelStyle();
        BitmapFont font = skin.getFont("default-font");
        font.getData().setScale(1.5f);
        labelStyle.font = font;
        labelStyle.fontColor = Color.BLACK;


        backgroundTexture = new Texture(Gdx.files.internal("vitoria.png"));
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setFillParent(true);
        stage.addActor(backgroundImage);

        Image avatarImage = new Image(jogador.getAvatarTexture());
        avatarImage.setSize(80, 80);
        avatarImage.setPosition(Gdx.graphics.getWidth() - 90, Gdx.graphics.getHeight() - 90);
        stage.addActor(avatarImage);


        rankingManager.adicionarOuAtualizarJogador(jogador);
        rankingManager.saveRanking();

        // Tabela principal
        Table tablePrincipal = new Table();
        tablePrincipal.setFillParent(true);
        tablePrincipal.center();
        stage.addActor(tablePrincipal);

        Label congratsLabel = new Label("Parabens! Voce venceu!", labelStyle);
        Label scoreLabel = new Label("Pontuacao final: " + pontuacaoFinal + " pts", labelStyle);

        tablePrincipal.add(congratsLabel).padBottom(20).row();
        tablePrincipal.add(scoreLabel).padBottom(30).row();


        Table tableRanking = new Table();
        tableRanking.center();
        tableRanking.setPosition(Gdx.graphics.getWidth() / 2f, 415);
        stage.addActor(tableRanking);

        Label rankingTitle = new Label("Ranking Top 3", labelStyle);
        tableRanking.add(rankingTitle).padBottom(10).row();

        List<Jogador> topJogadores = rankingManager.getTopJogadores();
        for (int i = 0; i < topJogadores.size(); i++) {
            Jogador j = topJogadores.get(i);
            Label rankingEntry = new Label((i + 1) + ". " + j.getNome() + " - " + j.getPontuacaoTotal() + " pts", labelStyle);
            tableRanking.add(rankingEntry).row();
        }


        tablePrincipal.add().height(100).row();

        // Botões
        TextButton jogarNovamenteButton = new TextButton("Jogar Novamente", skin);
        jogarNovamenteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (victoryMusic != null) victoryMusic.stop();
                game.getFacade().iniciarJogo();
            }
        });
        tablePrincipal.add(jogarNovamenteButton).width(200).height(50).padBottom(10).row();

        TextButton sairButton = new TextButton("Sair", skin);
        sairButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        tablePrincipal.add(sairButton).width(200).height(50).row();


        game.getFacade().stopMusic();
        victoryMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/victory.mp3"));
        victoryMusic.setVolume(0.5f);
        victoryMusic.play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render(delta);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (skin != null) skin.dispose();
        if (victoryMusic != null) victoryMusic.dispose();
        if (backgroundTexture != null) backgroundTexture.dispose();
    }
}
