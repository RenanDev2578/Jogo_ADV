package br.guessing.game;

import com.badlogic.gdx.graphics.Texture;

public class Jogador {
    private final String nome;
    private int acertosTotais;
    private final Texture avatarTexture;
    private final String avatarFileName; // Guarda só o nome do arquivo

    public Jogador(String nome, String avatarFileName) {
        this.nome = nome;
        this.avatarFileName = avatarFileName;
        this.avatarTexture = new Texture("avatars/" + avatarFileName);
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
        return avatarFileName;
    }


    public String getAvatarPath() {
        return "avatars/" + avatarFileName;
    }
}
















