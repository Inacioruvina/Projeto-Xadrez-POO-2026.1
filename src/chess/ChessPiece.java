package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;

/**
 * Superclasse abstrata que representa uma peça no jogo de xadrez.
 * Herda da classe base Piece e adiciona propriedades específicas de xadrez, como cor e contagem de movimentos.
 */
public abstract class ChessPiece extends Piece {
    private Color color;
    private int moveCount;

    /**
     * Construtor da peça de xadrez, associando-a a um tabuleiro e definindo sua cor.
     * A contagem de movimentos inicia em 0 por padrão.
     * 
     * @param board O tabuleiro do jogo.
     * @param color A cor da peça (Branca ou Preta).
     */
    public ChessPiece(Board board, Color color) {
        super(board);
        this.color = color;
    }

    /**
     * Obtém a cor desta peça.
     * 
     * @return A cor da peça.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Obtém o número total de movimentos realizados por esta peça.
     * 
     * @return O número de movimentos.
     */
    public int getMoveCount() {
        return moveCount;
    }

    /**
     * Incrementa a contagem de movimentos em 1.
     * Chamado ao realizar um movimento com esta peça.
     */
    public void increaseMoveCount() {
        moveCount++;
    }

    /**
     * Decrementa a contagem de movimentos em 1.
     * Chamado ao desfazer um movimento.
     */
    public void decreaseMoveCount() {
        moveCount--;
    }

    /**
     * Obtém a posição atual da peça no formato clássico de xadrez.
     * 
     * @return A ChessPosition correspondente à posição interna da peça.
     */
    public ChessPosition getChessPosition() {
        return ChessPosition.fromPosition(position);
    }

    /**
     * Verifica se existe uma peça do adversário em uma determinada posição do tabuleiro.
     * Usado principalmente para validar possibilidades de captura.
     * 
     * @param position A posição a ser analisada.
     * @return true se houver uma peça inimiga na posição, false caso contrário.
     */
    protected boolean isThereOpponentPiece(Position position) {
        ChessPiece p = (ChessPiece) getBoard().piece(position);
        return p != null && p.getColor() != color;
    }
}

