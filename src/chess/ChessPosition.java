package chess;

import boardgame.Position;

/**
 * Representa uma posição no formato clássico de xadrez (notação algébrica).
 * As colunas são representadas por letras de 'a' a 'h' e as linhas de 1 a 8.
 * Realiza a conversão bidirecional entre ChessPosition e as coordenadas de matriz bidimensional (Position).
 */
public class ChessPosition {
    private char column;
    private int row;

    /**
     * Construtor para criar uma ChessPosition a partir de coluna e linha clássicas.
     * 
     * @param column Letra da coluna ('a' a 'h').
     * @param row Número da linha (1 a 8).
     * @throws ChessException Se os valores fornecidos estiverem fora do intervalo permitido.
     */
    public ChessPosition(char column, int row) {
        if (column < 'a' || column > 'h' || row < 1 || row > 8) {
            throw new ChessException("Erro ao instanciar ChessPosition: valores válidos são de a1 a h8.");
        }
        this.column = column;
        this.row = row;
    }

    /**
     * Obtém a coluna da posição (letra).
     * 
     * @return O caractere da coluna.
     */
    public char getColumn() {
        return column;
    }

    /**
     * Obtém a linha da posição (número).
     * 
     * @return O inteiro da linha.
     */
    public int getRow() {
        return row;
    }

    /**
     * Converte esta posição do xadrez clássico para a posição da matriz interna (0-indexada).
     * 
     * @return Um objeto Position contendo os índices correspondentes para a matriz.
     */
    public Position toPosition() {
        return new Position(8 - row, column - 'a');
    }

    /**
     * Cria uma ChessPosition correspondente a partir de uma posição interna de matriz.
     * 
     * @param position Objeto contendo os índices da matriz interna.
     * @return Um novo objeto ChessPosition correspondente.
     */
    public static ChessPosition fromPosition(Position position) {
        return new ChessPosition((char) ('a' + position.getColumn()), 8 - position.getRow());
    }

    /**
     * Retorna a posição em notação textual clássica (ex: "e4").
     * 
     * @return String de dois caracteres representando a posição de xadrez.
     */
    @Override
    public String toString() {
        return "" + column + row;
    }
}

