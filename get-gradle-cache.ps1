# Script PowerShell pour récupérer le cache Gradle d'une autre machine
# Usage: .\get-gradle-cache.ps1 -SourcePC "autre-ordinateur" -Username "hugo"

param(
    [string]$SourcePC = "",
    [string]$Username = "",
    [string]$OutputFile = "gradle-cache.zip"
)

Write-Host "🔧 Récupération du cache Gradle depuis une autre machine" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""

if (-not $SourcePC) {
    Write-Host "📍 Options:" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "1️⃣  Copier MANUELLEMENT le cache:"
    Write-Host "    - Sur l'autre ordinateur, ouvre un PowerShell"
    Write-Host "    - Lance: Compress-Archive -Path `$env:USERPROFILE\.gradle\caches -DestinationPath gradle-cache.zip"
    Write-Host "    - Copie le fichier gradle-cache.zip sur une clé USB ou cloud"
    Write-Host ""
    Write-Host "2️⃣  Copier via RÉSEAU (si connectés):"
    Write-Host "    - Lance ce script avec des paramètres:"
    Write-Host "    - .\get-gradle-cache.ps1 -SourcePC 'nom-du-pc' -Username 'hugo'"
    Write-Host ""
    Write-Host "Ou fournis les informations interactivement:"
    Write-Host ""
    $SourcePC = Read-Host "Nom/IP de l'autre ordinateur (ou vide pour passer)"
    
    if (-not $SourcePC) {
        Write-Host ""
        Write-Host "⚠️  Sans accès à une autre machine qui a le cache, tu ne peux pas builder." -ForegroundColor Red
        Write-Host "    Cherche une sauvegarde du dossier .gradle sur ce PC ou un disque externe."
        exit 1
    }
}

Write-Host ""
Write-Host "📦 Tentative de connexion à: $SourcePC" -ForegroundColor Yellow

# Essayer d'accéder au dossier .gradle sur l'autre machine
$remotePath = "\\$SourcePC\C$\Users\$Username\.gradle\caches"

try {
    if (Test-Path $remotePath) {
        Write-Host "✅ Accès au cache trouvé!" -ForegroundColor Green
        Write-Host "   Compressing: $remotePath"
        Write-Host ""
        
        Compress-Archive -Path $remotePath -DestinationPath $OutputFile -Verbose
        
        Write-Host ""
        Write-Host "✅ Cache sauvegardé dans: $OutputFile" -ForegroundColor Green
        Write-Host ""
        Write-Host "Ensuite:"
        Write-Host "1. Transfère $OutputFile sur cette machine (clé USB, cloud, etc.)"
        Write-Host "2. Lance: Expand-Archive -Path $OutputFile -DestinationPath `$env:USERPROFILE\.gradle -Force"
        Write-Host "3. Lance: .\gradlew build"
    } else {
        Write-Host "❌ Impossible d'accéder à: $remotePath" -ForegroundColor Red
        Write-Host ""
        Write-Host "Raisons possibles:" -ForegroundColor Yellow
        Write-Host "- L'autre ordinateur est éteint"
        Write-Host "- Le nom/IP de l'ordinateur est incorrect"
        Write-Host "- Le partage réseau n'est pas activé"
        Write-Host "- Le nom d'utilisateur est incorrect"
        Write-Host ""
        Write-Host "Solution alternative:" -ForegroundColor Yellow
        Write-Host "1. Va physiquement sur l'autre ordinateur"
        Write-Host "2. Ouvre PowerShell et lance:"
        Write-Host "   Compress-Archive -Path `$env:USERPROFILE\.gradle\caches -DestinationPath gradle-cache.zip"
        Write-Host "3. Copie gradle-cache.zip sur une clé USB"
        Write-Host "4. Ramène-la sur ce PC et lance:"
        Write-Host "   Expand-Archive -Path gradle-cache.zip -DestinationPath `$env:USERPROFILE\.gradle -Force"
        exit 1
    }
} catch {
    Write-Host "❌ Erreur: $_" -ForegroundColor Red
    Write-Host ""
    Write-Host "Il y a un problème de connectivité réseau." -ForegroundColor Yellow
    Write-Host "Essaie la solution manuelle avec une clé USB." -ForegroundColor Yellow
    exit 1
}

Write-Host ""
Write-Host "✨ Terminé!" -ForegroundColor Green
