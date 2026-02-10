@echo off
echo Ajout des imports pour TextHelper

cd /d "%~dp0"

:: Parcourir récursivement tous les fichiers Java
for /r "src\main\java" %%F in (*.java) do (
    echo Traitement du fichier: %%F
    
    :: Vérifier si le fichier contient TextHelper mais pas l'import
    powershell -Command "if ((Get-Content '%%F' -Raw) -match 'TextHelper\.') { if (-not (Get-Content '%%F' -Raw -match 'import vswe\.stevescarts\.util\.TextHelper;')) { $content = Get-Content '%%F'; $insertAt = -1; for ($i=0; $i -lt $content.Length; $i++) { if ($content[$i] -match '^import ') { $insertAt = $i; } elseif ($content[$i] -match '^public |^class |^interface ') { break; } } if ($insertAt -ne -1) { $content = $content[0..$insertAt] + 'import vswe.stevescarts.util.TextHelper;' + $content[($insertAt+1)..($content.Length-1)]; Set-Content -Path '%%F' -Value $content -Encoding UTF8; }}}"
)

echo Fin du traitement des imports.
