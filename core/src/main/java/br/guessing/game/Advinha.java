package br.guessing.game;

import java.util.*;

public class Advinha {

    private final Map<Integer, List<String[]>> perguntasPorFase;

    public Advinha() {
        perguntasPorFase = new HashMap<>();

        perguntasPorFase.put(1, Arrays.asList(
            new String[]{"O que é, o que é? Anda com os pés na cabeça.", "piolho"},
            new String[]{"O que é, o que é? Quanto mais se tira, maior fica.", "buraco"},
            new String[]{"O que é, o que é? Tem dentes mas não morde.", "pente"}
        ));

        perguntasPorFase.put(2, Arrays.asList(
            new String[]{"O que é, o que é? Entra na água e não se molha.", "sombra"},
            new String[]{"O que é, o que é? Sempre cai, mas nunca se machuca.", "chuva"},
            new String[]{"O que é, o que é? Tem asas, mas não voa, e um bico, mas não bica.", "bule"}
        ));

        perguntasPorFase.put(3, Arrays.asList(
            new String[]{"O que é, o que é? Quanto mais se tira, mais aumenta.", "buraco"},
            new String[]{"O que é, o que é? Passa diante do sol e não faz sombra.", "vento"},
            new String[]{"O que é, o que é? Não se come, mas é bom para se mastigar.", "goma"}
        ));

        perguntasPorFase.put(4, Arrays.asList(
            new String[]{"O que é, o que é? Tem pescoço, mas não tem cabeça.", "garrafa"},
            new String[]{"O que é, o que é? Sobe, sobe, mas nunca desce.", "idade"},
            new String[]{"O que é, o que é? Corre em volta do pasto todo sem sair do lugar.", "cerca"}
        ));

        perguntasPorFase.put(5, Arrays.asList(
            new String[]{"O que é, o que é? Fica cheio de buracos mas ainda segura água.", "esponja"},
            new String[]{"O que é, o que é? É seu, mas os outros usam mais que você.", "nome"},
            new String[]{"O que é, o que é? Está no meio do começo e do fim.", "e"}
        ));

        perguntasPorFase.put(6, Arrays.asList(
            new String[]{"O que é, o que é? Tem cidades, mas não tem casas. Tem rios, mas não tem água.", "mapa"},
            new String[]{"O que é, o que é? Pode encher uma sala inteira, mas não ocupa espaço.", "luz"},
            new String[]{"O que é, o que é? Nasce grande e morre pequeno.", "vela"},
            new String[]{"O que é, o que é? Quanto mais rápido você corre, mais ele fica para trás.", "rastro"},
            new String[]{"O que é, o que é? Não pode ser usado antes de quebrar.", "ovo"},
            new String[]{"O que é, o que é? Tem olhos mas não pode ver.", "batata"}
        ));
    }

    public int getQuantidadePerguntas(int faseAtual) {
        List<String[]> perguntas = perguntasPorFase.get(faseAtual);
        return perguntas != null ? perguntas.size() : 0;
    }

    public String getPergunta(int faseAtual, int perguntaAtual) {
        List<String[]> perguntas = perguntasPorFase.get(faseAtual);
        if (perguntas != null && perguntaAtual < perguntas.size()) {
            return perguntas.get(perguntaAtual)[0];
        }
        return "Pergunta não encontrada.";
    }

    public String getResposta(int faseAtual, int perguntaAtual) {
        List<String[]> perguntas = perguntasPorFase.get(faseAtual);
        if (perguntas != null && perguntaAtual < perguntas.size()) {
            return perguntas.get(perguntaAtual)[1];
        }
        return "";
    }

    public String[] getOpcoes(int faseAtual, int perguntaAtual) {
        String respostaCorreta = getResposta(faseAtual, perguntaAtual);
        Set<String> opcoes = new LinkedHashSet<>();
        opcoes.add(respostaCorreta);

        // Lista com todas as respostas menos a correta
        List<String> todasRespostas = new ArrayList<>();
        for (List<String[]> listaPerguntas : perguntasPorFase.values()) {
            for (String[] pergunta : listaPerguntas) {
                String resp = pergunta[1];
                if (!resp.equalsIgnoreCase(respostaCorreta)) {
                    todasRespostas.add(resp);
                }
            }
        }

        Collections.shuffle(todasRespostas);

        int i = 0;
        while (opcoes.size() < 5 && i < todasRespostas.size()) {
            opcoes.add(todasRespostas.get(i));
            i++;
        }

        // Preenche com string vazia se faltar opções
        while (opcoes.size() < 5) {
            opcoes.add("");
        }

        // Embaralha as opções
        List<String> opcoesList = new ArrayList<>(opcoes);
        Collections.shuffle(opcoesList);

        return opcoesList.toArray(new String[0]);
    }

    public List<String[]> getTodasPerguntas() {
        List<String[]> todasPerguntas = new ArrayList<>();
        for (List<String[]> perguntas : perguntasPorFase.values()) {
            todasPerguntas.addAll(perguntas);
        }
        return todasPerguntas;
    }

    public int getTotalFases() {
        return perguntasPorFase.size();
    }

    private Boolean ultimoResultado;

    public Boolean getUltimoResultado() {
        return ultimoResultado;
    }

    public void setUltimoResultado(Boolean resultado) {
        this.ultimoResultado = resultado;
    }
}




