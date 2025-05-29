package br.guessing.game.handlers;

public abstract class AnswerHandler {
    private AnswerHandler proximo;

    public AnswerHandler setProximo(AnswerHandler handler) {
        this.proximo = handler;
        return handler;
    }

    public void handle(AnswerContext context) {
        if (podeProcessar(context)) {
            processar(context);
        }

        if (proximo != null) {
            proximo.handle(context);
        }
    }

    protected abstract boolean podeProcessar(AnswerContext context);
    protected abstract void processar(AnswerContext context);
}
