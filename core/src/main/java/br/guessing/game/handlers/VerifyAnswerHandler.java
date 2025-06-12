package br.guessing.game.handlers;

public class VerifyAnswerHandler extends AnswerHandler {

    @Override
    protected boolean podeProcessar(AnswerContext context) {
        String resposta = context.getResposta();
        if (resposta == null || resposta.isEmpty()) {
            context.setMensagem("Nenhuma alternativa foi selecionada.");
            return false;
        }
        return true;
    }

    @Override
    protected void processar(AnswerContext context) {
        String respostaUsuario = context.getResposta().trim().toLowerCase();
        String respostaCorreta = context.getRespostaCorreta().trim().toLowerCase();

        if (respostaUsuario.equals(respostaCorreta)) {
            context.setCorreta(true);
            context.setMensagem("Resposta correta!");
        } else {
            context.setCorreta(false);
            context.setMensagem("Resposta incorreta. A resposta correta era: " + context.getRespostaCorreta());
        }
    }
}


