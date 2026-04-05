#!/usr/bin/env python3
"""
Téléchargeur alternatif du plugin Fabric Loom
Contourne les problèmes SSL en téléchargeant depuis plusieurs sources
"""

import os
import sys
import urllib.request
import urllib.error
from pathlib import Path
import hashlib

LOOM_VERSION = "1.1.5"
GRADLE_CACHE = Path.home() / ".gradle" / "caches" / "modules-2" / "files-2.1"
LOOM_CACHE = GRADLE_CACHE / "net.fabricmc" / "fabric-loom" / LOOM_VERSION

# Sources alternatives pour télécharger le plugin
SOURCES = [
    "https://repo.maven.apache.org/maven2/net/fabricmc/fabric-loom/1.1.5/fabric-loom-1.1.5.jar",
    "https://my.jetbrains.com/intellij/maven-repo/release/net/fabricmc/fabric-loom/1.1.5/fabric-loom-1.1.5.jar",  
    "https://maven.aliyun.com/repository/public/net/fabricmc/fabric-loom/1.1.5/fabric-loom-1.1.5.jar",
]

def download_file(url, dest_path):
    """Télécharger un fichier avec gestion d'erreurs"""
    print(f"Téléchargement: {url}")
    try:
        urllib.request.urlretrieve(url, dest_path)
        return True
    except (urllib.error.URLError, urllib.error.HTTPError) as e:
        print(f"  ❌ Erreur: {e}")
        return False

def main():
    # Créer le répertoire de cache
    LOOM_CACHE.mkdir(parents=True, exist_ok=True)
    
    jar_path = LOOM_CACHE / f"fabric-loom-{LOOM_VERSION}.jar"
    
    if jar_path.exists():
        print(f"✅ Fabric Loom {LOOM_VERSION} déjà en cache")
        return 0
    
    # Essayer chaque source
    for source in SOURCES:
        if download_file(source, jar_path):
            print(f"✅ {jar_path} téléchargé avec succès")
            return 0
    
    print("❌ Impossible de télécharger Fabric Loom depuis toutes les sources")
    print(f"Répertoire cache attendu: {LOOM_CACHE}")
    return 1

if __name__ == "__main__":
    sys.exit(main())
