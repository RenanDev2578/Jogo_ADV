package br.guessing.game.handlers;

public class VerifyAnswerHandler extends AnswerHandler {
    @Override
    protected boolean podeProcessar(AnswerContext context) {
        return true; // Sempre processa primeiro
    }

    @Override
    protected void processar(AnswerContext context) {
        String respostaCorreta = context.getScreen().getAdvinha().getResposta(
            context.getFaseAtual(),
            context.getPerguntaAtual()
        );

        boolean acertou = respostaCorreta.equalsIgnoreCase(
            context.getAlternativas()[context.getIndiceEscolhido()]
        );

        context.getScreen().mostrarFeedback(
            acertou ? "Correto!" : "Errado! A resposta correta era: " + respostaCorreta,
            acertou ? 0 : 1, acertou ? 1 : 0, 0
        );

        // Armazena o resultado no contexto para handlers subsequentes
        context.getScreen().getAdvinha().setUltimoResultado(acertou);
    }
}
