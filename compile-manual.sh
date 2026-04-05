#!/bin/bash
# Compilation manuelle sans Gradle (dernier recours)
# Utilise les JARs déjà téléchargés dans build/loom-cache

set -e

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
LOOM_CACHE="$PROJECT_DIR/build/loom-cache/remapped_working"
SRC_DIR="$PROJECT_DIR/src/main/java"
BUILD_DIR="$PROJECT_DIR/build/classes/java/manual"

echo "🔧 Compilation manuelle de Steve's Carts"
echo "=========================================="
echo ""

# Vérifier que les dépendances existent
if [ ! -d "$LOOM_CACHE" ]; then
    echo "❌ Cache Loom introuvable: $LOOM_CACHE"
    echo "Lancez d'abord ./gradlew setup"
    exit 1
fi

if [ ! -d "$SRC_DIR" ]; then
    echo "❌ Sources introuvables: $SRC_DIR"
    exit 1
fi

# Créer le répertoire de sortie
mkdir -p "$BUILD_DIR"

# Construire le classpath
echo "📦 Construisant le classpath..."
CLASSPATH=""
for jar in "$LOOM_CACHE"/*.jar; do
    if [[ ! "$jar" == *"-sources.jar" ]]; then
        CLASSPATH="$jar:$CLASSPATH"
    fi
done

CLASSPATH="${CLASSPATH%:}"  # Supprimer le dernier ':'
JAR_COUNT=$(echo "$CLASSPATH" | tr ':' '\n' | wc -l)
echo "   $JAR_COUNT JARs ajoutés au classpath"

# Compiler
echo ""
echo "🔨 Compilation des sources Java..."
echo "   Source: $SRC_DIR"
echo "   Sortie: $BUILD_DIR"
echo ""

javac \
    -source 17 \
    -target 17 \
    -cp "$CLASSPATH" \
    -d "$BUILD_DIR" \
    -encoding UTF-8 \
    $(find "$SRC_DIR" -name "*.java") \
    2>&1 | head -50

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Compilation réussie!"
    echo "   Classes générées dans: $BUILD_DIR"
    echo ""
    echo "Note: Ce sont des classes uniquement, pas un JAR mod."
    echo "Un buildscript complet (Gradle/Loom) est nécessaire pour créer le JAR final."
else
    echo ""
    echo "❌ Compilation échouée"
    echo "Vérifiez les erreurs ci-dessus"
    exit 1
fi
