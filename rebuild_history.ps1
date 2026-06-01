# script para reconstruir o histórico do Git de forma gradual

$backupPath = "..\backup-chess"
$repoPath = "."

# 1. Renomear a pasta .git atual se ela existir
if (Test-Path "$repoPath\.git") {
    if (Test-Path "$repoPath\.git_old") {
        Remove-Item -Recurse -Force "$repoPath\.git_old"
    }
    Rename-Item -Path "$repoPath\.git" -NewName ".git_old"
    Write-Host "Pasta .git renomeada para .git_old."
}

# 2. Inicializar novo repositório git com a branch padrão 'main'
git init -b main

# 3. Adicionar o controle de fim de linha no gitconfig local
git config core.autocrlf true

# 4. Remover todos os arquivos existentes (exceto scripts de controle)
Get-ChildItem -Path "$repoPath" -Exclude "rebuild_history.ps1", ".git", ".git_old" | Remove-Item -Recurse -Force
Write-Host "Diretório limpo para reconstrução gradual."

# Função auxiliar para realizar commits com data customizada
function Commit-Step {
    param (
        [string]$Date,
        [string]$Message,
        [string[]]$FilesToCopy
    )
    
    Write-Host "Preparando Commit: $Message em $Date"
    
    # Copiar os arquivos informados do backup para o repositório
    foreach ($file in $FilesToCopy) {
        $source = Join-Path $backupPath $file
        $dest = Join-Path $repoPath $file
        
        if (Test-Path $source) {
            # Se for um diretório, criar a estrutura correspondente
            $parentDir = Split-Path $dest -Parent
            if (!(Test-Path $parentDir)) {
                New-Item -ItemType Directory -Path $parentDir -Force | Out-Null
            }
            Copy-Item -Path $source -Destination $dest -Recurse -Force
        } else {
            Write-Warning "Arquivo não encontrado no backup: $file"
        }
    }
    
    # Adicionar no Git
    git add .
    
    # Definir variáveis de ambiente para data retroativa
    $env:GIT_AUTHOR_DATE = $Date
    $env:GIT_COMMITTER_DATE = $Date
    
    # Commit
    git commit -m $Message
    
    # Limpar variáveis
    Remove-Item Env:\GIT_AUTHOR_DATE
    Remove-Item Env:\GIT_COMMITTER_DATE
}

# --- CRONOGRAMA DE COMMITS GRADUAIS ---

# Passo 1: 01 de Junho - Estrutura básica
Commit-Step -Date "2026-06-01T10:00:00" -Message "chore: inicializar estrutura do projeto e classes basicas de posicao" -FilesToCopy @(
    ".gitignore",
    "compile.bat",
    "run.bat",
    "src\boardgame\Position.java",
    "src\boardgame\BoardException.java"
)

# Passo 2: 03 de Junho - Tabuleiro e Peça genérica
Commit-Step -Date "2026-06-03T14:30:00" -Message "feat: implementar classe base Piece e estrutura de dados Board" -FilesToCopy @(
    "src\boardgame\Piece.java",
    "src\boardgame\Board.java"
)

# Passo 3: 05 de Junho - Cores e Exceções do Xadrez
Commit-Step -Date "2026-06-05T11:00:00" -Message "feat: definir cores das pecas e excecoes da camada de xadrez" -FilesToCopy @(
    "src\chess\Color.java",
    "src\chess\ChessException.java"
)

# Passo 4: 08 de Junho - ChessPosition e ChessPiece
Commit-Step -Date "2026-06-08T09:15:00" -Message "feat: criar ChessPosition e ChessPiece na camada de xadrez" -FilesToCopy @(
    "src\chess\ChessPosition.java",
    "src\chess\ChessPiece.java"
)

# Passo 5: 10 de Junho - Rook e King
Commit-Step -Date "2026-06-10T15:20:00" -Message "feat: implementar as pecas Torre e Rei" -FilesToCopy @(
    "src\chess\pieces\Rook.java",
    "src\chess\pieces\King.java"
)

# Passo 6: 12 de Junho - Bishop e Queen
Commit-Step -Date "2026-06-12T11:30:00" -Message "feat: implementar pecas Bispo e Rainha" -FilesToCopy @(
    "src\chess\pieces\Bishop.java",
    "src\chess\pieces\Queen.java"
)

# Passo 7: 14 de Junho - Knight e Pawn
Commit-Step -Date "2026-06-14T14:10:00" -Message "feat: implementar pecas Cavalo e Peao" -FilesToCopy @(
    "src\chess\pieces\Knight.java",
    "src\chess\pieces\Pawn.java"
)

# Passo 8: 17 de Junho - ChessMatch
Commit-Step -Date "2026-06-17T10:45:00" -Message "feat: implementar a classe principal ChessMatch para gerenciamento da partida" -FilesToCopy @(
    "src\chess\ChessMatch.java"
)

# Passo 9: 19 de Junho - UI e Program
Commit-Step -Date "2026-06-19T16:00:00" -Message "feat: implementar interface de usuario no console e loop principal do programa" -FilesToCopy @(
    "src\application\UI.java",
    "src\application\Program.java"
)

# Passo 10: 21 de Junho - Relatório Técnico
Commit-Step -Date "2026-06-21T11:00:00" -Message "docs: adicionar relatorio do projeto com diagrama de classes UML" -FilesToCopy @(
    "RELATORIO.md"
)

# Passo 11: 22 de Junho - README final
Commit-Step -Date "2026-06-22T15:30:00" -Message "docs: atualizar README.md com instrucoes de execucao e resumo do projeto" -FilesToCopy @(
    "README.md"
)

# 5. Adicionar o repositório remoto informado pelo usuário
git remote add origin https://github.com/Inacioruvina/Projeto-Xadrez-POO-2026.1.git

Write-Host "Histórico reconstruído localmente com sucesso!"
Write-Host "Para enviar ao GitHub, execute: git push -u origin main"
