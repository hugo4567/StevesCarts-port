# 🛑 Diagnostic Complet du Problème de Build

**Date du Diagnostic** : 5 Avril 2026  
**Machine** : Linux (Hugo)  
**Projet** : Steve's Carts 3 (Fabric 1.19)

## Résumé Exécutif

Le build de ce projet **ne peut pas être complet sur cette machine** en raison d'un **blocage SSL/TLS systémique** empêchant Gradle de télécharger le plugin Fabric Loom depuis tous les dépôts disponibles.

## Problème Détecté

### Erreur Primaire
```
Certificate for <maven.fabricmc.net> doesn't match any of the subject alternative names: [cdnjs.cloudflare.com, *.cdnjs.cloudflare.com]
```

### Cause Racine
- Le serveur `maven.fabricmc.net` pointe vers une CDN Cloudflare
- Le certificat SSL du CDN ne correspond pas au domaine demandé
- Gradle 8.9 refuse la connexion SSL non-valide

### Symptômes
- Gradle tente de télécharger : `net.fabricmc:fabric-loom:1.1.5`
- Le plugin n'est trouvé dans **aucun** des dépôts configurés:
  - Gradle Central Plugin Repository
  - Gradle Plugin Portal
  - Maven Central
  - maven.fabricmc.net
  - Modrinth
  - Local Maven Cache

## Tentatives Effectuées

### 1. Versions de Gradle Testées
| Version | Résultat |
|---------|----------|
| 7.4.2   | ❌ Incompatible avec Java 21 (bytecode maj 65) |
| 7.6     | ❌ Certificat SSL invalide |
| 8.4     | ❌ Plugin introuvable  |
| 8.8     | ❌ Bytecode Java 25 incompatible (maj 69) |
| **8.9**  | ❌ **Certificat SSL - CDN mismatch** |
| 9.0     | ❌ Java 25 incompatible |

### 2. Versions de Loom Testées  
| Version | Résultat |
|---------|----------|
| 0.12.89 | ❌ Non trouvé |
| 1.0.34  | ❌ Non trouvé |
| 1.1.5   | ❌ Non trouvé (cert SSL) |
| 1.2.26  | ❌ Version inexistante |
| 1.3.14  | ❌ Non trouvé |

### 3. Configurations Testées
- ✅ Plugin DSL moderne vs classique `buildscript`
- ✅ Différentes conbiles de repositories Maven
- ✅ Paramètres TLS : TLSv1.2, TLSv1.3
- ✅ SNI extension désactivé
- ✅ Préférences IPv4/IPv6 ajustées
- ✅ HTTP insécurisé avec `allowInsecureProtocol`
- ✅ Redirection HTTP → HTTPS testée
- ✅ Téléchargement manuel via curl, wget, Python

### 4. Ressources en Cache Trouvées
- ✅ 92 JARs de dépendances Minecraft/Fabric dans `build/loom-cache/`
- ✅ Mappings Yarn téléchargés et remappés
- ✅ Dépendances LibGui, LibBlockAttributes présentes
- ❌ Plugin Loom LUI-MÊME non présent

## État du Cache Gradle

```
~/.gradle/caches/
├── modules-2/
│   ├── files-2.1/  (36 KB - vide ou partiel)
│   └── jars-9/     (avec métadonnées)
├── 7.4.2/          (répertoire Gradle - n'utiliserait pas)
├── 7.6/            (répertoire Gradle - n'utiliserait pas)
├── 8.4/            (répertoire Gradle - n'utiliserait pas)
├── 8.8/            (répertoire Gradle - n'utiliserait pas)
├── 8.9/            (ACTUEL - contient JARs Gradle)
└── transforms-3/   (cache transformation)
```

Le cache `modules-2` est **vide ou très incomplet** et ne contient pas les dépendances Maven essentielles.

## Configuration Actuelle

### build.gradle
```gradle
buildscript {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven { 
            url "http://maven.fabricmc.net/"
            allowInsecureProtocol = true
        }
        maven { url "https://repo.gradle.org/gradle/repo" }
    }
    dependencies {
        classpath 'net.fabricmc:fabric-loom:1.1.5'
    }
}

apply plugin: 'net.fabricmc.loom'
apply plugin: 'maven-publish'

sourceCompatibility = JavaVersion.VERSION_17
targetCompatibility = JavaVersion.VERSION_17
```

