# Compare modules between v1.19 and v1.12.2

# Get module lists
$modules_1_19 = @(Get-ChildItem -Path "src\main\java\vswe\stevescarts\module\" -Recurse -Filter "*Module.java" | Select-Object -ExpandProperty BaseName)
$modules_1_12 = @(Get-ChildItem -Path "src_old\src\main\java\vswe\stevescarts\modules\" -Recurse -Filter "*Module.java" | Select-Object -ExpandProperty BaseName)

# Sort for comparison
$modules_1_19 = $modules_1_19 | Sort-Object
$modules_1_12 = $modules_1_12 | Sort-Object

# Analysis
$ported = @($modules_1_19 | Where-Object { $_ -in $modules_1_12 })
$missing = @($modules_1_12 | Where-Object { $_ -notin $modules_1_19 })
$new = @($modules_1_19 | Where-Object { $_ -notin $modules_1_12 })

Write-Host "=== COMPARISON: v1.19 vs v1.12.2 ===" -ForegroundColor Green
Write-Host ""
Write-Host "v1.12.2 original modules: $($modules_1_12.Count)"
Write-Host "v1.19 current modules: $($modules_1_19.Count)"
$percent = [math]::Round($ported.Count / $modules_1_12.Count * 100, 1)
Write-Host "Modules ported (common): $($ported.Count) [$percent%]"
Write-Host "Modules still missing: $($missing.Count)"
Write-Host "New modules added: $($new.Count)"
Write-Host ""

Write-Host "=== MISSING MODULES ($($missing.Count)) ===" -ForegroundColor Yellow
$missing | ForEach-Object { Write-Host "  - $_" }

Write-Host ""
Write-Host "=== NEW MODULES ADDED ($($new.Count)) ===" -ForegroundColor Cyan
$new | ForEach-Object { Write-Host "  + $_" }
