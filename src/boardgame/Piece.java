package boardgame;

/**
 * Representa uma peça genérica de jogo de tabuleiro.
 * Esta é uma classe abstrata que serve como base para peças específicas de xadrez.
 */
public abstract class Piece {
    protected Position position;
    private Board board;

    /**
     * Construtor que associa a peça a um tabuleiro específico.
     * Por padrão, a peça inicia fora do tabuleiro (posição nula).
     * 
     * @param board Tabuleiro ao qual a peça pertence.
     */
    public Piece(Board board) {
        this.board = board;
        position = null;
    }

    /**
     * Retorna o tabuleiro associado a esta peça.
     * Apenas classes filhas e o próprio pacote podem acessar o tabuleiro diretamente.
     * 
     * @return O tabuleiro associado.
     */
    protected Board getBoard() {
        return board;
    }

    /**
     * Método abstrato que define todos os movimentos possíveis da peça no tabuleiro.
     * Cada peça concreta deve fornecer sua própria regra de movimentos.
     * 
     * @return Matriz booleana onde as posições marcadas como true são destinos válidos.
     */
    public abstract boolean[][] possibleMoves();

    /**
     * Verifica se é possível mover a peça para uma posição específica.
     * 
     * @param position A posição de destino a ser verificada.
     * @return true se o movimento for possível, false caso contrário.
     */
    public boolean possibleMove(Position position) {
        return possibleMoves()[position.getRow()][position.getColumn()];
    }

    /**
     * Verifica se existe pelo menos um movimento possível para esta peça.
     * Útil para validar se a peça não está bloqueada ou sem opções de jogada.
     * 
     * @return true se houver algum movimento possível, false caso contrário.
     */
    public boolean isThereAnyPossibleMove() {
        boolean[][] mat = possibleMoves();
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat.length; j++) {
                if (mat[i][j]) {
                    return true;
                }
            }
        }
        return false;
    }
}

