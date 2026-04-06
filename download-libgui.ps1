# Download LibGui from Modrinth
$libguiUrl = "https://cdn.modrinth.com/data/4wYDEZ4a/versions/xJMDjGla/LibGui-5.4.0%2B1.19.2.jar"
$outputDir = "libs"
$outputFile = "$outputDir\LibGui-5.4.0+1.19.2.jar"

# Create libs directory if it doesn't exist
if (-not (Test-Path $outputDir)) {
    New-Item -ItemType Directory -Path $outputDir | Out-Null
}

Write-Host "Downloading LibGui from Modrinth..." -ForegroundColor Cyan
try {
    Invoke-WebRequest -Uri $libguiUrl -OutFile $outputFile -UseBasicParsing
    Write-Host "✓ LibGui downloaded successfully to $outputFile" -ForegroundColor Green
} catch {
    Write-Host "✗ Failed to download LibGui: $_" -ForegroundColor Red
    exit 1
}
