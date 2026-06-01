package boardgame;

/**
 * Exceção personalizada para representar erros ocorridos na camada de tabuleiro (Board Layer).
 * Esta exceção herda de RuntimeException para não obrigar a captura explícita.
 */
public class BoardException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Construtor que recebe uma mensagem detalhando o erro ocorrido.
     * 
     * @param msg Mensagem de erro.
     */
    public BoardException(String msg) {
        super(msg);
    }
}

