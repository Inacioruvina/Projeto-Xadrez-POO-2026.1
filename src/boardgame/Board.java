package boardgame;

/**
 * Representa um tabuleiro genérico composto por uma matriz bidimensional de peças.
 * Gerencia a colocação, remoção e verificação de validade das posições no tabuleiro.
 */
public class Board {
    private int rows;
    private int columns;
    private Piece[][] pieces;

    /**
     * Construtor para criar um tabuleiro com dimensões específicas.
     * 
     * @param rows Quantidade de linhas (deve ser no mínimo 1).
     * @param columns Quantidade de colunas (deve ser no mínimo 1).
     * @throws BoardException Se as dimensões forem menores que 1.
     */
    public Board(int rows, int columns) {
        if (rows < 1 || columns < 1) {
            throw new BoardException("Erro ao criar tabuleiro: deve haver ao menos 1 linha e 1 coluna.");
        }
        this.rows = rows;
        this.columns = columns;
        pieces = new Piece[rows][columns];
    }

    /**
     * Obtém a quantidade total de linhas do tabuleiro.
     * 
     * @return O número de linhas.
     */
    public int getRows() {
        return rows;
    }

    /**
     * Obtém a quantidade total de colunas do tabuleiro.
     * 
     * @return O número de colunas.
     */
    public int getColumns() {
        return columns;
    }

    /**
     * Retorna a peça presente na linha e coluna especificadas.
     * 
     * @param row A linha da peça.
     * @param column A coluna da peça.
     * @return A peça na posição dada, ou null se não houver peça.
     * @throws BoardException Se a posição informada for inválida.
     */
    public Piece piece(int row, int column) {
        if (!positionExists(row, column)) {
            throw new BoardException("Posição fora do tabuleiro.");
        }
        return pieces[row][column];
    }

    /**
     * Retorna a peça presente na posição especificada.
     * 
     * @param position O objeto de Posição contendo as coordenadas.
     * @return A peça na posição dada, ou null se não houver peça.
     * @throws BoardException Se a posição informada for inválida.
     */
    public Piece piece(Position position) {
        return piece(position.getRow(), position.getColumn());
    }

    /**
     * Coloca uma peça em uma posição específica do tabuleiro.
     * 
     * @param piece A peça a ser colocada.
     * @param position A posição onde a peça será inserida.
     * @throws BoardException Se já houver outra peça no local escolhido.
     */
    public void placePiece(Piece piece, Position position) {
        if (thereIsAPiece(position)) {
            throw new BoardException("Já existe uma peça na posição " + position);
        }
        pieces[position.getRow()][position.getColumn()] = piece;
        piece.position = position;
    }

    /**
     * Remove uma peça de uma posição específica e zera sua referência de posição.
     * 
     * @param position A posição da peça a ser removida.
     * @return A peça removida, ou null se a posição já estivesse vazia.
     * @throws BoardException Se a posição informada for inválida.
     */
    public Piece removePiece(Position position) {
        if (!positionExists(position)) {
            throw new BoardException("Posição fora do tabuleiro.");
        }
        if (piece(position) == null) {
            return null;
        }
        Piece aux = piece(position);
        aux.position = null;
        pieces[position.getRow()][position.getColumn()] = null;
        return aux;
    }

    /**
     * Verifica internamente se uma linha e coluna estão dentro das fronteiras do tabuleiro.
     * 
     * @param row O índice de linha.
     * @param column O índice de coluna.
     * @return true se a coordenada for válida, false caso contrário.
     */
    private boolean positionExists(int row, int column) {
        return row >= 0 && row < rows && column >= 0 && column < columns;
    }

    /**
     * Verifica publicamente se uma posição existe no tabuleiro.
     * 
     * @param position O objeto de Posição a verificar.
     * @return true se a posição for válida, false caso contrário.
     */
    public boolean positionExists(Position position) {
        return positionExists(position.getRow(), position.getColumn());
    }

    /**
     * Verifica se existe alguma peça em uma determinada posição.
     * 
     * @param position A posição a ser verificada.
     * @return true se houver uma peça na posição, false caso contrário.
     * @throws BoardException Se a posição informada for inválida.
     */
    public boolean thereIsAPiece(Position position) {
        if (!positionExists(position)) {
            throw new BoardException("Posição fora do tabuleiro.");
        }
        return piece(position) != null;
    }
}

