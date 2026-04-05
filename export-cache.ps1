# Script à exécuter SUR L'AUTRE PC (celui qui fonctionne)
Write-Host "📦 Création de l'archive du cache Gradle" -ForegroundColor Cyan
Write-Host "═══════════════════════════════════════════`n" -ForegroundColor DarkGray

$cacheDir = "$env:USERPROFILE\.gradle\caches"
$outputFile = "gradle-cache.zip"

# Vérifier que le cache existe
if (-not (Test-Path $cacheDir)) {
    Write-Host "✗ Cache Gradle non trouvé à: $cacheDir" -ForegroundColor Red
    Write-Host "  Assurez-vous que le build fonctionne sur ce PC d'abord" -ForegroundColor Yellow
    exit 1
}

# Calculer la taille
$cacheSize = (Get-ChildItem $cacheDir -Recurse -ErrorAction SilentlyContinue | 
    Measure-Object -Property Length -Sum).Sum
$cacheSizeMB = [math]::Round($cacheSize / 1MB, 2)

Write-Host "📊 Informations:" -ForegroundColor Cyan
Write-Host "   Cache: $cacheDir"
Write-Host "   Taille: $cacheSizeMB MB"
Write-Host ""

# Créer l'archive
Write-Host "🗜️  Compression en cours..." -ForegroundColor Yellow
Write-Host "   (Ceci peut prendre 2-5 minutes pour ~500 MB de cache)" -ForegroundColor Gray
Write-Host ""

$stopwatch = [System.Diagnostics.Stopwatch]::StartNew()

try {
    Compress-Archive -Path $cacheDir -DestinationPath $outputFile -Force -CompressionLevel Optimal
    $stopwatch.Stop()
    
    $zipSize = (Get-Item $outputFile).Length
    $zipSizeMB = [math]::Round($zipSize / 1MB, 2)
    
    Write-Host "✅ Archive créée avec succès!" -ForegroundColor Green
    Write-Host ""
    Write-Host "   📁 Fichier: $outputFile"
    Write-Host "   📏 Taille: $zipSizeMB MB (compression: $(100 - [math]::Round($zipSizeMB/$cacheSizeMB*100, 0))%)"
    Write-Host "   ⏱️  Temps: $($stopwatch.Elapsed.TotalSeconds.ToString('0.0')) secondes"
    Write-Host ""
    Write-Host "📤 Transférez maintenant ce fichier vers l'autre PC" -ForegroundColor Cyan
    Write-Host "   (USB, réseau local, cloud, etc.)" -ForegroundColor Gray
    Write-Host ""
    
} catch {
    Write-Host "✗ Erreur lors de la compression: $_" -ForegroundColor Red
    exit 1
}
