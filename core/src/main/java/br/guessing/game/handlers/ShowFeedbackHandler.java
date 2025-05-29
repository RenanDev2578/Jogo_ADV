package br.guessing.game.handlers;

import com.badlogic.gdx.utils.Timer;

public class ShowFeedbackHandler extends AnswerHandler {
    @Override
    protected boolean podeProcessar(AnswerContext context) {
        return true; // Sempre processa
    }

    @Override
    protected void processar(AnswerContext context) {
        // Aguarda 2 segundos antes de prosseguir
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                context.getScreen().carregarProximaPergunta();
            }
        }, 2);
    }
}
