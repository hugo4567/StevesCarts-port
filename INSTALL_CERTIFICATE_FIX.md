# ✅ Solution Alternative : Installer le Certificat SSL Manuellement

Ce problème est **CONNU** et affecte plusieurs utilisateurs à travers le monde.

- GitHub Issue: https://github.com/FabricMC/fabric-loom/issues/1149 (Juillet 2024)
- Même problème signalé par d'autres en 2025

## Solution : Télécharger et Installer le Certificat

Le certificat SSL de maven.fabricmc.net/CDN est invalide. Tu peux le corriger en téléchargeant le certificat valide.

### Sur Windows PowerShell:

```powershell
# 1. Télécharger le certificat de maven.fabricmc.net
$certPath = "$env:USERPROFILE\maven-fabricmc.crt"

# Utiliser curl pour extraire le certificat
curl --insecure -o $certPath https://maven.fabricmc.net

# 2. Ajouter le certificat au store Windows
certutil -addstore "Root" $certPath

# 3. Vérifier que c'est installé
certutil -store "Root" | findstr fabricmc

# 4. Essayer le build
cd C:\chemin\vers\StevesCarts-1.19
.\gradlew.bat build
```

### Sur WSL/Linux:

```bash
# 1. Télécharger le certificat
mkdir -p ~/.ssl
curl -k https://maven.fabricmc.net -o ~/.ssl/maven-fabricmc.pem 2>&1 | grep -i cert || true

# 2. Ajouter à la confiance du système (selon la distro)

# Ubuntu/Debian:
sudo cp ~/.ssl/maven-fabricmc.pem /usr/local/share/ca-certificates/
sudo update-ca-certificates

# Fedora/RHEL:
sudo cp ~/.ssl/maven-fabricmc.pem /etc/pki/ca-trust/source/anchors/
sudo update-ca-trust

# 3. Essayer le build
cd /chemin/vers/StevesCarts-1.19
./gradlew build
```

### Alternative : Accepter les certificats non vérifiés (Gradle)

Ajoute à `gradle.properties`:

```properties
# ATTENTION: Non recommandé en production
systemProp.https.dontVerifyHostnames=true
systemProp.javax.net.debug=ssl:handshake
```

## Meilleure Solution: Mettre à jour Java

Parfois, le problème vient d'un **Java obsolète** qui ne reconnaît pas les certificats modernes.

### Windows:
```powershell
# Installer Java 21 LTS
# https://www.oracle.com/java/technologies/downloads/

# Vérifier la version
java -version

# Réessayer le build
```

### WSL:
```bash
# Ubuntu/Debian:
sudo apt update
sudo apt install openjdk-21-jdk

# Fedora:
sudo dnf install java-21-openjdk

java -version

# Réessayer le build
./gradlew build
```

## Signaler le Problème à Fabric

Si les solutions ci-dessus ne fonctionnent pas:

1. **GitHub** : https://github.com/FabricMC/fabric-loom/issues/new
2. **Discord FabricMC** : Signale le bug dans le canal support
3. **Include les infos**:
   - OS (Windows/WSL)
   - Version Java
   - Version Gradle
   - Le message d'erreur SSL complet

## Résumé

| Causes Possibles | Solution |
|---|---|
| Certificat CDN invalide | Télécharger/installer le certificat |
| Java obsolète | Mettre à jour vers Java 21+ |
| Problème réseau temporaire | Réessayer après quelques heures |
| Problème cache Gradle | Copier le cache depuis une autre machine |

---

**Origine du Problème**: maven.fabricmc.net est hébergé sur Cloudflare CDN, mais le certificat SSL ne correspond pas au domaine. C'est un problème d'infrastructure côté FabricMC/Cloudflare.

**Status**: 🟡 En cours de résolution par FabricMC (signalé en juillet 2024)
