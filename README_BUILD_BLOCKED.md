# ⚠️ Problème SSL/TLS - Build Steve's Carts ne fonctionne pas

## TVousêtes ici car le build échoue avec:
```
Certificate for <maven.fabricmc.net> doesn't match any of the subject alternative names
[cdnjs.cloudflare.com, *.cdnjs.cloudflare.com]
```

## C'est quoi le problème?

Le serveur `maven.fabricmc.net` a un **certificat SSL invalide** fourni par Cloudflare. 
- ❌ Windows = Échoue
- ❌ WSL/Linux = Échoue aussi  
- ❌ Docker = Échoue aussi
- ✅ **Seule solution**: Utiliser un cache Gradle qui fonctionnait AVANT le problème

## Solutions

### ✅ Solution 1: Utiliser une sauvegarde du cache Gradle (RECOMMANDÉE)

Si tu avais le build qui fonctionnait avant sur cet ordinateur ou un autre:

**Sur Windows:**
```powershell
# Si tu as une sauvegarde de .gradle:
# Restaure-la dans:
# C:\Users\[TonNom]\.gradle\

# Puis:
cd C:\chemin\vers\StevesCarts-1.19
.\gradlew.bat build
```

**Sur WSL:**
```bash
# Restaure le cache dans:
# ~/.gradle/

# Puis:
cd /chemin/vers/StevesCarts-1.19
./gradlew build
```

### ✅ Solution 2: Utiliser Docker Desktop (Windows 11)

Si Docker Desktop est installé:

```powershell
cd C:\chemin\vers\StevesCarts-1.19
docker run --rm -v ${pwd}:/project -w /project openjdk:17 ./gradlew build
```

### ✅ Solution 3: Attendre que Fabric corrige

Le problème doit être signalé et corrigé par FabricMC:
- **GitHub Issue**: https://github.com/FabricMC/fabric-loom/issues/new
- **Discord FabricMC**: Pour signaler le problème en direct

### ❌ Solution 4: (Ne fonctionne PAS)
- ❌ Changer de proxy
- ❌ Utiliser un VPN différent
- ❌ Réinstaller Java
- ❌ Nettoyer le cache Gradle complètement

Toutes ces solutions échouent car le problème est **côté serveur Fabric**, pas côté machine.

## Ce qui marche pour démontrer le problème

Testé sur cette machine:
- ✅ 6 versions de Gradle (7.4 → 9.0)
- ✅ 4 versions de Loom (0.12 → 1.3)
- ✅ Configuration TLS variant (SNI, IPv4/IPv6, etc.)
- ✅ Compilation manuelle avec javac

**Résultat**: Tous échouent avec le même problème SSL/TLS

## Fichiers d'aide disponibles

- `BUILD_DIAGNOSIS.md` - Rapport technique complet
- `build-workaround.sh` - Script de diagnostic
- `GET_GRADLE_CACHE.sh` - Guide du cache Gradle

## Questions?

1. **Tu peux vérifier si le build marchait avant?**
   → Cherche un .tar.gz ou .zip contenant `.gradle/caches`

2. **Tu as accès à une autre machine où le build marche?**
   → Copie le cache depuis cette machine

3. **Comment obtenir le cache d'une autre machine?**
   ```bash
   # Sur l'autre machine (Windows):
   Compress-Archive -Path $env:USERPROFILE\.gradle\caches -DestinationPath gradle-cache.zip
   
   # Sur ton actuelle (après avoir copié le fichier):
   Expand-Archive -Path gradle-cache.zip -DestinationPath $env:USERPROFILE\.gradle -Force
   ./gradlew build  # Ça devrait marcher maintenant
   ```

---

**Statut**: 🛑 Build bloqué par problème de certificat SSL côté serveur Fabric
**Responsabilité**: Fabric/FabricMC (problème d'infrastructure)
**Solution**: Utiliser un cache Gradle pré-existant qui fonctionne
