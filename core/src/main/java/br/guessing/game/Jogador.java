package br.guessing.game;

import com.badlogic.gdx.graphics.Texture;

public class Jogador {
    private final String nome;
    private int acertosTotais;
    private final Texture avatarTexture;

    public Jogador(String nome, String avatarCaminho) {
        this.nome = nome;
        this.avatarTexture = new Texture(avatarCaminho);
        this.acertosTotais = 0;
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

    public void dispose() {
        avatarTexture.dispose();
    }

    public String getAvatar() {

        return "";
    }
}










