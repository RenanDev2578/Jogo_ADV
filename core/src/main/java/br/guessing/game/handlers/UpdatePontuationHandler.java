package br.guessing.game.handlers;

public class UpdatePontuationHandler extends AnswerHandler {
    @Override
    protected boolean podeProcessar(AnswerContext context) {
        return context.getScreen().getAdvinha().getUltimoResultado();
    }

    @Override
    protected void processar(AnswerContext context) {
        context.getScreen().incrementarAcertos();
        context.getScreen().atualizarAcertosLabel();
    }
}
