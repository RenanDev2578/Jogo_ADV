package br.guessing.game.handlers;

public class UpdatePontuationHandler extends AnswerHandler {
    @Override
    protected boolean podeProcessar(AnswerContext context) {
        return true;  // Sempre processa para manter o fluxo
    }

    @Override
    protected void processar(AnswerContext context) {
        boolean acertou = context.getScreen().getAdvinha().getUltimoResultado();
        if (acertou) {
            context.getScreen().incrementarAcertos();
            context.getScreen().atualizarAcertosLabel();
        }
        // Caso tenha errado ou tempo esgotado, não incrementa mas segue o fluxo normalmente
    }
}

