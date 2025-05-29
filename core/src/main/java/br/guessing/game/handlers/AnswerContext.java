package br.guessing.game.handlers;

import br.guessing.game.Advinha;
import br.guessing.game.GuessingGameScreen;

public class AnswerContext {
    private final GuessingGameScreen screen;
    private final int faseAtual;
    private final int perguntaAtual;
    private final String[] alternativas;
    private final int indiceEscolhido;

    public AnswerContext(GuessingGameScreen screen, int faseAtual, int perguntaAtual, String[] alternativas, int indiceEscolhido) {
        this.screen = screen;
        this.faseAtual = faseAtual;
        this.perguntaAtual = perguntaAtual;
        this.alternativas = alternativas;
        this.indiceEscolhido = indiceEscolhido;
    }

    // Getters
    public GuessingGameScreen getScreen() { return screen; }
    public int getFaseAtual() { return faseAtual; }
    public int getPerguntaAtual() { return perguntaAtual; }
    public String[] getAlternativas() { return alternativas; }
    public int getIndiceEscolhido() { return indiceEscolhido; }
}
