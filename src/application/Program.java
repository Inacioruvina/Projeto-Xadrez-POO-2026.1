package application;

import java.util.Scanner;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

/**
 * Ponto de entrada (Main) da aplicação do Sistema de Xadrez.
 * Controla o fluxo de execução principal do jogo de xadrez em terminal,
 * instanciando a partida e processando o loop de rodadas e captura de exceções.
 */
public class Program {

    /**
     * Ponto de entrada principal do programa.
     * 
     * @param args Argumentos de linha de comando (não utilizados).
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ChessMatch chessMatch = new ChessMatch();

        printWelcome();

        while (!chessMatch.getCheckMate()) {
            try {
                UI.printMatch(chessMatch, null);

                System.out.println();
                System.out.print(UI.ANSI_CYAN + "  Origem: " + UI.ANSI_RESET);
                ChessPosition source = UI.readChessPosition(sc);

                boolean[][] possibleMoves = chessMatch.possibleMoves(source);
                UI.printMatch(chessMatch, possibleMoves);

                System.out.println();
                System.out.print(UI.ANSI_CYAN + "  Destino: " + UI.ANSI_RESET);
                ChessPosition target = UI.readChessPosition(sc);

                ChessPiece capturedPiece = chessMatch.performChessMove(source, target);

                // Trata a promoção do Peão
                if (chessMatch.getPromoted() != null) {
                    System.out.print(UI.ANSI_PURPLE + "  Peão promovido! Escolha a peça (Q/R/B/N): " + UI.ANSI_RESET);
                    String type = sc.nextLine().toUpperCase();
                    while (!type.equals("Q") && !type.equals("R") && !type.equals("B") && !type.equals("N")) {
                        System.out.print(UI.ANSI_RED + "  Tipo inválido! Digite Q, R, B ou N: " + UI.ANSI_RESET);
                        type = sc.nextLine().toUpperCase();
                    }
                    chessMatch.replacePromotedPiece(type);
                }

            } catch (ChessException e) {
                System.out.println(UI.ANSI_RED + "\n  ⚠ Erro: " + e.getMessage() + UI.ANSI_RESET);
                System.out.println(UI.ANSI_CYAN + "  Pressione Enter para continuar..." + UI.ANSI_RESET);
                sc.nextLine();
            } catch (Exception e) {
                System.out.println(UI.ANSI_RED + "\n  ⚠ Erro: " + e.getMessage() + UI.ANSI_RESET);
                System.out.println(UI.ANSI_CYAN + "  Pressione Enter para continuar..." + UI.ANSI_RESET);
                sc.nextLine();
            }
        }

        UI.printMatch(chessMatch, null);
        sc.close();
    }

    /**
     * Imprime a tela de boas-vindas com as instruções básicas de jogo.
     */
    private static void printWelcome() {
        System.out.println(UI.ANSI_CYAN);
        System.out.println("  ╔══════════════════════════════════════════╗");
        System.out.println("  ║         SISTEMA DE XADREZ EM CAMADAS     ║");
        System.out.println("  ║       Programação Orientada a Objetos    ║");
        System.out.println("  ╠══════════════════════════════════════════╣");
        System.out.println("  ║  Como jogar:                             ║");
        System.out.println("  ║  • Digite a posição no formato: a1, e2   ║");
        System.out.println("  ║  • " + UI.ANSI_WHITE + "Brancas" + UI.ANSI_CYAN + ": letras MAIÚSCULAS no tabuleiro  ║");
        System.out.println("  ║  • " + UI.ANSI_YELLOW + "Pretas" + UI.ANSI_CYAN + ":  letras minúsculas no tabuleiro  ║");
        System.out.println("  ║  • Destinos possíveis marcados em " + UI.ANSI_BLUE_BACKGROUND + "azul" + UI.ANSI_RESET + UI.ANSI_CYAN + "    ║");
        System.out.println("  ╚══════════════════════════════════════════╝");
        System.out.println(UI.ANSI_RESET);
    }
}

