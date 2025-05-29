package br.guessing.game;

import java.util.*;

public class Advinha {

    private final Map<Integer, List<Pergunta>> perguntasPorFase;
    private Boolean ultimoResultado;

    public Advinha() {
        perguntasPorFase = new HashMap<>();

        perguntasPorFase.put(1, Arrays.asList(
            new Pergunta("O que é, o que é? Anda com os pés na cabeça.", "piolho",
                Arrays.asList("pulga", "carrapato", "barata", "formiga")),
            new Pergunta("O que é, o que é? Quanto mais se tira, maior fica.", "buraco",
                Arrays.asList("vala", "cova", "fenda", "cratera")),
            new Pergunta("O que é, o que é? Tem dentes mas não morde.", "pente",
                Arrays.asList("serrote", "garfo", "chave", "escova"))
        ));

        perguntasPorFase.put(2, Arrays.asList(
            new Pergunta("O que é, o que é? Entra na água e não se molha.", "sombra",
                Arrays.asList("reflexo", "eco", "luz", "pensamento")),
            new Pergunta("O que é, o que é? Sempre cai, mas nunca se machuca.", "chuva",
                Arrays.asList("neve", "granizo", "orvalho", "sereno")),
            new Pergunta("O que é, o que é? Tem asas, mas não voa, e um bico, mas não bica.", "bule",
                Arrays.asList("chaleira", "panela", "garrafa térmica", "cafeteira"))
        ));

        perguntasPorFase.put(3, Arrays.asList(
            new Pergunta("O que é, o que é? Quanto mais se tira, mais aumenta.", "buraco",
                Arrays.asList("fenda", "cova", "cratera", "vala")),
            new Pergunta("O que é, o que é? Passa diante do sol e não faz sombra.", "vento",
                Arrays.asList("ar", "brisa", "perfume", "fumaça")),
            new Pergunta("O que é, o que é? Não se come, mas é bom para se mastigar.", "goma",
                Arrays.asList("chiclete", "resina", "cola", "isopor"))
        ));

        perguntasPorFase.put(4, Arrays.asList(
            new Pergunta("O que é, o que é? Tem pescoço, mas não tem cabeça.", "garrafa",
                Arrays.asList("jarro", "vaso", "tubo", "frasco")),
            new Pergunta("O que é, o que é? Sobe, sobe, mas nunca desce.", "idade",
                Arrays.asList("tempo", "ano", "história", "experiência")),
            new Pergunta("O que é, o que é? Corre em volta do pasto todo sem sair do lugar.", "cerca",
                Arrays.asList("muro", "valeta", "arame", "trincheira"))
        ));

        perguntasPorFase.put(5, Arrays.asList(
            new Pergunta("O que é, o que é? Fica cheio de buracos mas ainda segura água.", "esponja",
                Arrays.asList("bucha", "peneira", "rede", "filtro")),
            new Pergunta("O que é, o que é? É seu, mas os outros usam mais que você.", "nome",
                Arrays.asList("apelido", "identidade", "assinatura", "marca")),
            new Pergunta("O que é, o que é? Está no meio do começo e do fim.", "e",
                Arrays.asList("letra", "meio", "sílaba", "espaço"))
        ));

        perguntasPorFase.put(6, Arrays.asList(
            new Pergunta("O que é, o que é? Tem cidades, mas não tem casas. Tem rios, mas não tem água.", "mapa",
                Arrays.asList("globo", "atlas", "GPS", "croqui")),
            new Pergunta("O que é, o que é? Pode encher uma sala inteira, mas não ocupa espaço.", "luz",
                Arrays.asList("som", "vento", "sombra", "brilho")),
            new Pergunta("O que é, o que é? Nasce grande e morre pequeno.", "vela",
                Arrays.asList("cigarro", "tocha", "fósforo", "isqueiro")),
            new Pergunta("O que é, o que é? Quanto mais rápido você corre, mais ele fica para trás.", "rastro",
                Arrays.asList("pegada", "sombra", "marca", "trilha")),
            new Pergunta("O que é, o que é? Não pode ser usado antes de quebrar.", "ovo",
                Arrays.asList("castanha", "caixa-surpresa", "segredo", "lacre")),
            new Pergunta("O que é, o que é? Tem olhos mas não pode ver.", "batata",
                Arrays.asList("cebola", "cenoura", "inhame", "mandioca"))
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
        List<Pergunta> perguntas = perguntasPorFase.get(faseAtual);
        if (perguntas == null || perguntaAtual >= perguntas.size()) return new String[0];

        Pergunta pergunta = perguntas.get(perguntaAtual);
        String correta = pergunta.getRespostaCorreta();

        Set<String> opcoes = new LinkedHashSet<>();
        opcoes.add(correta);

        List<String> similares = new ArrayList<>(pergunta.getRespostasParecidas());
        Collections.shuffle(similares);
        int i = 0;
        while (opcoes.size() < 5 && i < similares.size()) {
            opcoes.add(similares.get(i));
            i++;
        }

        // Preencher com outras aleatórias se faltar
        if (opcoes.size() < 5) {
            List<String> todasRespostas = new ArrayList<>();
            for (List<Pergunta> listaPerguntas : perguntasPorFase.values()) {
                for (Pergunta p : listaPerguntas) {
                    String resp = p.getRespostaCorreta();
                    if (!resp.equalsIgnoreCase(correta) && !opcoes.contains(resp)) {
                        todasRespostas.add(resp);
                    }
                }
            }
            Collections.shuffle(todasRespostas);
            i = 0;
            while (opcoes.size() < 5 && i < todasRespostas.size()) {
                opcoes.add(todasRespostas.get(i));
                i++;
            }
        }

        List<String> listaFinal = new ArrayList<>(opcoes);
        Collections.shuffle(listaFinal);
        return listaFinal.toArray(new String[0]);
    }

    public int getTotalFases() {
        return perguntasPorFase.size();
    }

    public List<Pergunta> getPerguntasDaFase(int faseAtual) {
        return perguntasPorFase.getOrDefault(faseAtual, Collections.emptyList());
    }

    public Boolean getUltimoResultado() {
        return ultimoResultado;
    }

    public void setUltimoResultado(Boolean resultado) {
        this.ultimoResultado = resultado;
    }
}
