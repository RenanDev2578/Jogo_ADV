package br.guessing.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.Batch;

public class GuessMaster extends Game {
    public Batch batch;
    private GameFacade facade;

    @Override
    public void create() {
        facade = new GameFacade(this);
        facade.iniciarJogo();
    }

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (facade != null && facade.getJogador() != null) {
            facade.getJogador().dispose();
        }
        super.dispose();
    }

    public GameFacade getFacade() {
        return facade;
    }

    public Jogador getJogador() {
        return facade.getJogador();
    }
}






