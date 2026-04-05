# ⚠️ IMPORTANT - NE PAS SUPPRIMER

## Cache Gradle Requis

Sur ce PC, Fabric Loom ne peut pas télécharger automatiquement les fichiers Minecraft.
Le cache Gradle a été copié depuis un autre PC et **NE DOIT JAMAIS ÊTRE SUPPRIMÉ**.

### ❌ Commandes INTERDITES:
```bash
./gradlew clean          # ❌ Vide le cache complet
./gradlew cleanCache     # ❌ Vide les caches
rm -rf ~/.gradle/caches  # ❌ Supprime tout le cache
```

### ✅ Commandes AUTORISÉES:
```bash
./gradlew build                           # ✓ Build normal
Remove-Item build -Recurse -Force         # ✓ Clean build outputs only
./gradlew build --rerun-tasks             # ✓ Force rebuild sans vider cache
.\fix-build.ps1; ./gradlew build          # ✓ Réparer locks avant build
```

### 🔧 En cas de problème:
1. Exécuter `.\fix-build.ps1` pour réparer les locks
2. Si ça ne marche toujours pas, re-copier le cache depuis l'autre PC

### 📁 Fichiers critiques:
- `%USERPROFILE%\.gradle\caches\fabric-loom\` - Cache Loom (NE PAS TOUCHER)
- `%USERPROFILE%\.gradle\caches\modules-2\` - Dépendances Maven (NE PAS TOUCHER)
- `.\build\` - Sorties de build (SAFE à supprimer)
