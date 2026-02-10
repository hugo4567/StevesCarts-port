$javaFiles = Get-ChildItem -Path . -Filter "*.java" -Recurse
foreach ($file in $javaFiles) {
    Write-Host "Processing $($file.FullName)"
    
    # Lire le contenu en tant que bytes
    $bytes = [System.IO.File]::ReadAllBytes($file.FullName)
    
    # Vérifier si le BOM existe (0xEF,0xBB,0xBF est le BOM UTF-8)
    if ($bytes.Length -gt 2 -and $bytes[0] -eq 0xEF -and $bytes[1] -eq 0xBB -and $bytes[2] -eq 0xBF) {
        Write-Host "Found BOM in $($file.FullName), removing..."
        # Créer un nouveau tableau sans les 3 premiers bytes (BOM UTF-8)
        $newBytes = $bytes[3..($bytes.Length-1)]
        [System.IO.File]::WriteAllBytes($file.FullName, $newBytes)
        Write-Host "BOM removed from $($file.FullName)"
    }
    
    # Vérifier le point d'interrogation au début
    $content = [System.IO.File]::ReadAllText($file.FullName)
    if ($content.StartsWith("?")) {
        $content = $content.Substring(1)
        [System.IO.File]::WriteAllText($file.FullName, $content)
        Write-Host "Removed starting ? from $($file.FullName)"
    }
}
