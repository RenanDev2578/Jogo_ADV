package br.guessing.game;

import java.util.*;

public class Advinha {

    private final Map<Integer, List<Pergunta>> perguntasPorFase;

    public Advinha() {
        perguntasPorFase = new HashMap<>();

        perguntasPorFase.put(1, Arrays.asList(
            new Pergunta("O que é, o que é? Anda com os pés na cabeça.", "piolho", Arrays.asList("pulga", "lêndea", "piolho-do-corpo", "carrapato")),
            new Pergunta("O que é, o que é? Tem patas mas não anda, tem penas mas não voa.", "galinha", Arrays.asList("peru", "ema", "avestruz", "frango")),
            new Pergunta("O que é, o que é? Quanto mais se tira, maior fica.", "buraco", Arrays.asList("fossa", "vala", "fenda", "abismo"))
        ));

        perguntasPorFase.put(2, Arrays.asList(
            new Pergunta("O que é, o que é? Entra na água e não se molha.", "sombra", Arrays.asList("reflexo", "eco", "imagem", "espelho")),
            new Pergunta("O que é, o que é? Sempre cai, mas nunca se machuca.", "chuva", Arrays.asList("neve", "granizo", "orvalho", "sereno")),
            new Pergunta("O que é, o que é? Tem asas, mas não voa.", "ventilador", Arrays.asList("helicóptero quebrado", "baleia", "asa-delta desmontada", "relógio"))
        ));

        perguntasPorFase.put(3, Arrays.asList(
            new Pergunta("O que é, o que é? Passa diante do sol e não faz sombra.", "vento", Arrays.asList("calor", "luz", "ar", "fumaça")),
            new Pergunta("O que é, o que é? Vive no chão, mas nunca se suja.", "tapete", Arrays.asList("pano", "toalha", "capacho", "papelão")),
            new Pergunta("O que é, o que é? Está em pé, mas não anda. Está deitado, mas não dorme.", "letra", Arrays.asList("linha", "parágrafo", "sinal", "símbolo"))
        ));

        perguntasPorFase.put(4, Arrays.asList(
            new Pergunta("O que é, o que é? Tem pescoço, mas não tem cabeça.", "garrafa", Arrays.asList("vaso", "pote", "frasco", "bule")),
            new Pergunta("O que é, o que é? Sobe, sobe, mas nunca desce.", "idade", Arrays.asList("tempo", "calendário", "contagem", "número")),
            new Pergunta("O que é, o que é? Corre em volta do pasto todo sem sair do lugar.", "cerca", Arrays.asList("muro", "estrada", "rio", "barreira"))
        ));

        perguntasPorFase.put(5, Arrays.asList(
            new Pergunta("O que é, o que é? Fica cheio de buracos, mas ainda segura água.", "esponja", Arrays.asList("peneira molhada", "tecido", "toalha", "bucha")),
            new Pergunta("O que é, o que é? É seu, mas os outros usam mais que você.", "nome", Arrays.asList("apelido", "RG", "senha", "número")),
            new Pergunta("O que é, o que é? Está no meio do começo e do fim.", "e", Arrays.asList("meio", "nada", "vazio", "intervalo"))
        ));

        perguntasPorFase.put(6, Arrays.asList(
            new Pergunta("O que é, o que é? Tem cidades, mas não tem casas. Tem rios, mas não tem água.", "mapa", Arrays.asList("globo", "atlas", "planta", "GPS")),
            new Pergunta("O que é, o que é? Pode encher uma sala inteira, mas não ocupa espaço.", "luz", Arrays.asList("sombra", "cheiro", "eco", "vibração")),
            new Pergunta("O que é, o que é? Nasce grande e morre pequeno.", "vela", Arrays.asList("sabão", "chupeta", "pirulito", "incenso")),
            new Pergunta("O que é, o que é? Quanto mais rápido você corre, mais ele fica para trás.", "rastro", Arrays.asList("passo", "sombra", "vento", "eco")),
            new Pergunta("O que é, o que é? Não pode ser usado antes de quebrar.", "ovo", Arrays.asList("coco", "nozes", "ampola", "pílula")),
            new Pergunta("O que é, o que é? Tem olhos mas não pode ver.", "batata", Arrays.asList("agulha", "rede", "boneca", "cebola"))
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
