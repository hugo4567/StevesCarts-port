$javaFiles = Get-ChildItem -Path . -Filter "*.java" -Recurse
foreach ($file in $javaFiles) {
    Write-Host "Processing $($file.FullName)"
    $content = [System.IO.File]::ReadAllText($file.FullName)
    if ($content.StartsWith("?")) {
        $content = $content.Substring(1)
        [System.IO.File]::WriteAllText($file.FullName, $content)
        Write-Host "Fixed BOM in $($file.FullName)"
    } elseif ($content.StartsWith([char]0xFEFF)) {
        $content = $content.Substring(1)
        [System.IO.File]::WriteAllText($file.FullName, $content)
        Write-Host "Fixed BOM in $($file.FullName)"
    }
}
