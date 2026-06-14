package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

/**
 * Representa a peça Peão (Pawn) no jogo de xadrez.
 * O Peão se move uma casa para a frente (ou duas se for o primeiro movimento da peça).
 * Ele captura diagonalmente para a frente e possui suporte aos movimentos especiais de
 * En Passant e Promoção.
 */
public class Pawn extends ChessPiece {
    private ChessMatch chessMatch;

    /**
     * Construtor da peça Peão.
     * 
     * @param board Tabuleiro associado.
     * @param color Cor da peça (Branca ou Preta).
     * @param chessMatch Partida de xadrez associada (necessária para obter vulnerabilidade de En Passant).
     */
    public Pawn(Board board, Color color, ChessMatch chessMatch) {
        super(board, color);
        this.chessMatch = chessMatch;
    }

    /**
     * Calcula todos os movimentos possíveis para o Peão.
     * Leva em consideração a cor (direção do movimento), casas vazias à frente (movimento simples
     * ou duplo inicial), capturas diagonais de oponentes e jogadas especiais de En Passant.
     * 
     * @return Matriz booleana de movimentos possíveis.
     */
    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
        Position p = new Position(0, 0);

        if (getColor() == Color.WHITE) {
            // Branco move-se para cima (linhas diminuem)
            p.setValues(position.getRow() - 1, position.getColumn());
            if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }
            p.setValues(position.getRow() - 2, position.getColumn());
            Position p2 = new Position(position.getRow() - 1, position.getColumn());
            if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)
                    && getBoard().positionExists(p2) && !getBoard().thereIsAPiece(p2)
                    && getMoveCount() == 0) {
                mat[p.getRow()][p.getColumn()] = true;
            }
            // Captura diagonal esquerda
            p.setValues(position.getRow() - 1, position.getColumn() - 1);
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }
            // Captura diagonal direita
            p.setValues(position.getRow() - 1, position.getColumn() + 1);
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }

            // Movimento especial: En Passant Brancas
            if (position.getRow() == 3) {
                Position left = new Position(position.getRow(), position.getColumn() - 1);
                Position right = new Position(position.getRow(), position.getColumn() + 1);
                if (getBoard().positionExists(left)
                        && isThereOpponentPiece(left)
                        && getBoard().piece(left) == chessMatch.getEnPassantVulnerable()) {
                    mat[left.getRow() - 1][left.getColumn()] = true;
                }
                if (getBoard().positionExists(right)
                        && isThereOpponentPiece(right)
                        && getBoard().piece(right) == chessMatch.getEnPassantVulnerable()) {
                    mat[right.getRow() - 1][right.getColumn()] = true;
                }
            }
        } else {
            // Preto move-se para baixo (linhas aumentam)
            p.setValues(position.getRow() + 1, position.getColumn());
            if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }
            p.setValues(position.getRow() + 2, position.getColumn());
            Position p2 = new Position(position.getRow() + 1, position.getColumn());
            if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)
                    && getBoard().positionExists(p2) && !getBoard().thereIsAPiece(p2)
                    && getMoveCount() == 0) {
                mat[p.getRow()][p.getColumn()] = true;
            }
            // Captura diagonal esquerda
            p.setValues(position.getRow() + 1, position.getColumn() - 1);
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }
            // Captura diagonal direita
            p.setValues(position.getRow() + 1, position.getColumn() + 1);
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }

            // Movimento especial: En Passant Pretas
            if (position.getRow() == 4) {
                Position left = new Position(position.getRow(), position.getColumn() - 1);
                Position right = new Position(position.getRow(), position.getColumn() + 1);
                if (getBoard().positionExists(left)
                        && isThereOpponentPiece(left)
                        && getBoard().piece(left) == chessMatch.getEnPassantVulnerable()) {
                    mat[left.getRow() + 1][left.getColumn()] = true;
                }
                if (getBoard().positionExists(right)
                        && isThereOpponentPiece(right)
                        && getBoard().piece(right) == chessMatch.getEnPassantVulnerable()) {
                    mat[right.getRow() + 1][right.getColumn()] = true;
                }
            }
        }

        return mat;
    }

    /**
     * Retorna a notação em string da peça Peão.
     * 
     * @return "P" para representar o Peão.
     */
    @Override
    public String toString() {
        return "P";
    }
}

