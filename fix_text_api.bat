@echo off
echo Remplacement des appels Text.translatable par TextHelper.translatable

cd /d "%~dp0"

:: Parcourir récursivement tous les fichiers Java
for /r "src\main\java" %%F in (*.java) do (
    echo Traitement du fichier: %%F
    
    :: Utiliser PowerShell pour faire le remplacement avec regex
    powershell -Command "(Get-Content '%%F') -replace 'Text\.translatable\(', 'TextHelper.translatable(' | Set-Content -Encoding UTF8 '%%F'"
    powershell -Command "(Get-Content '%%F') -replace 'Text\.empty\(', 'TextHelper.empty(' | Set-Content -Encoding UTF8 '%%F'"
)

echo Fin du traitement.
