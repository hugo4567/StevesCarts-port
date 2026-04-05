# Script pour réparer les problèmes de build
Write-Host "🔧 Fixing Loom cache issues..." -ForegroundColor Cyan

$loomCache = "$env:USERPROFILE\.gradle\caches\fabric-loom"

# Créer le dossier si nécessaire
New-Item -ItemType Directory -Force -Path $loomCache | Out-Null

# Télécharger le manifest
$manifestUrl = "https://piston-meta.mojang.com/mc/game/version_manifest_v2.json"
$manifestPath = Join-Path $loomCache "version_manifest.json"
$lockPath = "$manifestPath.lock"

# Supprimer le lock s'il existe
if (Test-Path $lockPath) {
    Remove-Item $lockPath -Force
    Write-Host "✓ Removed lock file" -ForegroundColor Green
}

# Télécharger le manifest
try {
    Invoke-WebRequest -Uri $manifestUrl -OutFile $manifestPath -UseBasicParsing
    Write-Host "✓ Downloaded manifest ($(((Get-Item $manifestPath).Length / 1KB).ToString('0.0')) KB)" -ForegroundColor Green
} catch {
    Write-Host "✗ Failed to download manifest: $_" -ForegroundColor Red
    exit 1
}

# Arrêter les daemons Gradle
Write-Host "`n🛑 Stopping Gradle daemons..." -ForegroundColor Cyan
& ./gradlew --stop | Out-Null

Write-Host "`n✅ Ready to build! Run: ./gradlew build" -ForegroundColor Green
