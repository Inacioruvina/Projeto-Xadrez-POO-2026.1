package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

/**
 * Representa a peça Torre (Rook) no jogo de xadrez.
 * A Torre se move em linhas retas horizontais e verticais por qualquer número de casas livres.
 */
public class Rook extends ChessPiece {

    /**
     * Construtor da peça Torre.
     * 
     * @param board Tabuleiro associado.
     * @param color Cor da peça (Branca ou Preta).
     */
    public Rook(Board board, Color color) {
        super(board, color);
    }

    /**
     * Calcula todos os movimentos possíveis para a Torre.
     * A Torre verifica posições ortogonais (norte, sul, leste, oeste) até encontrar as bordas do tabuleiro
     * ou outra peça.
     * 
     * @return Matriz booleana de movimentos possíveis.
     */
    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
        Position p = new Position(0, 0);

        // Direção: Acima
        p.setValues(position.getRow() - 1, position.getColumn());
        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
            p.setValues(p.getRow() - 1, p.getColumn());
        }
        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        // Direção: Esquerda
        p.setValues(position.getRow(), position.getColumn() - 1);
        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
            p.setValues(p.getRow(), p.getColumn() - 1);
        }
        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        // Direção: Direita
        p.setValues(position.getRow(), position.getColumn() + 1);
        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
            p.setValues(p.getRow(), p.getColumn() + 1);
        }
        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        // Direção: Abaixo
        p.setValues(position.getRow() + 1, position.getColumn());
        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
            p.setValues(p.getRow() + 1, p.getColumn());
        }
        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        return mat;
    }

    /**
     * Retorna a notação em string da peça Torre.
     * 
     * @return "R" para representar a Torre.
     */
    @Override
    public String toString() {
        return "R";
    }
}

