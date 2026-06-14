package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

/**
 * Representa a peça Cavalo (Knight) no jogo de xadrez.
 * O Cavalo se move em formato de "L" (duas casas em uma direção e uma casa perpendicular)
 * e possui a capacidade única de pular sobre outras peças.
 */
public class Knight extends ChessPiece {

    /**
     * Construtor da peça Cavalo.
     * 
     * @param board Tabuleiro associado.
     * @param color Cor da peça (Branca ou Preta).
     */
    public Knight(Board board, Color color) {
        super(board, color);
    }

    /**
     * Auxiliar para verificar se o Cavalo pode se mover para uma determinada posição.
     * Pode se mover se a casa estiver vazia ou se contiver uma peça adversária.
     */
    private boolean canMove(Position position) {
        ChessPiece p = (ChessPiece) getBoard().piece(position);
        return p == null || p.getColor() != getColor();
    }

    /**
     * Calcula todos os movimentos possíveis para o Cavalo.
     * Testa os 8 saltos em "L" a partir de deslocamentos fixos de linha/coluna.
     * 
     * @return Matriz booleana contendo os movimentos possíveis.
     */
    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];

        int[] rowOffsets = {-2, -2, -1, -1, 1, 1, 2, 2};
        int[] colOffsets = {-1, 1, -2, 2, -2, 2, -1, 1};

        Position p = new Position(0, 0);

        for (int i = 0; i < rowOffsets.length; i++) {
            p.setValues(position.getRow() + rowOffsets[i], position.getColumn() + colOffsets[i]);
            if (getBoard().positionExists(p) && canMove(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }
        }

        return mat;
    }

    /**
     * Retorna a notação em string da peça Cavalo.
     * 
     * @return "N" para representar o Cavalo (usado internacionalmente para evitar conflito com o King "K").
     */
    @Override
    public String toString() {
        return "N";
    }
}

