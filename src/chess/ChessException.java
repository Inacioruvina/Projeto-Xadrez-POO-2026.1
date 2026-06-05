package chess;

import boardgame.BoardException;

/**
 * Exceção personalizada para representar erros específicos nas regras e lógica de xadrez (Chess Layer).
 * Estende BoardException para unificar os erros sob o domínio do tabuleiro.
 */
public class ChessException extends BoardException {
    private static final long serialVersionUID = 1L;

    /**
     * Construtor que recebe uma mensagem detalhando o erro de xadrez ocorrido.
     * 
     * @param msg Mensagem de erro.
     */
    public ChessException(String msg) {
        super(msg);
    }
}

