# Script pour télécharger les dépendances Fabric avec curl --insecure
# Contournement du problème SSL de maven.fabricmc.net

Write-Host "🔧 Téléchargement des dépendances Fabric (ignore SSL)`n" -ForegroundColor Green

$curlPath = "C:\Program Files\Git\mingw64\bin\curl.exe"

if (-not (Test-Path $curlPath)) {
    Write-Host "❌ curl non trouvé. Installez Git for Windows." -ForegroundColor Red
    exit 1
}

# Fonction pour télécharger avec curl
function Download-WithCurl {
    param($url, $output)
    
    $dir = Split-Path $output -Parent
    if (-not (Test-Path $dir)) {
        New-Item -ItemType Directory -Force -Path $dir | Out-Null
    }
    
    Write-Host "  📥 $url" -ForegroundColor Gray
    & $curlPath --insecure --location --silent --show-error --output $output $url 2>&1
    
    if (Test-Path $output) {
        $size = (Get-Item $output).Length
        if ($size -gt 0) {
            Write-Host "  ✓ Téléchargé: $([math]::Round($size / 1KB, 1)) KB" -ForegroundColor Green
            return $true
        }
    }
    Write-Host "  ✗ Échec" -ForegroundColor Red
    return $false
}

Write-Host "Étape 1: Téléchargement manuel avec Gradle en mode résolution`n" -ForegroundColor Cyan
Write-Host "Ceci va lister toutes les dépendances nécessaires...`n" -ForegroundColor Gray

# Lancer Gradle pour qu'il liste les dépendances manquantes
Write-Host "Lancement de: ./gradlew dependencies --refresh-dependencies`n" -ForegroundColor Yellow
Write-Host "(Cela va échouer mais nous donnera la liste des URLs)"`n -ForegroundColor Gray

.\gradlew dependencies --refresh-dependencies 2>&1 | Out-File -FilePath ".\dep-errors.log"

Write-Host "✓ Log créé: dep-errors.log`n" -ForegroundColor Green
Write-Host "Analysez ce fichier pour voir quelles URLs maven.fabricmc.net sont nécessaires`n" -ForegroundColor Cyan

Write-Host "═══════════════════════════════════════════════════`n" -ForegroundColor DarkGray
Write-Host "SOLUTION ALTERNATIVE PLUS SIMPLE:`n" -ForegroundColor Yellow
Write-Host "Utilisez --offline mode APRÈS avoir copié le cache d'un PC qui fonctionne:`n" -ForegroundColor White
Write-Host "  1. Sur PC qui marche: copier ~/.gradle/caches/" -ForegroundColor Gray
Write-Host "  2. Sur ce PC: coller dans ~/.gradle/caches/" -ForegroundColor Gray
Write-Host "  3. ./gradlew build --offline`n" -ForegroundColor Gray
