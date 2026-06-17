package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Queen;
import chess.pieces.Rook;

/**
 * Classe principal que gerencia as regras, turnos e estado de uma partida de xadrez.
 * Controla a movimentação de peças, xeque, xeque-mate, en passant, promoção e roque.
 */
public class ChessMatch {

    private int turn;
    private Color currentPlayer;
    private Board board;
    private boolean check;
    private boolean checkMate;
    private ChessPiece enPassantVulnerable;
    private ChessPiece promoted;

    private List<Piece> piecesOnTheBoard = new ArrayList<>();
    private List<Piece> capturedPieces = new ArrayList<>();

    /**
     * Inicializa uma nova partida de xadrez.
     * Cria um tabuleiro de 8x8, define o primeiro turno, define o jogador inicial como Branco
     * e realiza a disposição inicial das peças.
     */
    public ChessMatch() {
        board = new Board(8, 8);
        turn = 1;
        currentPlayer = Color.WHITE;
        initialSetup();
    }

    /**
     * Obtém o número do turno atual.
     * 
     * @return O número do turno.
     */
    public int getTurn() {
        return turn;
    }

    /**
     * Obtém o jogador atual (Branco ou Preto).
     * 
     * @return A cor do jogador do turno atual.
     */
    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Informa se a partida está em estado de xeque.
     * 
     * @return true se o jogador atual estiver em xeque, false caso contrário.
     */
    public boolean getCheck() {
        return check;
    }

    /**
     * Informa se a partida foi encerrada por xeque-mate.
     * 
     * @return true se houver xeque-mate, false caso contrário.
     */
    public boolean getCheckMate() {
        return checkMate;
    }

    /**
     * Obtém a peça vulnerável a um ataque "En Passant" nesta jogada.
     * 
     * @return A ChessPiece vulnerável, ou null se nenhuma for elegível.
     */
    public ChessPiece getEnPassantVulnerable() {
        return enPassantVulnerable;
    }

    /**
     * Obtém a peça que foi promovida no turno atual.
     * 
     * @return A ChessPiece promovida, ou null se não houver promoção.
     */
    public ChessPiece getPromoted() {
        return promoted;
    }

