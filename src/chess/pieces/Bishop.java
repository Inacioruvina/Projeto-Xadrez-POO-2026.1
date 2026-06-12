package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

/**
 * Representa a peça Bispo (Bishop) no jogo de xadrez.
 * O Bispo se move em linhas diagonais por qualquer número de casas livres.
 */
public class Bishop extends ChessPiece {

    /**
     * Construtor da peça Bispo.
     * 
     * @param board Tabuleiro associado.
     * @param color Cor da peça (Branca ou Preta).
     */
    public Bishop(Board board, Color color) {
        super(board, color);
    }

    /**
     * Calcula todos os movimentos possíveis para o Bispo.
     * O Bispo varre as quatro diagonais (noroeste, nordeste, sudoeste, sudeste) até encontrar
     * limites do tabuleiro ou outras peças.
     * 
     * @return Matriz booleana de movimentos possíveis.
     */
    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
        Position p = new Position(0, 0);

        // Direção: Noroeste (NW)
        p.setValues(position.getRow() - 1, position.getColumn() - 1);
        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
            p.setValues(p.getRow() - 1, p.getColumn() - 1);
        }
        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        // Direção: Nordeste (NE)
        p.setValues(position.getRow() - 1, position.getColumn() + 1);
        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
            p.setValues(p.getRow() - 1, p.getColumn() + 1);
        }
        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        // Direção: Sudoeste (SW)
        p.setValues(position.getRow() + 1, position.getColumn() - 1);
        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
            p.setValues(p.getRow() + 1, p.getColumn() - 1);
        }
        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        // Direção: Sudeste (SE)
        p.setValues(position.getRow() + 1, position.getColumn() + 1);
        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
            p.setValues(p.getRow() + 1, p.getColumn() + 1);
        }
        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        return mat;
    }

    /**
     * Retorna a notação em string da peça Bispo.
     * 
     * @return "B" para representar o Bispo.
     */
    @Override
    public String toString() {
        return "B";
    }
}

