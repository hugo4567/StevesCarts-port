# ⚠️ Build Workaround pour Linux

## Problème Rencontré

Sur cette machine Linux, Gradle 8.9 ne peut **pas télécharger le plugin Fabric Loom** en raison d'un **problème SSL/TLS systémique** :

```
Certificate for <maven.fabricmc.net> doesn't match any of the subject alternative names: [cdnjs.cloudflare.com, *.cdnjs.cloudflare.com]
```

Le serveur maven.fabricmc.net pointe vers une CDN Cloudflare avec un certificat qui ne correspond pas, et toutes les versions de Gradle/Loom testées (7.4.2 → 9.0) échouent avec la même erreur.

## Solutions Disponibles

### 1. ✅ Copier le Cache Gradle (Méthode Recommandée)

Si vous avez accès à une autre machine où le build de Steve's Carts fonctionne :

```bash
# Sur la machine fonctionnelle:
tar -czf gradle-cache.tar.gz ~/.gradle/caches

# Sur cette machine (Linux):
tar -xzf gradle-cache.tar.gz -C ~/
```

Après cela, le build devrait fonctionner :

```bash
./gradlew build
```

### 2. 🔄 Installer un Proxy Maven Local

Installez **Nexus Repository Manager** ou **Artifactory** localement et synchronisez les dépendances Fabric.

### 3. 🌐 Configurer un VPN/Proxy Réseau

Un proxy réseau ou VPN pourrait contourner le blocage SSL/TLS.

### 4. 🐳 Utiliser Docker

Lancez le build dans un container Docker avec une image Java/Gradle pré-configurée qui n'a pas ce problème SSL.

## Versions Testées

Les versions suivantes ont **toutes échoué** avec le même problème SSL :

| Gradle | Loom    | Résultat |
|--------|---------|----------|
| 7.4.2  | 0.12.89 | ❌ Incompatibilité Java 21 |
| 7.6    | 0.12.89 | ❌ Problème TLS/cert |
| 8.4    | 1.3.14  | ❌ Plugin introuvable |
| 8.8    | 1.3.14  | ❌ Incompatibilité bytecode |
| 8.9    | 1.1.5   | ❌ Cert mismatch CDN |
| 9.0    | 1.1.5   | ❌ Java 25 incompatible |

## Configuration Actuelle

```gradle
// build.gradle
buildscript {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven { 
            url "http://maven.fabricmc.net/"
            allowInsecureProtocol = true
        }
    }
    dependencies {
        classpath 'net.fabricmc:fabric-loom:1.1.5'
    }
}

apply plugin: 'net.fabricmc.loom'
```

```properties
# gradle.properties
org.gradle.jvmargs=-Xmx5G -Dhttps.protocols=TLSv1.2 \
  -Djava.net.preferIPv4Stack=true
systemProp.jsse.enableSNIExtension=false
```

## Cache Disponible

Le répertoire `build/loom-cache/` contient **92 JARs pré-téléchargés** de dépendances Minecraft/Fabric, mais Gradle ne peut pas charger le plugin Loom lui-même.

## Prochaines Étapes

1. Obtenez le cache Gradle complet depuis une machine fonctionnelle
2. Exécutez : `./gradlew build`
3. Le JAR du mod sera généré dans `build/libs/`

## Support

Si vous avez toujours des problèmes après avoir copié le cache, vérifiez :
- `java -version` (doit être Java 17+)
- `./gradlew --version` (doit être Gradle 8.9)
- Les fichiers du cache ne sont pas corrompus