    /**
     * Retorna a matriz de peças de xadrez representando o estado atual do tabuleiro.
     * 
     * @return Matriz bidimensional contendo as peças de xadrez nas posições correspondentes.
     */
    public ChessPiece[][] getPieces() {
        ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                mat[i][j] = (ChessPiece) board.piece(i, j);
            }
        }
        return mat;
    }

    /**
     * Calcula e retorna os movimentos possíveis para uma peça em uma dada posição algébrica.
     * 
     * @param sourcePosition Posição de origem da peça.
     * @return Matriz booleana correspondente aos destinos válidos para a peça.
     * @throws ChessException Se a posição de origem for inválida ou não contiver uma peça do jogador atual.
     */
    public boolean[][] possibleMoves(ChessPosition sourcePosition) {
        Position position = sourcePosition.toPosition();
        validateSourcePosition(position);
        return board.piece(position).possibleMoves();
    }

    /**
     * Realiza um movimento de xadrez da posição de origem à posição de destino.
     * Valida as posições, verifica situações de xeque autoinduzidas, trata captura de peças,
     * promoções de peão, roque, en passant, atualiza turnos e verifica xeque/xeque-mate.
     * 
     * @param sourcePosition Posição de origem da peça.
     * @param targetPosition Posição de destino escolhida.
     * @return A peça capturada durante o movimento (se houver), ou null.
     * @throws ChessException Se o movimento for ilegal ou violar as regras do xadrez.
     */
    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();

        validateSourcePosition(source);
        validateTargetPosition(source, target);

        Piece capturedPiece = makeMove(source, target);

        if (testCheck(currentPlayer)) {
            undoMove(source, target, capturedPiece);
            throw new ChessException("Você não pode se colocar em xeque!");
        }

        ChessPiece movedPiece = (ChessPiece) board.piece(target);

        // Movimento especial: Promoção
        promoted = null;
        if (movedPiece instanceof Pawn) {
            if ((movedPiece.getColor() == Color.WHITE && target.getRow() == 0)
                    || (movedPiece.getColor() == Color.BLACK && target.getRow() == 7)) {
                promoted = (ChessPiece) board.piece(target);
                promoted = replacePromotedPiece("Q"); // Rainha por padrão
            }
        }

        check = testCheck(opponent(currentPlayer));

        if (testCheckMate(opponent(currentPlayer))) {
            checkMate = true;
        } else {
            nextTurn();
        }

        // Movimento especial: En Passant (marcar vulnerabilidade)
        if (movedPiece instanceof Pawn
                && (target.getRow() == source.getRow() - 2 || target.getRow() == source.getRow() + 2)) {
            enPassantVulnerable = movedPiece;
        } else {
            enPassantVulnerable = null;
        }

        return (ChessPiece) capturedPiece;
    }

    /**
     * Substitui a peça elegível à promoção por uma nova peça escolhida pelo jogador.
     * 
     * @param type Sigla da peça de destino para promoção ("B" para Bispo, "N" para Cavalo, "R" para Torre ou "Q" para Rainha).
     * @return A nova peça criada na posição de promoção.
     * @throws ChessException Se não houver peça a ser promovida.
     */
    public ChessPiece replacePromotedPiece(String type) {
        if (promoted == null) {
            throw new ChessException("Não há peça a ser promovida.");
        }
        if (!type.equals("B") && !type.equals("N") && !type.equals("R") && !type.equals("Q")) {
            return promoted;
        }

        Position pos = promoted.getChessPosition().toPosition();
        Piece p = board.removePiece(pos);
        piecesOnTheBoard.remove(p);

        ChessPiece newPiece = newPiece(type, promoted.getColor());
        board.placePiece(newPiece, pos);
        piecesOnTheBoard.add(newPiece);

        return newPiece;
    }

    /**
     * Fábrica auxiliar para criar peças baseadas em string/tipo e cor.
     */
    private ChessPiece newPiece(String type, Color color) {
        if (type.equals("B")) return new Bishop(board, color);
        if (type.equals("N")) return new Knight(board, color);
        if (type.equals("R")) return new Rook(board, color);
        return new Queen(board, color);
    }

    /**
     * Executa fisicamente um movimento na matriz do tabuleiro, incluindo movimentos especiais.
     * 
     * @param source Posição de origem da peça.
     * @param target Posição de destino da peça.
     * @return Peça que foi capturada (se aplicável).
     */
    private Piece makeMove(Position source, Position target) {
        ChessPiece p = (ChessPiece) board.removePiece(source);
        p.increaseMoveCount();

        Piece capturedPiece = board.removePiece(target);
        board.placePiece(p, target);

        if (capturedPiece != null) {
            piecesOnTheBoard.remove(capturedPiece);
            capturedPieces.add(capturedPiece);
        }

        // Movimento especial: Roque Pequeno (ala do rei)
        if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
            Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
            Position targetT = new Position(source.getRow(), source.getColumn() + 1);
            ChessPiece rook = (ChessPiece) board.removePiece(sourceT);
            board.placePiece(rook, targetT);
            rook.increaseMoveCount();
        }

        // Movimento especial: Roque Grande (ala da rainha)
        if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
            Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
            Position targetT = new Position(source.getRow(), source.getColumn() - 1);
            ChessPiece rook = (ChessPiece) board.removePiece(sourceT);
            board.placePiece(rook, targetT);
            rook.increaseMoveCount();
        }

        // Movimento especial: En Passant
        if (p instanceof Pawn) {
            if (source.getColumn() != target.getColumn() && capturedPiece == null) {
                Position pawnPosition;
                if (p.getColor() == Color.WHITE) {
                    pawnPosition = new Position(target.getRow() + 1, target.getColumn());
                } else {
                    pawnPosition = new Position(target.getRow() - 1, target.getColumn());
                }
                capturedPiece = board.removePiece(pawnPosition);
                capturedPieces.add(capturedPiece);
                piecesOnTheBoard.remove(capturedPiece);
            }
        }

        return capturedPiece;
    }

    /**
     * Desfaz um movimento realizado, reposicionando as peças e restaurando capturadas e contagens de movimentos.
     * 
     * @param source Posição original de origem.
     * @param target Posição de destino a ser revertida.
     * @param capturedPiece Peça capturada no movimento, a ser reinserida.
     */
    private void undoMove(Position source, Position target, Piece capturedPiece) {
        ChessPiece p = (ChessPiece) board.removePiece(target);
        p.decreaseMoveCount();
        board.placePiece(p, source);

        if (capturedPiece != null) {
            board.placePiece(capturedPiece, target);
            capturedPieces.remove(capturedPiece);
            piecesOnTheBoard.add(capturedPiece);
        }

        // Desfaz Roque Pequeno
        if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
            Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
            Position targetT = new Position(source.getRow(), source.getColumn() + 1);
            ChessPiece rook = (ChessPiece) board.removePiece(targetT);
            board.placePiece(rook, sourceT);
            rook.decreaseMoveCount();
        }

        // Desfaz Roque Grande
        if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
            Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
            Position targetT = new Position(source.getRow(), source.getColumn() - 1);
            ChessPiece rook = (ChessPiece) board.removePiece(targetT);
            board.placePiece(rook, sourceT);
            rook.decreaseMoveCount();
        }

        // Desfaz En Passant
        if (p instanceof Pawn) {
            if (source.getColumn() != target.getColumn() && capturedPiece == enPassantVulnerable) {
                ChessPiece pawn = (ChessPiece) board.removePiece(target);
                Position pawnPosition;
                if (p.getColor() == Color.WHITE) {
                    pawnPosition = new Position(3, target.getColumn());
                } else {
                    pawnPosition = new Position(4, target.getColumn());
                }
                board.placePiece(pawn, pawnPosition);
            }
        }
    }

    /**
     * Valida se a posição de origem de um movimento possui uma peça que pertence ao jogador atual.
     */
    private void validateSourcePosition(Position position) {
        if (!board.thereIsAPiece(position)) {
            throw new ChessException("Não existe peça na posição de origem.");
        }
        if (currentPlayer != ((ChessPiece) board.piece(position)).getColor()) {
            throw new ChessException("A peça escolhida não é sua.");
        }
        if (!board.piece(position).isThereAnyPossibleMove()) {
            throw new ChessException("Não existem movimentos possíveis para a peça escolhida.");
        }
    }

    /**
     * Valida se a posição de destino é um movimento realizável pela peça selecionada.
     */
    private void validateTargetPosition(Position source, Position target) {
        if (!board.piece(source).possibleMove(target)) {
            throw new ChessException("A peça escolhida não pode se mover para a posição de destino.");
        }
    }

    /**
     * Testa se o rei de uma cor específica está sob ameaça de captura (xeque).
     */
    private boolean testCheck(Color color) {
        Position kingPosition = king(color).getChessPosition().toPosition();
        List<Piece> opponentPieces = piecesOnTheBoard.stream()
                .filter(p -> ((ChessPiece) p).getColor() == opponent(color))
                .collect(Collectors.toList());

        for (Piece p : opponentPieces) {
            boolean[][] mat = p.possibleMoves();
            if (mat[kingPosition.getRow()][kingPosition.getColumn()]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Testa se o jogador de uma determinada cor sofreu xeque-mate (sem jogadas legais para sair do xeque).
     */
    private boolean testCheckMate(Color color) {
        if (!testCheck(color)) {
            return false;
        }
        List<Piece> list = piecesOnTheBoard.stream()
                .filter(p -> ((ChessPiece) p).getColor() == color)
                .collect(Collectors.toList());

        for (Piece p : list) {
            boolean[][] mat = p.possibleMoves();
            for (int i = 0; i < board.getRows(); i++) {
                for (int j = 0; j < board.getColumns(); j++) {
                    if (mat[i][j]) {
                        Position source = ((ChessPiece) p).getChessPosition().toPosition();
                        Position target = new Position(i, j);
                        Piece capturedPiece = makeMove(source, target);
                        boolean testCheck = testCheck(color);
                        undoMove(source, target, capturedPiece);
                        if (!testCheck) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Retorna a cor do jogador oponente.
     */
    private Color opponent(Color color) {
        return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    /**
     * Encontra e retorna o Rei da cor informada no tabuleiro.
     */
    private ChessPiece king(Color color) {
        List<Piece> list = piecesOnTheBoard.stream()
                .filter(p -> ((ChessPiece) p).getColor() == color)
                .collect(Collectors.toList());

        for (Piece p : list) {
            if (p instanceof King) {
                return (ChessPiece) p;
            }
        }
        throw new IllegalStateException("Não existe o Rei da cor " + color + " no tabuleiro.");
    }

    /**
     * Alterna o turno para o jogador oposto e incrementa a contagem de turnos.
     */
    private void nextTurn() {
        turn++;
        currentPlayer = opponent(currentPlayer);
    }

    /**
     * Posiciona uma nova peça no tabuleiro usando as coordenadas do xadrez tradicional (ex: 'e', 2) e a adiciona à lista do jogo.
     */
    private void placeNewPiece(char column, int row, ChessPiece piece) {
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
        piecesOnTheBoard.add(piece);
    }

    /**
     * Define a disposição inicial padrão das 32 peças de xadrez no início do jogo.
     */
    private void initialSetup() {
        placeNewPiece('a', 1, new Rook(board, Color.WHITE));
        placeNewPiece('b', 1, new Knight(board, Color.WHITE));
        placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('d', 1, new Queen(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE, this));
        placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('g', 1, new Knight(board, Color.WHITE));
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
        placeNewPiece('a', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('b', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('c', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('d', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('e', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('f', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('g', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('h', 2, new Pawn(board, Color.WHITE, this));

        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('b', 8, new Knight(board, Color.BLACK));
        placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('d', 8, new Queen(board, Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK, this));
        placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('g', 8, new Knight(board, Color.BLACK));
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));
        placeNewPiece('a', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('b', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('c', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('d', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('e', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('f', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('g', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('h', 7, new Pawn(board, Color.BLACK, this));
    }

    /**
     * Retorna a lista de peças capturadas de uma determinada cor.
     * 
     * @param color Cor das peças capturadas desejadas.
     * @return Lista contendo as peças de xadrez capturadas dessa cor.
     */
    public List<ChessPiece> getCapturedPieces(Color color) {
        return capturedPieces.stream()
                .filter(p -> ((ChessPiece) p).getColor() == color)
                .map(p -> (ChessPiece) p)
                .collect(Collectors.toList());
    }
}

