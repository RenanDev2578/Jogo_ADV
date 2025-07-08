package br.guessing.game;

import java.util.*;

public class Advinha {

    private final Map<Integer, List<Pergunta>> perguntasPorFase;

    public Advinha() {
        perguntasPorFase = new HashMap<>();

        perguntasPorFase.put(1, Arrays.asList(
            new Pergunta("O que é, o que é? Anda com os pés na cabeça.", "piolho", Arrays.asList("pulga", "carrapato", "lêndea", "formiga")),
            new Pergunta("O que é, o que é? Tem patas mas não anda, tem penas mas não voa.", "galinha", Arrays.asList("pato", "cisne", "avestruz", "peru")),
            new Pergunta("O que é, o que é? Quanto mais se tira, maior fica.", "buraco", Arrays.asList("montanha", "fosso", "cova", "abismo"))
        ));

        perguntasPorFase.put(2, Arrays.asList(
            new Pergunta("O que é, o que é? Entra na água e não se molha.", "sombra", Arrays.asList("espelho", "fumaça", "ar", "vapor")),
            new Pergunta("O que é, o que é? Sempre cai, mas nunca se machuca.", "chuva", Arrays.asList("neve", "granizo", "folha", "nuvem")),
            new Pergunta("O que é, o que é? Tem asas, mas não voa.", "baleia", Arrays.asList("peixe", "golfinho", "tubarão", "sereia"))
        ));

        perguntasPorFase.put(3, Arrays.asList(
            new Pergunta("O que é, o que é? Quanto mais se tira, mais aumenta.", "buraco", Arrays.asList("dívida", "fome", "poço", "cova")),
            new Pergunta("O que é, o que é? Passa diante do sol e não faz sombra.", "vento", Arrays.asList("luz", "onda", "nuvem", "fumaça")),
            new Pergunta("O que é, o que é? Não se come, mas é bom para se mastigar.", "goma", Arrays.asList("chiclete", "papel", "borracha", "plástico"))
        ));

        perguntasPorFase.put(4, Arrays.asList(
            new Pergunta("O que é, o que é? Tem pescoço, mas não tem cabeça.", "garrafa", Arrays.asList("vaso", "bule", "copo", "frasco")),
            new Pergunta("O que é, o que é? Sobe, sobe, mas nunca desce.", "idade", Arrays.asList("tempo", "ano", "ciclo", "vida")),
            new Pergunta("O que é, o que é? Corre em volta do pasto todo sem sair do lugar.", "cerca", Arrays.asList("muro", "barreira", "rio", "estrada"))
        ));

        perguntasPorFase.put(5, Arrays.asList(
            new Pergunta("O que é, o que é? Fica cheio de buracos mas ainda segura água.", "esponja", Arrays.asList("peneira", "pano", "balde", "filtro")),
            new Pergunta("O que é, o que é? É seu, mas os outros usam mais que você.", "nome", Arrays.asList("sobrenome", "apelido", "endereço", "cpf")),
            new Pergunta("O que é, o que é? Está no meio do começo e do fim.", "e", Arrays.asList("meio", "centro", "final", "início"))
        ));

        perguntasPorFase.put(6, Arrays.asList(
            new Pergunta("O que é, o que é? Tem cidades, mas não tem casas. Tem rios, mas não tem água.", "mapa", Arrays.asList("globo", "atlas", "livro", "cidade")),
            new Pergunta("O que é, o que é? Pode encher uma sala inteira, mas não ocupa espaço.", "luz", Arrays.asList("ar", "sombra", "som", "silêncio")),
            new Pergunta("O que é, o que é? Nasce grande e morre pequeno.", "vela", Arrays.asList("sabonete", "lápis", "fósforo", "barbante")),
            new Pergunta("O que é, o que é? Quanto mais rápido você corre, mais ele fica para trás.", "rastro", Arrays.asList("sombra", "poeira", "vento", "tempo")),
            new Pergunta("O que é, o que é? Não pode ser usado antes de quebrar.", "ovo", Arrays.asList("nozes", "copo", "vidro", "prato")),
            new Pergunta("O que é, o que é? Tem olhos mas não pode ver.", "batata", Arrays.asList("cebola", "milho", "pedra", "cacto"))
        ));
    }

    public int getQuantidadePerguntas(int faseAtual) {
        List<Pergunta> perguntas = perguntasPorFase.get(faseAtual);
        return perguntas != null ? perguntas.size() : 0;
    }

    public String getPergunta(int faseAtual, int perguntaAtual) {
        List<Pergunta> perguntas = perguntasPorFase.get(faseAtual);
        if (perguntas != null && perguntaAtual < perguntas.size()) {
            return perguntas.get(perguntaAtual).getTexto();
        }
        return "Pergunta não encontrada.";
    }

    public String getResposta(int faseAtual, int perguntaAtual) {
        List<Pergunta> perguntas = perguntasPorFase.get(faseAtual);
        if (perguntas != null && perguntaAtual < perguntas.size()) {
            return perguntas.get(perguntaAtual).getRespostaCorreta();
        }
        return "";
    }

    public String[] getOpcoes(int faseAtual, int perguntaAtual) {
        String respostaCorreta = getResposta(faseAtual, perguntaAtual);
        Set<String> opcoes = new LinkedHashSet<>();
        opcoes.add(respostaCorreta);

        List<Pergunta> currentPhaseQuestions = perguntasPorFase.get(faseAtual);
        if (currentPhaseQuestions != null && perguntaAtual < currentPhaseQuestions.size()) {
            List<String> similares = currentPhaseQuestions.get(perguntaAtual).getRespostasParecidas();
            for (String s : similares) {
                if (opcoes.size() < 5 && !s.equalsIgnoreCase(respostaCorreta)) {
                    opcoes.add(s);
                }
            }
        }

        if (opcoes.size() < 5) {
            List<String> extras = new ArrayList<>();
            for (List<Pergunta> lista : perguntasPorFase.values()) {
                for (Pergunta p : lista) {
                    if (!p.getRespostaCorreta().equalsIgnoreCase(respostaCorreta)) {
                        extras.add(p.getRespostaCorreta());
                    }
                    for (String s : p.getRespostasParecidas()) {
                        if (!s.equalsIgnoreCase(respostaCorreta)) {
                            extras.add(s);
                        }
                    }
                }
            }
            Collections.shuffle(extras);
            for (String s : extras) {
                if (opcoes.size() >= 5) break;
                opcoes.add(s);
            }
        }

        while (opcoes.size() < 5) opcoes.add("Opção Vazia");

        List<String> listaFinal = new ArrayList<>(opcoes);
        Collections.shuffle(listaFinal);
        return listaFinal.toArray(new String[0]);
    }

    public List<Pergunta> getTodasPerguntas() {
        List<Pergunta> todas = new ArrayList<>();
        for (List<Pergunta> lista : perguntasPorFase.values()) {
            todas.addAll(lista);
        }
        return todas;
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
