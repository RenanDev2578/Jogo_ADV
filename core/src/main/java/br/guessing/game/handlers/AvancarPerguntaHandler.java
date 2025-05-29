package br.guessing.game.handlers;

public class AvancarPerguntaHandler extends AnswerHandler {
    @Override
    protected boolean podeProcessar(AnswerContext context) {
        return true; // Sempre processa por último
    }

    @Override
    protected void processar(AnswerContext context) {
        // Nada a fazer aqui, o ShowFeedbackHandler já agendou a próxima pergunta
    }
}
