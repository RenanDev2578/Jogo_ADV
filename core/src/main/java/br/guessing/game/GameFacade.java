package br.guessing.game;

public class GameFacade {
    private GuessMaster game;
    private Jogador jogadorAtual;  // só guarda o jogador real com avatar
    private int faseAtual = 1;

    public GameFacade(GuessMaster game) {
        this.game = game;
    }

    public void iniciarJogo() {
        game.setScreen(new CharacterSelectionScreen(game, this));
    }

    public void iniciarFaseComJogador(Jogador jogador) {
        this.jogadorAtual = jogador;
        faseAtual = 1;
        game.setScreen(new GuessingGameScreen(game, this, jogadorAtual, new Advinha(), faseAtual));
    }

    public void mostrarFaseCompleta(int acertos, int total, int proximaFase) {
        // Se precisar do jogador, passe também aqui
        game.setScreen(new FaseCompletaScreen(game, acertos, total, proximaFase));
    }

    public void trocarParaProximaFase(int acertosDaFase, int totalNaFase) {
        faseAtual++;
        if (faseAtual <= new Advinha().getTotalFases()) {
            game.setScreen(new GuessingGameScreen(game, this, jogadorAtual, new Advinha(), faseAtual));
        } else {
            game.setScreen(new VictoryScreen(game, jogadorAtual.getAcertosTotais(), 18));
        }
    }

    public void trocarParaGameOver(int acertosDaFase, int totalNaFase) {
        game.setScreen(new GameOverScreen(game, acertosDaFase, totalNaFase,jogadorAtual));

    }

    public Jogador getJogador() {
        return jogadorAtual;
    }

    public void trocarParaVictory() {
        game.setScreen(new VictoryScreen(game, jogadorAtual.getAcertosTotais(), 0));
    }
}


