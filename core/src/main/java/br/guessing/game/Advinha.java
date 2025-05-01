package br.guessing.game;



import java.util.*;

public class Advinha {
    private String pergunta;
    private String resposta;

    private final Map<Integer, List<String[]>> perguntasPorFase = new HashMap<>();

    public Advinha() {

        perguntasPorFase.put(1, Arrays.asList(
                new String[]{"O que é, o que é? Anda com os pés na cabeça.", "piolho"},
                new String[]{"O que é, o que é? Quanto mais se tira, maior fica.", "buraco"},
                new String[]{"O que é, o que é? Tem dentes mas não morde.", "pente"},
                new String[]{"O que é, o que é? Entra na água e não se molha.", "sombra"},
                new String[]{"O que é, o que é? Sempre cai, mas nunca se machuca.", "chuva"}
        ));

        perguntasPorFase.put(1, Arrays.asList(
            new String[]{"O que é, o que é? Anda com os pés na cabeça.", "piolho"},
            new String[]{"O que é, o que é? Quanto mais se tira, maior fica.", "buraco"},
            new String[]{"O que é, o que é? Tem dentes mas não morde.", "pente"},
            new String[]{"O que é, o que é? Entra na água e não se molha.", "sombra"},
            new String[]{"O que é, o que é? Sempre cai, mas nunca se machuca.", "chuva"}
        ));
    }
}

