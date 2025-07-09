package br.guessing.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Ranking {

    private final List<Jogador> jogadores = new ArrayList<>();

    public void adicionarOuAtualizarJogador(Jogador jogador) {
        // Remove o jogador se já existir para evitar duplicatas
        jogadores.removeIf(j -> j.getNome().equals(jogador.getNome()));
        jogadores.add(jogador);
    }

    public List<Jogador> getJogadoresOrdenados() {
        List<Jogador> copia = new ArrayList<>(jogadores);
        copia.sort(Comparator.comparingInt(Jogador::getPontuacaoTotal).reversed());
        return copia;
    }

    public Jogador getTopJogador(int posicao) {
        List<Jogador> ordenados = getJogadoresOrdenados();
        if (posicao >= 1 && posicao <= ordenados.size()) {
            return ordenados.get(posicao - 1);
        }
        return null;
    }

    public List<Jogador> getTopN(int n) {
        List<Jogador> ordenados = getJogadoresOrdenados();
        return ordenados.subList(0, Math.min(n, ordenados.size()));
    }
}
