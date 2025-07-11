package br.guessing.game.handlers;

public class UpdatePontuationHandler extends AnswerHandler {

    @Override
    protected boolean podeProcessar(AnswerContext context) {
        return context.isCorreta();
    }

    @Override
    protected void processar(AnswerContext context) {
        context.getScreen().incrementarAcertos();
        context.getScreen().atualizarAcertosLabel();
        context.getScreen().mostrarFeedback("Acertou!", 0, 0.5f, 0);
    }
}
