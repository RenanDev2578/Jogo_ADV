package br.guessing.game;

import java.util.List;

public class Pergunta {
    private final String texto;
    private final String respostaCorreta;
    private final List<String> respostasParecidas;

    public Pergunta(String texto, String respostaCorreta, List<String> respostasParecidas) {
        this.texto = texto;
        this.respostaCorreta = respostaCorreta;
        this.respostasParecidas = respostasParecidas;
    }

    public String getTexto() {
        return texto;
    }

    public String getRespostaCorreta() {
        return respostaCorreta;
    }

    public List<String> getRespostasParecidas() {
        return respostasParecidas;
    }
}
