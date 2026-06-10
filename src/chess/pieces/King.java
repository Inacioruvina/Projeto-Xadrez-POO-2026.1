package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

/**
 * Representa a peça Rei (King) no jogo de xadrez.
 * O Rei se move uma casa em qualquer direção. Também participa da jogada especial
 * de Roque (Castling), desde que atenda a requisitos específicos.
 */
public class King extends ChessPiece {
    private ChessMatch chessMatch;

    /**
     * Construtor da peça Rei.
     * 
     * @param board Tabuleiro associado.
     * @param color Cor da peça (Branca ou Preta).
     * @param chessMatch Partida de xadrez associada (necessária para validar roque e xeque).
     */
    public King(Board board, Color color, ChessMatch chessMatch) {
        super(board, color);
        this.chessMatch = chessMatch;
    }

    /**
     * Auxiliar para verificar se o Rei pode se mover para uma determinada posição.
     * 
     * @param position Posição a testar.
     * @return true se a casa estiver vazia ou tiver peça adversária, false caso contrário.
     */
    private boolean canMove(Position position) {
        ChessPiece p = (ChessPiece) getBoard().piece(position);
        return p == null || p.getColor() != getColor();
    }

    /**
     * Auxiliar para testar se uma Torre é elegível para a jogada de Roque.
     * A Torre deve estar na posição correta, ter a mesma cor do Rei e não ter se movido na partida.
     */
    private boolean testRookCastling(Position position) {
        ChessPiece p = (ChessPiece) getBoard().piece(position);
        return p instanceof Rook && p.getColor() == getColor() && p.getMoveCount() == 0;
    }

    /**
     * Calcula todos os movimentos possíveis para o Rei.
     * Inclui as 8 casas adjacentes e as jogadas especiais de Roque Pequeno (ala do rei)
     * e Roque Grande (ala da rainha).
     * 
     * @return Matriz booleana contendo as jogadas válidas.
     */
    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
        Position p = new Position(0, 0);

        int[] rowOffsets = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] colOffsets = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < rowOffsets.length; i++) {
            p.setValues(position.getRow() + rowOffsets[i], position.getColumn() + colOffsets[i]);
            if (getBoard().positionExists(p) && canMove(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }
        }

        // Movimento especial: Roque (Castling)
        if (getMoveCount() == 0 && !chessMatch.getCheck()) {
            // Roque Pequeno (ala do Rei)
            Position posT1 = new Position(position.getRow(), position.getColumn() + 3);
            if (testRookCastling(posT1)) {
                Position p1 = new Position(position.getRow(), position.getColumn() + 1);
                Position p2 = new Position(position.getRow(), position.getColumn() + 2);
                if (getBoard().piece(p1) == null && getBoard().piece(p2) == null) {
                    mat[position.getRow()][position.getColumn() + 2] = true;
                }
            }

            // Roque Grande (ala da Rainha)
            Position posT2 = new Position(position.getRow(), position.getColumn() - 4);
            if (testRookCastling(posT2)) {
                Position p1 = new Position(position.getRow(), position.getColumn() - 1);
                Position p2 = new Position(position.getRow(), position.getColumn() - 2);
                Position p3 = new Position(position.getRow(), position.getColumn() - 3);
                if (getBoard().piece(p1) == null && getBoard().piece(p2) == null && getBoard().piece(p3) == null) {
                    mat[position.getRow()][position.getColumn() - 2] = true;
                }
            }
        }

        return mat;
    }

    /**
     * Retorna a notação em string da peça Rei.
     * 
     * @return "K" para representar o Rei.
     */
    @Override
    public String toString() {
        return "K";
    }
}

