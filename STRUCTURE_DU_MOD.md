# 📦 Structure du Mod Steve's Carts 3 - Minecraft 1.19

## 📋 Vue d'ensemble

**Steve's Carts Reborn** est un mod Minecraft pour la version **1.19** qui permet aux joueurs de créer et personnaliser des minecarts modulaires pour automatiser des tâches. C'est un mod Fabric basé sur Java.

### Infos du projet
- **Nom**: Steves Carts 3
- **Version du mod**: 3.0.0
- **Version Minecraft**: 1.19
- **Type**: Mod Fabric
- **Licence**: MIT
- **Langage**: Java 17
- **Repository**: https://github.com/TechReborn/StevesCarts

---

## 🗂️ Structure racine

### Fichiers de configuration
| Fichier | Description |
|---------|-------------|
| **build.gradle** | Configuration de compilation Gradle, dépendances, plugins |
| **settings.gradle** | Configuration du projet Gradle multi-modules |
| **gradle.properties** | Variables de configuration (version, nom de base d'archives) |
| **fabric.mod.json** | Manifest du mod Fabric (dépendances, points d'entrée, métadonnées) |
| **gradlew / gradlew.bat** | Scripts de lancement de Gradle (Windows & Unix) |

### Fichiers de documentation
| Fichier | Description |
|---------|-------------|
| **README.md** | Documentation principale du mod |
| **LICENSE** | Licence MIT |

### Scripts utilitaires (.bat & .ps1)
| Script | Objectif |
|--------|---------|
| **add_imports.bat** | Ajouter automatiquement les imports Java |
| **add_texthelper_imports.ps1** | Ajouter les imports pour TextHelper (PowerShell) |
| **fix_bom.ps1 / fix_bom_improved.ps1** | Corriger l'encodage BOM des fichiers |
| **fix_text_api.bat** | Corriger les problèmes d'API texte |
| **remove_bom.ps1** | Retirer les BOM des fichiers |

---

## 📁 Répertoires principaux

### 🔨 `/build` - Répertoire de compilation
**Contient les artefacts générés lors de la compilation**

```
build/
├── classes/java/main/         # Classes compilées (.class)
├── generated/sources/          # Code source généré
│   ├── annotationProcessor/   # Résultats du traitement des annotations
│   └── headers/               # Headers générés pour JNI (si applicable)
├── resources/main/            # Ressources du mod (assets, data, lang)
├── libs/                       # Bibliothèques JAR
├── devlibs/                    # Bibliothèques de développement
├── loom-cache/                 # Cache de Loom (remappage de bytecode Minecraft)
└── tmp/                        # Fichiers temporaires de compilation
    ├── compileJava/
    ├── jar/
    ├── remapJar/              # JAR recompilé après remappage
    ├── remapSourcesJar/
    └── sourcesJar/            # Archive des sources
```

**Objectif**: Tous les fichiers compilés et générés par le processus de build Gradle.

---

### 💻 `/src/main/java` - Code source Java

**Structure du mod**

```
src/main/java/vswe/stevescarts/
├── StevesCarts.java                 # Point d'entrée principal du mod
├── block/                           # Blocs personnalisés
│   ├── Registres de blocs
│   ├── Modèles de blocs
│   └── Propriétés des blocs
├── client/                          # Code côté client (uniquement)
│   ├── StevesCartsClient.java      # Entrée client
│   ├── Renderers                    # Systèmes de rendu des minecarts
│   ├── Screens                      # Interfaces graphiques (GUIs)
│   └── Textures & Models            # Assets graphiques
├── entity/                          # Entités personnalisées
│   ├── Minecarts modulaires
│   ├── Logique de mouvement
│   └── Interactions avec joueurs
├── item/                            # Items personnalisés
│   ├── Modules de minecart
│   ├── Outils de configuration
│   └── Registres d'items
├── module/                          # 🎯 Cœur du mod
│   ├── Classe abstraite Module
│   ├── Types de modules (mineur, agricole, etc.)
│   ├── Propriétés modulables
│   └── Système d'upgrades
├── mixin/                           # Mixins Fabric
│   ├── Patches bytecode au runtime
│   ├── Modifie le comportement de Minecraft
│   └── Hooks pour minecarts vanille
├── screen/                          # Écrans (GUIs)
│   ├── Interface de configuration
│   ├── Inventaires personnalisés
│   └── Widgets
├── data/                            # Datagen (génération de données)
│   ├── StevesCartsDatagen.java     # Point d'entrée datagen
│   ├── Générateurs de recettes
│   ├── Générateurs de loot tables
│   ├── Générateurs d'advancements
│   └── Tags personnalisés
└── util/                            # Classes utilitaires
    ├── Helpers
    ├── Gestion des configs
    ├── Sérialisation/Désérialisation
    └── Outils math et conversion
```

**Objectif**: Tout le code Java du mod (logique métier, entités, blocs, items, interfaces).

---

### 📦 `/src/main/resources` - Ressources du mod

**Configuration du mod**

```
src/main/resources/
├── fabric.mod.json                  # Manifest Fabric (copié au build)
├── stevescarts.mixins.json          # Configuration des Mixins
├── stevescarts.accesswidener        # Accès aux champs/méthodes privées
│
├── assets/stevescarts/              # 🎨 Ressources côté client
│   ├── lang/                        # Fichiers de traduction (JSON)
│   │   ├── en_us.json              # Anglais (clés et traductions)
│   │   ├── fr_fr.json              # Français (si disponible)
│   │   └── [autres langues...]
│   ├── models/                      # Modèles 3D (JSON)
│   │   ├── block/                  # Modèles de blocs
│   │   ├── item/                   # Modèles d'items
│   │   └── entity/                 # Modèles d'entités
│   ├── blockstates/                # États des blocs (variantes)
│   │   └── [blocs].json
│   └── textures/                    # Textures PNG
│       ├── block/                  # 64x64, 128x128...
│       ├── item/
│       ├── entity/
│       └── gui/
│
└── data/stevescarts/                # 🔧 Données du mod
    ├── recipes/                     # Recettes d'artisanat (JSON)
    │   ├── Modules
    │   ├── Items
    │   └── Blocs
    ├── loot_tables/                 # Tables de loot
    │   └── [types_entités].json
    ├── advancements/                # Succès/Advancements
    ├── tags/                        # Tags d'items/blocs
    └── [autres données...]
```

**Objectif**: Assets (textures, modèles), données de jeu (recettes, loot), traductions.

---

### 📄 `/src/main/generated` - Ressources générées (Datagen)

**Auto-généré par le système Datagen**

```
src/main/generated/
├── assets/stevescarts/
│   ├── blockstates/                 # États générés
│   └── models/                      # Modèles générés
│
└── data/stevescarts/
    ├── recipes/                     # Recettes générées
    ├── loot_tables/                 # Tables de loot générées
    └── advancements/                # Advancements générés
```

**Objectif**: Éviter de dupliquer les données entre `src/` et `build/`. Le datagen génère les fichiers JSON.

---

### 📚 `/build/resources/main` - Ressources compilées

**Copie des ressources après compilation**

```
build/resources/main/
├── fabric.mod.json                  # Manifest final du JAR
├── stevescarts.mixins.json
├── stevescarts.accesswidener
├── assets/stevescarts/              # Ressources copiées de src/main/resources/
└── data/stevescarts/                # Données du mod
```

**Objectif**: Ressources prêtes à être empaquetées dans le JAR final.

---

### 📖 `/docs` - Documentation du projet

```
docs/
├── module_checklist.md              # Liste de contrôle des modules implémentés
└── progress.md                      # Progression du développement
```

**Objectif**: Suivi du développement et des tâches.

---

### 🔙 `/src_old` - Code ancien (Archive)

**Ancienne structure du mod (probablement v2 ou branche)**

```
src_old/
├── build.gradle                     # Build ancien
├── gradle.properties
└── src/main/java/vswe/stevescarts/  # Code ancien à archiver
```

**Objectif**: Archivage, ne pas toucher sauf pour consultation.

---

## 🔄 Flux de compilation (Build Process)

```
┌─────────────────────────────────────┐
│  Source Code (src/main/java)        │  + Resources (src/main/resources)
└────────────┬────────────────────────┘
             │
             ▼
┌─────────────────────────────────────┐
│  1. Compilation Java (javac)        │
│     → build/classes/java/main/      │
└────────────┬────────────────────────┘
             │
             ▼
┌─────────────────────────────────────┐
│  2. Datagen (Générer recettes, etc) │
│     → src/main/generated/           │
└────────────┬────────────────────────┘
             │
             ▼
┌─────────────────────────────────────┐
│  3. Appliquer Mixins                │
│     (Patcher bytecode Minecraft)    │
└────────────┬────────────────────────┘
             │
             ▼
┌─────────────────────────────────────┐
│  4. Copier Ressources               │
│     → build/resources/main/         │
└────────────┬────────────────────────┘
             │
             ▼
┌─────────────────────────────────────┐
│  5. Créer JAR                       │
│     → build/libs/stevescarts-*.jar  │
└────────────┬────────────────────────┘
             │
             ▼
┌─────────────────────────────────────┐
│  6. Remapper (Yarn ↔ Obfuscated)    │
│     → build/libs/stevescarts-*-     │
│        remapped.jar (DEV)           │
└─────────────────────────────────────┘
```

---

## 🎯 Architecture du mod

### Points d'entrée (Entrypoints)

Définis dans `fabric.mod.json`:

| Point d'entrée | Classe | Rôle |
|---|---|---|
| **main** | `vswe.stevescarts.StevesCarts` | Initialisation du mod (côté serveur) |
| **client** | `vswe.stevescarts.client.StevesCartsClient` | Initialisation côté client (renderers, screens) |
| **fabric-datagen** | `vswe.stevescarts.data.StevesCartsDatagen` | Génération de données (recettes, loot...) |

### Système Modulaire

Le cœur du mod repose sur un **système modulaire**:

1. **Minecart de base** (`entity/`)
2. **Modules** (`module/`) - Les composants (mineur, fermier, etc.)
3. **Items** (`item/`) - Les modules se craftent en items
4. **Rendering** (`client/`) - Affichage custom des minecarts modulaires

### Mixins

Les **mixins** (`mixin/`) patchen le code Minecraft au runtime pour:
- Hooker les entités minecart vanille
- Modifier le rendu
- Changer le comportement

---

## 📝 Résumé des dossiers clés

| Dossier | Contenu | But |
|---------|---------|-----|
| **block/** | Classes de blocs | Créer des blocs custom |
| **client/** | Renderers, Screens, GUI | Interface utilisateur & affichage |
| **entity/** | Classes d'entités | Minecarts custom, entités |
| **item/** | Classes d'items | Items craftables (modules) |
| **module/** | ⭐ Logique des modules | Cœur du système modulaire |
| **mixin/** | Patches bytecode | Modifier Minecraft sans remplacer classes |
| **screen/** | GUIs | Interfaces (configuration, inventaires) |
| **data/** | Datagen | Générer recettes, loot, advancements |
| **util/** | Helpers & outils | Code réutilisable |
| **assets/** | Textures, modèles, lang | Graphismes & traductions |
| **data/stevescarts/** | Recettes, loot, tags | Données de jeu |

---

## 🛠️ Comment compiler le mod

```bash
# Compiler le mod
./gradlew build

# Résultats:
# - build/libs/stevescarts-3.0.0.jar (obfusqué)
# - build/libs/stevescarts-3.0.0-remapped.jar (pour développement)
# - build/libs/stevescarts-3.0.0-sources.jar (sources)
```

---

## 🔗 Dérivés de compilation

- **JAR dev**: Placez `build/libs/stevescarts-3.0.0-remapped.jar` dans `~/.minecraft/mods/`
- **JAR release**: Distribuez `build/libs/stevescarts-3.0.0.jar` sur CurseForge

---

**Créé**: février 2026  
**Mod**: Steve's Carts Reborn v3.0.0 (Minecraft 1.19 - Fabric)

---

## 📚 Documents de référence

- [ROADMAP.md](ROADMAP.md) - **Plan de développement complet** (Phases 1-4)
- [ETAT_PROGRESSION.md](ETAT_PROGRESSION.md) - **État d'avancement actualisé** avec stats
- [GUIDE_PORTAGE.md](GUIDE_PORTAGE.md) - **Comment adapter les modules** v1.12.2→v1.19
- [docs/module_checklist.md](docs/module_checklist.md) - **Checklist de tous les modules**
