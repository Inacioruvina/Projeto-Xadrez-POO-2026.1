package application;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.Color;

/**
 * Interface de Usuário (UI) via console para o jogo de xadrez.
 * Fornece métodos para renderizar o tabuleiro de xadrez em cores ANSI no terminal,
 * ler as entradas do jogador e exibir informações de turnos, peças capturadas, xeque e xeque-mate.
 */
public class UI {

    // Códigos ANSI para cores de texto no terminal
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    // Códigos ANSI para cores de fundo no terminal
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    /**
     * Limpa a tela do console utilizando comandos ANSI.
     * Útil para manter a interface limpa a cada turno.
     */
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Lê uma posição informada pelo usuário no formato de xadrez (ex: e2).
     * 
     * @param sc Scanner para ler a entrada do console.
     * @return Um objeto ChessPosition correspondente à entrada.
     * @throws chess.ChessException Se o formato for inválido.
     */
    public static ChessPosition readChessPosition(Scanner sc) {
        try {
            String s = sc.nextLine();
            char column = s.charAt(0);
            int row = Integer.parseInt(s.substring(1));
            return new ChessPosition(column, row);
        } catch (RuntimeException e) {
            throw new chess.ChessException("Erro ao ler posição: valores válidos são de a1 a h8.");
        }
    }

    /**
     * Imprime o estado completo da partida de xadrez, incluindo tabuleiro,
     * peças capturadas, turno corrente e status (xeque, xeque-mate ou aguardando jogada).
     * 
     * @param chessMatch A partida ativa de xadrez.
     * @param possibleMoves Matriz com os movimentos possíveis destacados na tela.
     */
    public static void printMatch(ChessMatch chessMatch, boolean[][] possibleMoves) {
        printBoard(chessMatch.getPieces(), possibleMoves);
        System.out.println();
        printCapturedPieces(chessMatch);
        System.out.println();
        printTurnInfo(chessMatch);
    }

    /**
     * Auxiliar para imprimir os dados do turno atual e o status da partida.
     */
    private static void printTurnInfo(ChessMatch chessMatch) {
        if (!chessMatch.getCheckMate()) {
            System.out.println(ANSI_CYAN + "┌─────────────────────────────┐" + ANSI_RESET);
            System.out.printf(ANSI_CYAN + "│" + ANSI_RESET + " Turno: %-22d" + ANSI_CYAN + "│%n" + ANSI_RESET, chessMatch.getTurn());
            if (chessMatch.getCurrentPlayer() == Color.WHITE) {
                System.out.printf(ANSI_CYAN + "│" + ANSI_RESET + " Aguardando: " + ANSI_WHITE + "%-16s" + ANSI_RESET + ANSI_CYAN + "│%n" + ANSI_RESET, "BRANCAS");
            } else {
                System.out.printf(ANSI_CYAN + "│" + ANSI_RESET + " Aguardando: " + ANSI_YELLOW + "%-16s" + ANSI_RESET + ANSI_CYAN + "│%n" + ANSI_RESET, "PRETAS");
            }
            if (chessMatch.getCheck()) {
                System.out.println(ANSI_CYAN + "│" + ANSI_RESET + ANSI_YELLOW + " *** XEQUE! ***             " + ANSI_RESET + ANSI_CYAN + "│" + ANSI_RESET);
            } else {
                System.out.println(ANSI_CYAN + "│                             │" + ANSI_RESET);
            }
            System.out.println(ANSI_CYAN + "└─────────────────────────────┘" + ANSI_RESET);
        } else {
            System.out.println(ANSI_RED + "╔═════════════════════════════╗" + ANSI_RESET);
            System.out.println(ANSI_RED + "║   *** XEQUE-MATE! ***       ║" + ANSI_RESET);
            if (chessMatch.getCurrentPlayer() == Color.WHITE) {
                System.out.println(ANSI_RED + "║   Vencedor: " + ANSI_WHITE + "BRANCAS        " + ANSI_RED + "║" + ANSI_RESET);
            } else {
                System.out.println(ANSI_RED + "║   Vencedor: " + ANSI_YELLOW + "PRETAS         " + ANSI_RED + "║" + ANSI_RESET);
            }
            System.out.println(ANSI_RED + "╚═════════════════════════════╝" + ANSI_RESET);
        }
    }

    /**
     * Imprime o tabuleiro de xadrez na tela sem destaques de movimentos possíveis.
     * 
     * @param pieces A matriz de peças de xadrez atuais do tabuleiro.
     */
    public static void printBoard(ChessPiece[][] pieces) {
        printBoard(pieces, null);
    }

    /**
     * Imprime o tabuleiro de xadrez destacando em azul as posições que são movimentos possíveis.
     * 
     * @param pieces A matriz de peças do tabuleiro.
     * @param possibleMoves Matriz de booleanos com as casas possíveis marcadas como true.
     */
    public static void printBoard(ChessPiece[][] pieces, boolean[][] possibleMoves) {
        System.out.println();
        System.out.println(ANSI_CYAN + "   ╔════════════════╗" + ANSI_RESET);
        for (int i = 0; i < pieces.length; i++) {
            System.out.print(ANSI_CYAN + " " + (8 - i) + " ║" + ANSI_RESET);
            for (int j = 0; j < pieces[i].length; j++) {
                printPiece(pieces[i][j], possibleMoves != null && possibleMoves[i][j]);
            }
            System.out.println(ANSI_CYAN + "║" + ANSI_RESET);
        }
        System.out.println(ANSI_CYAN + "   ╚════════════════╝" + ANSI_RESET);
        System.out.println(ANSI_CYAN + "    a b c d e f g h" + ANSI_RESET);
    }

    /**
     * Auxiliar para imprimir uma peça individualmente, aplicando cores conforme o lado (Branco ou Preto)
     * e alterando o fundo se for uma casa de destino viável.
     */
    private static void printPiece(ChessPiece piece, boolean possibleMove) {
        if (possibleMove) {
            System.out.print(ANSI_BLUE_BACKGROUND);
        }
        if (piece == null) {
            System.out.print("-" + ANSI_RESET);
        } else {
            if (piece.getColor() == Color.WHITE) {
                System.out.print(ANSI_WHITE + piece + ANSI_RESET);
            } else {
                System.out.print(ANSI_YELLOW + piece + ANSI_RESET);
            }
        }
        System.out.print(" ");
    }

    /**
     * Imprime na tela a listagem das peças que foram capturadas ao longo do jogo, separadas por cor.
     * 
     * @param chessMatch A partida ativa contendo a lista de capturas.
     */
    public static void printCapturedPieces(ChessMatch chessMatch) {
        List<ChessPiece> white = chessMatch.getCapturedPieces(Color.WHITE);
        List<ChessPiece> black = chessMatch.getCapturedPieces(Color.BLACK);

        System.out.println(ANSI_CYAN + "  Peças capturadas:" + ANSI_RESET);
        System.out.print("  " + ANSI_WHITE + "Brancas: [");
        System.out.print(white.stream().map(Object::toString).collect(Collectors.joining(", ")));
        System.out.println("]" + ANSI_RESET);

        System.out.print("  " + ANSI_YELLOW + "Pretas:  [");
        System.out.print(black.stream().map(Object::toString).collect(Collectors.joining(", ")));
        System.out.println("]" + ANSI_RESET);
    }
}

