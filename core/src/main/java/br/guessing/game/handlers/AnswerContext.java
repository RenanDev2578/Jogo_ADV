package br.guessing.game.handlers;

import br.guessing.game.GuessingGameScreen;

public class AnswerContext {
    private final GuessingGameScreen screen;
    private final int faseAtual;
    private final int perguntaAtual;
    private final String[] alternativas;
    private final int indiceEscolhido;

    private boolean correta;
    private String respostaCorreta;
    private String mensagem;


    public AnswerContext(GuessingGameScreen screen, int faseAtual, int perguntaAtual, String[] alternativas, int indiceEscolhido, String respostaCorreta) {
        this.screen = screen;
        this.faseAtual = faseAtual;
        this.perguntaAtual = perguntaAtual;
        this.alternativas = alternativas;
        this.indiceEscolhido = indiceEscolhido;
        this.respostaCorreta = respostaCorreta;
    }


    public GuessingGameScreen getScreen() { return screen; }
    public int getFaseAtual() { return faseAtual; }
    public int getPerguntaAtual() { return perguntaAtual; }
    public String[] getAlternativas() { return alternativas; }
    public int getIndiceEscolhido() { return indiceEscolhido; }


    public void setCorreta(boolean correta) {
        this.correta = correta;
    }

    public boolean isCorreta() {
        return correta;
    }

    public void setRespostaCorreta(String resposta) {
        this.respostaCorreta = resposta;
    }

    public String getRespostaCorreta() {
        return respostaCorreta;
    }

    public void setMensagem(String s) {
        this.mensagem = s;
    }

    public String getMensagem() {
        return mensagem;
    }


    public String getResposta() {
        if (respostaValida()) {
            return alternativas[indiceEscolhido];
        }
        return null;
    }


    public boolean respostaValida() {
        return alternativas != null && indiceEscolhido >= 0 && indiceEscolhido < alternativas.length;
    }
}
