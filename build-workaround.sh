#!/bin/bash
# Script de workaround pour builder Steve's Carts sur Linux avec problèmes SSL
# Ce script essaie plusieurs stratégies pour contourner les problèmes de téléchargement du plugin Loom

set -e

GRADLE_USER_HOME="${GRADLE_USER_HOME:-$HOME/.gradle}"
LOOM_CACHE_DIR="$GRADLE_USER_HOME/caches/modules-2/files-2.1/net.fabricmc"

echo "🔧 Steve's Carts - Build Workaround pour Linux"
echo "================================================"
echo ""

# Étape 1: Vérifier Java
echo "✓ Vérification de Java..."
if ! command -v java &> /dev/null; then
    echo "❌ Java n'est pas installé"
    exit 1
fi
JAVA_VERSION=$(java -version 2>&1 | grep 'version' | head -1)
echo "  Java trouvé: $JAVA_VERSION"
echo ""

# Étape 2: Vérifier Gradle
echo "✓ Vérification de Gradle..."
./gradlew --version 2>&1 | head -2
echo ""

# Étape 3: Essayer le build normal d'abord
echo "✓ Tentative 1: Build normal..."
if ./gradlew build 2>&1 | tee /tmp/gradle.log | grep -q "BUILD SUCCESSFUL"; then
    echo "✅ Build réussi! JAR généré dans build/libs/"
    exit 0
fi

# Vérifier l'erreur
if grep -q "Certificate for.*doesn't match" /tmp/gradle.log; then
    echo "❌ Problème SSL/TLS détecté"
    echo "   Le certificat de maven.fabricmc.net ne correspond pas"
    echo ""
    echo "📋 Solutions disponibles:"
    echo ""
    echo "1. RECOMMANDÉ - Copier le cache Gradle depuis une autre machine:"
    echo "   $ scp -r utilisateur@autre-machine:~/.gradle/caches ./"
    echo "   $ mkdir -p ~/.gradle && mv caches ~/.gradle/"
    echo "   $ ./gradlew build"
    echo ""
    echo "2. Utiliser un proxy IPv4 uniquement (désactiver IPv6):"
    echo "   $ ./gradlew -Djava.net.preferIPv4Stack=true build"
    echo ""
    echo "3. Utiliser Docker (si installé):"
    echo "   $ docker run --rm -v \$(pwd):/project -w /project openjdk:17 "./gradlew build""
    echo ""
    exit 1
fi

if grep -q "Could not resolve.*fabric-loom" /tmp/gradle.log; then
    echo "❌ Plugin Loom introuvable"
    echo ""
    echo "Le système ne peut pas télécharger le plugin Fabric Loom depuis les dépôts."
    echo "Cela est généralement dû à:"
    echo "  - Un problème de connectivité réseau"
    echo "  - Un firewall/proxy bloquant les dépôts Maven"
    echo "  - Un problème DNS"
    echo ""
    echo "Solutions:"
    echo "1. Vérifier la connectivité: curl -I https://maven.fabricmc.net/"
    echo "2. Vérifier le DNS: nslookup maven.fabricmc.net"
    echo "3. Copier le cache Gradle depuis une machine fonctionnelle"
    echo ""
    exit 1
fi

echo "❌ Build échoué avec une erreur inconnue"
echo "Consulter BUILD_LINUX_WORKAROUND.md pour plus d'infos"
exit 1
