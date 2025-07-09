package br.guessing.game;

import com.badlogic.gdx.graphics.Texture;
import java.util.EnumMap;
import java.util.Map;

public class Jogador {
    private final String nome;
    private final Texture avatarTexture;
    private final String avatarFileName;
    private int acertosTotais = 0;
    private int pontuacaoTotal = 0;

    /* mapa de recompensas por tipo*/
    private Map<TipoRecompensa, Integer> recompensas = new EnumMap<>(TipoRecompensa.class);

    public Jogador(String nome, String avatarFileName) {
        this.nome = nome;
        this.avatarFileName = avatarFileName;
        this.avatarTexture = new Texture("avatars/" + avatarFileName);
        for (TipoRecompensa tipo : TipoRecompensa.values()) {
            recompensas.put(tipo, 0);
        }
    }

    public String getNome() {
        return nome;
    }

    public Texture getAvatarTexture() {
        return avatarTexture;
    }

    public int getAcertosTotais() {
        return acertosTotais;
    }

    public void adicionarAcertos(int quantidade) {
        this.acertosTotais += quantidade;
    }

    public void adicionarPontuacao(int pontos) {
        this.pontuacaoTotal += pontos;
    }

    public int getPontuacaoTotal() {
        return pontuacaoTotal;
    }

    public void dispose() {
        avatarTexture.dispose();
    }

    public String getAvatar() {
        return avatarFileName;
    }

    public String getAvatarPath() {
        return "avatars/" + avatarFileName;
    }

  /*------------------------------------------------------*/
    // Métodos de recompensa


    public void adicionarRecompensa(TipoRecompensa tipo) {
        recompensas.put(tipo, recompensas.getOrDefault(tipo, 0) + 1);
    }

    public boolean temRecompensa(TipoRecompensa tipo) {
        return recompensas.getOrDefault(tipo, 0) > 0;
    }

    public boolean usarRecompensa(TipoRecompensa tipo) {
        int qtd = recompensas.getOrDefault(tipo, 0);
        if (qtd > 0) {
            recompensas.put(tipo, qtd - 1);
            return true;
        }
        return false;
    }

    public int getQtdRecompensa(TipoRecompensa tipo) {
        return recompensas.getOrDefault(tipo, 0);
    }

    public Map<TipoRecompensa, Integer> getRecompensas() {
        return recompensas;
    }
}
