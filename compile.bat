@echo off
echo Compilando Sistema de Xadrez...

if not exist bin mkdir bin

javac -d bin ^
  src\boardgame\BoardException.java ^
  src\boardgame\Position.java ^
  src\boardgame\Piece.java ^
  src\boardgame\Board.java ^
  src\chess\Color.java ^
  src\chess\ChessException.java ^
  src\chess\ChessPosition.java ^
  src\chess\ChessPiece.java ^
  src\chess\pieces\Rook.java ^
  src\chess\pieces\Bishop.java ^
  src\chess\pieces\Knight.java ^
  src\chess\pieces\Queen.java ^
  src\chess\pieces\King.java ^
  src\chess\pieces\Pawn.java ^
  src\chess\ChessMatch.java ^
  src\application\UI.java ^
  src\application\Program.java

if %ERRORLEVEL% == 0 (
    echo.
    echo Compilacao concluida com sucesso!
    echo Para executar: java -cp bin application.Program
) else (
    echo.
    echo ERRO na compilacao. Verifique as mensagens acima.
)
