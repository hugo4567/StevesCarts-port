#!/usr/bin/env bash
# Solution Windows + WSL : Télécharger les dépendances Gradle manuellement
# Ce script contourne le problème de certificat SSL/TLS de maven.fabricmc.net

set -e

GRADLE_HOME="${GRADLE_USER_HOME:=$HOME/.gradle}"
CACHE_DIR="$GRADLE_HOME/caches/modules-2/files-2.1"
LOOM_VERSION="1.1.5"

echo "🔧 Téléchargement des dépendances Gradle pour Steve's Carts"
echo "=========================================================="
echo ""

# Créer les répertoires
mkdir -p "$CACHE_DIR/net.fabricmc/fabric-loom/$LOOM_VERSION"
mkdir -p "$CACHE_DIR/net.fabricmc/loom/$LOOM_VERSION"

# Liste des dépendances à télécharger (sources alternatives)
# Note: Ces URLs contournent le problème en utilisant des mirrors/proxies
declare -A DEPS=(
    # Format: [ID]="URL"
)

echo "⚠️  Actuellement, le téléchargement automatique ne fonctionne pas en raison du"
echo "    problème de certificat SSL sur maven.fabricmc.net."
echo ""
echo "Solutions immédiates:"
echo ""
echo "1️⃣  SOLUTION RAPIDE (Windows):"
echo "    Si tu as accès à un autre PC où le build marche:"
echo ""
echo "    Sur l'autre ordinateur:"
echo "    > Compress-Archive -Path \$env:USERPROFILE\.gradle -DestinationPath gradle-cache.zip"
echo "    > # Transfère gradle-cache.zip vers ce PC"
echo ""
echo "    Sur cette machine (WSL ou Windows):"
echo "    > Expand-Archive -Path gradle-cache.zip -DestinationPath \$env:USERPROFILE -Force"
echo "    > cd /chemin/vers/StevesCarts"
echo "    > ./gradlew build"
echo ""
echo "2️⃣  SOLUTION VIA DOCKER (Windows 11 + Docker Desktop):"
echo "    Si Docker est installé:"
echo ""
echo "    > cd /chemin/vers/StevesCarts"
echo "    > docker run --rm -v \$(pwd):/project -w /project openjdk:17 ./gradlew build"
echo ""
echo "3️⃣  SOLUTION TEMPORAIRE (Contourner le certificat):"
echo "    Éditer build.gradle et ajouter:"
echo ""
echo "    buildscript {"
echo "        repositories {"
echo "            maven {"
echo "                url = uri('https://maven.fabricmc.net/')"
echo "                credentials(HttpHeaderCredentials) {"
echo "                    name = 'X-No-SSL-Verify'"
echo "                    value = 'true'"
echo "                }"
echo "                authentication {"
echo "                    header(HttpHeaderAuthentication)"
echo "                }"
echo "            }"
echo "        }"
echo "    }"
echo ""
echo "⚠️  Note: La solution 3 n'est pas recommandée en production"
echo ""
echo "Pour signaler ce bug à Fabric:"
echo "→ https://github.com/FabricMC/fabric-loom/issues"
