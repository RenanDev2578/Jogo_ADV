package br.guessing.game;



import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite seu nome, jogador: ");
        String nome = scanner.nextLine();

        Facede GameFacade = new Facede(nome);
        GameFacade.iniciar();

        scanner.close();
    }
}



