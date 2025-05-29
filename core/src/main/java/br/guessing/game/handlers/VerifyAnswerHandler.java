package br.guessing.game.handlers;

import br.guessing.game.Advinha;
import br.guessing.game.GuessingGameScreen;

public class VerifyAnswerHandler extends AnswerHandler {

    @Override
    protected boolean podeProcessar(AnswerContext context) {
        return true; // Sempre processa
    }

    @Override
    protected void processar(AnswerContext context) {
        GuessingGameScreen screen = context.getScreen();
        Advinha advinha = screen.getAdvinha();

        int fase = context.getFaseAtual();
        int pergunta = context.getPerguntaAtual();
        int escolhido = context.getIndiceEscolhido();
        String[] alternativas = context.getAlternativas();

        String respostaCorreta = advinha.getResposta(fase, pergunta);
        String respostaUsuario = (escolhido >= 0 && escolhido < alternativas.length)
            ? alternativas[escolhido]
            : null;

        boolean acertou = respostaUsuario != null && respostaCorreta.equalsIgnoreCase(respostaUsuario);

        if (respostaUsuario == null) {
            screen.mostrarFeedback("Tempo esgotado! Resposta: " + respostaCorreta, 1, 0, 0);
        } else if (acertou) {
            screen.mostrarFeedback("Correto!", 0, 1, 0);
        } else {
            screen.mostrarFeedback("Errado! Resposta correta era: " + respostaCorreta, 1, 0, 0);
        }

        advinha.setUltimoResultado(acertou);
    }
}



