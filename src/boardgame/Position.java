package boardgame;

/**
 * Representa uma posição simples em uma matriz bidimensional (linha e coluna).
 * É uma classe utilitária usada principalmente pela camada do tabuleiro.
 */
public class Position {
    private int row;
    private int column;

    /**
     * Construtor para instanciar uma posição com linha e coluna especificadas.
     * 
     * @param row Linha da posição na matriz (0-indexada).
     * @param column Coluna da posição na matriz (0-indexada).
     */
    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Obtém o índice da linha.
     * 
     * @return O índice da linha.
     */
    public int getRow() {
        return row;
    }

    /**
     * Define o índice da linha.
     * 
     * @param row Novo índice de linha.
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Obtém o índice da coluna.
     * 
     * @return O índice da coluna.
     */
    public int getColumn() {
        return column;
    }

    /**
     * Define o índice da coluna.
     * 
     * @param column Novo índice de coluna.
     */
    public void setColumn(int column) {
        this.column = column;
    }

    /**
     * Atualiza os valores da linha e da coluna de uma só vez.
     * Útil para reuso de objetos de posição e otimização.
     * 
     * @param row Nova linha.
     * @param column Nova coluna.
     */
    public void setValues(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Retorna a representação textual da posição no formato "linha, coluna".
     * 
     * @return String contendo os valores da posição.
     */
    @Override
    public String toString() {
        return row + ", " + column;
    }
}

