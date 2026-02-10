$filesWithTextHelper = Get-ChildItem -Path "src/main/java" -Recurse -Filter "*.java" | Select-String -Pattern "TextHelper\." | Select-Object -ExpandProperty Path -Unique

foreach ($file in $filesWithTextHelper) {
    $content = Get-Content -Path $file -Raw -Encoding UTF8
    if (-not ($content -match "import vswe\.stevescarts\.util\.TextHelper;")) {
        $newContent = $content -replace "package (.*?);", "package `$1;`n`nimport vswe.stevescarts.util.TextHelper;"
        Set-Content -Path $file -Value $newContent -Encoding UTF8 -NoNewline
        Write-Host "Ajout de l'import TextHelper au fichier $file"
    } else {
        Write-Host "L'import TextHelper existe déjà dans le fichier $file"
    }
}