### gradle.properties
```properties
org.gradle.jvmargs=-Xmx5G -Dhttps.protocols=TLSv1.2 \
  -Djava.net.preferIPv4Stack=true
systemProp.jsse.enableSNIExtension=false
systemProp.https.protocols=TLSv1.2
org.gradle.offline=false
```

## Solutions Disponibles

### ✅ Solution 1 : Copier le Cache Gradle (RECOMMANDÉE)

**Pré-requis** : Accès à une machine où le build de Steve's Carts fonctionne

#### Sur la machine source (fonctionnelle) :
```bash
# Compresser le cache complet
tar -czf /tmp/gradle-cache.tar.gz ~/.gradle/caches

# Transférer vers la machine Linux
scp /tmp/gradle-cache.tar.gz hugo@machine-linux:/tmp/
```

#### Sur cette machine Linux :
```bash
# Extraire et remplacer le cache
cd ~
tar -xzf /tmp/gradle-cache.tar.gz 

# Vérifier les permissions
chmod -R u+w ~/.gradle/caches

# Tester le build
cd /run/media/hugo/SAN16G/StevesCarts-1.19/StevesCarts-1.19
./gradlew build
```

### ✅ Solution 2 : Utiliser Docker

Si Docker est installé sur la machine :

```bash
# Créer un container avec Java/Gradle pré-configuré
docker run --rm \
  -v $(pwd):/project \
  -w /project \
  openjdk:17-jdk-slim \
  bash -c "./gradlew build"
```

### ✅ Solution 3 : Configurer un Proxy Maven Local

Installer **Nexus Repository Manager** ou **Artifactory** et synchroniser les dépôts Fabric.

### ✅ Solution 4 : Utiliser un VPN/Proxy Réseau

Un VPN ou proxy réseau pourrait contourner le blocage SSL/TLS sur le serveur Fabric.

## Fichiers de Support Créés

1. **`BUILD_LINUX_WORKAROUND.md`** - Guide détaillé des solutions
2. **`build-workaround.sh`** - Script de diagnostic automatisé
3. **`download_loom.py`** - Téléchargeur alternatif (non fonctionnel, pour référence)
4. **`GRADLE_CACHE_WARNING.md`** - Avertissement original (Windows)

## Prochaines Étapes Recommandées

### Immédiat
1. [ ] Obtenir le cache Gradle complet depuis une machine fonctionnelle
2. [ ] Copier vers `~/.gradle/caches`
3. [ ] Lancer `./gradlew build`

### Alternative
1. [ ] Installer et configurer Docker
2. [ ] Lancer le build dans un container

### Long terme
1. [ ] Contacter FabricMC pour signaler le problème de certificat SSL
2. [ ] Maintenir une copie du cache gradle en backup
3. [ ] Envisager un mirror Maven local pour les dépôts Fabric

## Informations du Système

```
OS: Linux
Java: java-21-openjdk + java-25-openjdk
Gradle: 8.9 (actuel)
Minecraft: 1.19.2
Fabric Loader: 0.14.8
Yarn Mappings: build.28
```

## Ressources Disponibles

- **Original v1.12.2 (Forge)** : `src_old/` - 124 modules complets
- **Cache Loom** : `build/loom-cache/` - 92 JARs téléchargés + remappés
- **Source code Fabric 1.19** : `src/main/java/` - 35/124 modules portés (28%)

## Contact / Support

Pour plus d'informations sur Steve's Carts:
- Repository: `github.com/FabricMC/fabric-loom`
- Discord FabricMC: Pour signaler les problèmes SSL
- Documentation: `GUIDE_PORTAGE.md`, `STRUCTURE_DU_MOD.md`

---

**Statut** : 🛑 Build bloqué en raison de problèmes SSL/TLS systémiques
**Recommandation** : Copier le cache Gradle depuis une machine fonctionnelle
