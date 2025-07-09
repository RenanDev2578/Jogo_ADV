package br.guessing.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RankingManager {
    private static final int MAX_PLAYERS = 3;
    private static final String RANKING_FILE = "ranking.json";
    private final List<Jogador> jogadoresRanking;

    public RankingManager() {
        this.jogadoresRanking = new ArrayList<>();
        loadRanking();
    }
    // Remove o jogador se já existir para evitar duplicatas e atualizar a pontuação
    public void adicionarOuAtualizarJogador(Jogador jogador) {

        jogadoresRanking.removeIf(j -> j.getNome().equals(jogador.getNome()));
        jogadoresRanking.add(jogador);
        ordenarRanking();
        if (jogadoresRanking.size() > MAX_PLAYERS) {
            jogadoresRanking.subList(MAX_PLAYERS, jogadoresRanking.size()).clear();
        }

    }

    private void ordenarRanking() {
        jogadoresRanking.sort(Comparator.comparingInt(Jogador::getPontuacaoTotal).reversed());
    }

    public List<Jogador> getTopJogadores() {
        return Collections.unmodifiableList(jogadoresRanking);
    }

    // Método público para salvar o ranking
    public void saveRanking() {
        try {
            Json json = new Json();
            json.setOutputType(JsonWriter.OutputType.json);
            FileHandle file = Gdx.files.local(RANKING_FILE);


            Array<RankingEntry> entries = new Array<>();
            for (Jogador j : jogadoresRanking) {
                entries.add(new RankingEntry(j.getNome(), j.getPontuacaoTotal(), j.getAvatar()));
            }

            file.writeString(json.toJson(entries), false);
            Gdx.app.log("RankingManager", "Ranking salvo com sucesso!");
        } catch (Exception e) {
            Gdx.app.error("RankingManager", "Erro ao salvar ranking: " + e.getMessage());
        }
    }
    // Criar jogador temporário apenas para o ranking
    private void loadRanking() {
        FileHandle file = Gdx.files.local(RANKING_FILE);
        if (file.exists()) {
            try {
                Json json = new Json();
                Array<RankingEntry> entries = json.fromJson(Array.class, RankingEntry.class, file.readString());

                for (RankingEntry entry : entries) {

                    Jogador jogador = new Jogador(entry.nome, entry.avatar);
                    jogador.adicionarPontuacao(entry.pontuacao);
                    jogadoresRanking.add(jogador);
                }

                ordenarRanking();
                Gdx.app.log("RankingManager", "Ranking carregado com " + jogadoresRanking.size() + " jogadores");
            } catch (Exception e) {
                Gdx.app.error("RankingManager", "Erro ao carregar ranking: " + e.getMessage());
            }
        }
    }

    // Classe auxiliar para serialização JSON
    public static class RankingEntry {
        public String nome;
        public int pontuacao;
        public String avatar;

        public RankingEntry() {}

        public RankingEntry(String nome, int pontuacao, String avatar) {
            this.nome = nome;
            this.pontuacao = pontuacao;
            this.avatar = avatar;
        }
    }
}

