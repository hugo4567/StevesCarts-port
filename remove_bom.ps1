$javaFiles = Get-ChildItem -Path "src/main/java" -Recurse -Filter "*.java"

foreach ($file in $javaFiles) {
    $fullPath = $file.FullName
    $content = [System.IO.File]::ReadAllBytes($fullPath)
    
    # Vérifier si le fichier commence par un BOM UTF-8 (EF BB BF)
    if ($content.Length -ge 3 -and $content[0] -eq 0xEF -and $content[1] -eq 0xBB -and $content[2] -eq 0xBF) {
        Write-Host "Suppression du BOM de $fullPath"
        
        # Créer un nouveau tableau sans les 3 premiers octets (BOM)
        $newContent = New-Object byte[] ($content.Length - 3)
        [System.Buffer]::BlockCopy($content, 3, $newContent, 0, $content.Length - 3)
        
        # Écrire le contenu sans BOM
        [System.IO.File]::WriteAllBytes($fullPath, $newContent)
    }
}

Write-Host "Traitement terminé."
