# Projeto-Xadrez-POO

Este projeto de xadrez em Java foi desenvolvido utilizando os conceitos de Programação Orientada a Objetos (POO) e arquitetura em camadas, como parte da disciplina lecionada pelo Professor David Nadler.

## Arquitetura

```
src/
├── boardgame/           ← Board Layer (estrutura genérica)
│   ├── Board.java
│   ├── Piece.java
│   ├── Position.java
│   └── BoardException.java
├── chess/               ← Chess Layer (regras do xadrez)
│   ├── ChessMatch.java
│   ├── ChessPiece.java
│   ├── ChessPosition.java
│   ├── ChessException.java
│   ├── Color.java
│   └── pieces/
│       ├── King.java
│       ├── Queen.java
│       ├── Rook.java
│       ├── Bishop.java
│       ├── Knight.java
│       └── Pawn.java
└── application/
    ├── Program.java
    └── UI.java
```

## Funcionalidades

- ✅ Todas as 6 peças com seus movimentos específicos
- ✅ Verificação de Xeque e Xeque-Mate
- ✅ Roque Pequeno e Roque Grande
- ✅ En Passant
- ✅ Promoção de Peão (Q, R, B, N)
- ✅ Validação de movimentos (não pode deixar o Rei em xeque)
- ✅ Interface colorida com ANSI no terminal
- ✅ Destinos possíveis destacados em azul

## Como Compilar e Executar

### Windows
```bat
compile.bat
run.bat
```

### Manual
```bash
mkdir bin
javac -d bin src/boardgame/*.java src/chess/*.java src/chess/pieces/*.java src/application/*.java
java -cp bin application.Program
```

## Como Jogar

1. Digite a **posição de origem** da peça (ex: `e2`)
2. Os movimentos possíveis serão destacados em **azul**
3. Digite a **posição de destino** (ex: `e4`)
4. Ao promover um peão, escolha a peça: `Q` (Rainha), `R` (Torre), `B` (Bispo), `N` (Cavalo)

### Notação das Peças
| Símbolo | Peça    |
|---------|---------|
| K       | Rei     |
| Q       | Rainha  |
| R       | Torre   |
| B       | Bispo   |
| N       | Cavalo  |
| P       | Peão    |

> **Brancas**: maiúsculas brancas | **Pretas**: maiúsculas amarelas

## Conceitos POO Aplicados

- **Encapsulamento**: Atributos privados com getters controlados
- **Herança**: `ChessPiece extends Piece`, cada peça extends `ChessPiece`
- **Polimorfismo**: `possibleMoves()` sobrescrito em cada peça
- **Enumerador**: `Color.WHITE / Color.BLACK`
- **Exceções personalizadas**: `BoardException`, `ChessException`
- **Separação de responsabilidades**: Board Layer vs Chess Layer

## Movimentos Especiais

### Roque
- O Rei se move 2 casas para o lado
- A Torre salta para o outro lado do Rei automaticamente
- Condições: Rei e Torre sem movimentos anteriores, casas livres, Rei não em xeque

### En Passant
- Peão captura um peão adversário que avançou 2 casas no turno anterior
- O peão capturado é removido da sua posição (não do destino)

### Promoção
- Peão que alcança a última fileira pode ser promovido
- Por padrão promove para Rainha; o jogador pode escolher
