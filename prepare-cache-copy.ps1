# Script de préparation pour copier le cache depuis l'autre PC
Write-Host "🔧 Préparation pour copie de cache Gradle" -ForegroundColor Cyan
Write-Host "═════════════════════════════════════════════════`n" -ForegroundColor DarkGray

# 1. Arrêter tous les daemons Gradle
Write-Host "1️⃣  Arrêt des daemons Gradle..." -ForegroundColor Yellow
& ./gradlew --stop
Write-Host "   ✓ Daemons arrêtés`n" -ForegroundColor Green

# 2. Sauvegarder le cache existant (au cas où)
$cacheDir = "$env:USERPROFILE\.gradle\caches"
$backupDir = "$env:USERPROFILE\.gradle\caches.backup.$(Get-Date -Format 'yyyyMMdd-HHmmss')"

if (Test-Path $cacheDir) {
    Write-Host "2️⃣  Sauvegarde du cache existant..." -ForegroundColor Yellow
    Write-Host "   Source: $cacheDir" -ForegroundColor Gray
    Write-Host "   Backup: $backupDir" -ForegroundColor Gray
    Move-Item -Path $cacheDir -Destination $backupDir -Force
    Write-Host "   ✓ Sauvegarde créée`n" -ForegroundColor Green
} else {
    Write-Host "2️⃣  Pas de cache existant à sauvegarder`n" -ForegroundColor Gray
}

# 3. Instructions pour la copie
Write-Host "3️⃣  Prochaines étapes:" -ForegroundColor Yellow
Write-Host ""
Write-Host "   📁 SUR LE PC QUI FONCTIONNE:" -ForegroundColor Cyan
Write-Host "   ────────────────────────────" -ForegroundColor DarkGray
Write-Host "   Exécuter cette commande PowerShell:"
Write-Host ""
Write-Host '   Compress-Archive -Path "$env:USERPROFILE\.gradle\caches" `' -ForegroundColor White
Write-Host '       -DestinationPath "gradle-cache.zip" -Force' -ForegroundColor White
Write-Host ""
Write-Host "   Puis transférer gradle-cache.zip vers ce PC"
Write-Host ""
Write-Host "   📁 SUR CE PC (après transfert):" -ForegroundColor Cyan
Write-Host "   ────────────────────────────" -ForegroundColor DarkGray
Write-Host "   1. Placer gradle-cache.zip dans ce dossier"
Write-Host "   2. Exécuter:"
Write-Host ""
Write-Host '   Expand-Archive -Path "gradle-cache.zip" `' -ForegroundColor White
Write-Host '       -DestinationPath "$env:USERPROFILE\.gradle" -Force' -ForegroundColor White
Write-Host ""
Write-Host "   3. Puis lancer le build:"
Write-Host "   ./gradlew build" -ForegroundColor White
Write-Host ""

# 4. Créer le dossier caches vide pour éviter les erreurs
New-Item -ItemType Directory -Path $cacheDir -Force | Out-Null

Write-Host "✅ Prêt pour recevoir le cache!" -ForegroundColor Green
Write-Host ""
Write-Host "⚠️  RAPPEL: Une fois le cache copié, NE JAMAIS faire './gradlew clean'" -ForegroundColor Yellow
Write-Host ""
