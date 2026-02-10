# ⚡ QUICKSTART - Steve's Carts 3 v1.19

**Démarrer en 5 min**.

---

## 🎯 C'est quoi?

Recréer le meilleur mod Minecraft de minecarts (**v1.12.2**) en version moderne (**Fabric 1.19**)  
avec innovations.

---

## 📊 État

```
Actuellement:    35/124 modules (28%)
Objectif final: 124/124 modules (100%) + innovations
Durée estimée:   6-12 mois
```

---

## 🚀 Comment commencer

### Pour ceux qui comprennent le projet déjà
```bash
# 1. Cloner
git clone [repo]
cd StevesCarts-1.19

# 2. Compiler
./gradlew build

# 3. Tester
# JAR: build/libs/StevesCarts-3.0.0-remapped.jar
# Placer dans ~/.minecraft/mods/
# Lancer Minecraft

# 4. Contribuer
git checkout -b feature/port-[ModuleName]
# Voir GUIDE_PORTAGE.md
```

### Pour ceux qui découvrent
```
1. Lire: README_PROJET.md (2 min)
2. Lire: ROADMAP.md - Overview (5 min)
3. Lire: GUIDE_PORTAGE.md (10 min)
4. Choisir: Un module Phase 2
5. Coder: Suivre checklist
6. Test: En jeu
```

---

## 📚 Docs essentielles

| Doc | Pourquoi | Temps |
|-----|----------|-------|
| [README_PROJET.md](README_PROJET.md) | Comprendre mission | 2 min |
| [ROADMAP.md](ROADMAP.md) | Voir plan complet | 10 min |
| [GUIDE_PORTAGE.md](GUIDE_PORTAGE.md) | Apprendre coder | 15 min |
| [INDEX.md](INDEX.md) | Naviguer docs | 5 min |

---

## 💻 Pour porter un module

**Temps**: 2-5h par module simple

```
1. Ouvrir src_old/ 
   → Trouver Module[X].java pour code de référence

2. Lire GUIDE_PORTAGE.md
   → Section "Exemple: Porter le Basic Farmer"

3. Copier classe
   → Adapt imports (Forge → Fabric)
   → Adapt types (NBT, BlockState, etc)

4. Tester
   → ./gradlew build
   → Placer JAR dans mods/
   → Vérifier en jeu

5. Ajouter ressources
   → Recette (datagen)
   → Traduction (lang files)
   → Texture (optional)

6. Commit
   → "[PHASE2] Port: Module Name"
   → Push
   → PR

7. Documenter
   → docs/module_checklist.md [x]
   → TEMPLATE_MODULE.md (si besoin)
```

---

## 🎯 Modules à faire (Phase 2)

**Faciles** (2-3h chacun):
- Compact Solar Engine
- Creative Engine
- Track Remover
- Lawn Mower
- Internal Storage
- Gift Storage

**Moyens** (5-10h chacun):
- Basic Farmer
- Basic Wood Cutter
- Basic Drill
- Smelter
- Crafter

**Détecteurs** (3-5h chacun):
- Entity Detector: Animal
- Entity Detector: Monster
- Entity Detector: Player

→ **Total Phase 2**: 30 modules en 3-5 semaines

---

## 🔥 3 fichiers clés à étudier

### 1. `GUIDE_PORTAGE.md`
```
Lis cette section:
- "Différences Forge 1.12.2 → Fabric 1.19"
- "Checklist de portage d'un module"
- "Exemple: Porter le Basic Farmer"
```

### 2. `src_old/src/main/java/vswe/stevescarts/modules/`
```
C'est votre mine d'or!
- 124 modules complets
- Logique déjà testée
- À adapter seulement
```

### 3. `src/main/java/vswe/stevescarts/module/`
```
Exemples déjà adaptés:
- CoalEngineModule.java
- BasicSolarEngineModule.java
- [autres modules]

→ Pattern à suivre pour autres modules
```

---

## 🧪 Tester en jeu (30 sec)

```bash
# 1. Build
./gradlew build

# 2. Copier JAR
cp build/libs/StevesCarts-3.0.0-remapped.jar \
   ~/.minecraft/mods/

# 3. Lancer Minecraft avec Fabric

# 4. Creative mode
# → Tab → Chercher "stevescarts"
# → Craft un module dans établi
# → Placer dans minecart
```

---

## 📊 Suivi progression

**Chaque mois** (1er du mois):
```
1. Mettre à jour LOG_MENSUEL.md
2. Mettre à jour ETAT_PROGRESSION.md
3. Mettre à jour docs/module_checklist.md
4. Commit & announce
```

**Métriques**:
```
- Modules total: XX/124
- Temps moyen par module: Xh
- Blockers: Aucun / [Liste]
```

---

## ❓ Questions courantes

**Q: Par où commencer?**
A: Lire README_PROJET.md (2 min), puis GUIDE_PORTAGE.md

**Q: C'est difficile?**
A: Non! 70% du travail = copier code v1.12.2 + adapter imports. Suivre le guide.

**Q: Quelle version Java?**
A: Java 17+. Fabric 1.19 nécessite Java 17 minimum.

**Q: Risque de rester incomplet?**
A: Non! Code v1.12.2 existe = copier/adapter. Pas réinventer.

**Q: Peut-on vraiment partir de 28% vers 100%?**
A: Oui! Avec ~5-10 modules/mois = 10-14 mois pour 89 modules. Possible.

---

## 🎓 Ressources

**Fabric API**
- https://fabricmc.net/wiki/en:start
- https://javadoc.io/doc/net.fabricmc

**Minecraft 1.19 API**
- Yarn mappings
- Loom wiki

**Nos docs**
- [INDEX.md](INDEX.md) - Navigation complète
- [STRUCTURE_DU_MOD.md](STRUCTURE_DU_MOD.md) - Technique

---

## ⚡ Commandes rapides

```bash
# Compiler
./gradlew build

# Nettoyer & rebuild
./gradlew clean build

# Générer data
./gradlew runDatagen

# Run mod dev
./gradlew runClient

# Format code
./gradlew formatSourceJava
```

---

## 🏆 Votre checklist perso

Avant de commencer:

```
□ Installé JDK 17+
□ Cloné le repo
./gradlew build réussit
□ Placé JAR dans ~/.minecraft/mods/
□ Testé en jeu = marche
□ Lu README_PROJET.md
□ Lu GUIDE_PORTAGE.md
□ Choisi module à porter
□ Créé branche
□ Commencé code
```

---

## 🚀 Next steps

**Aujourd'hui** (< 1h):
- Compiler le mod
- Tester en jeu

**Demain** (1-2h):
- Lire GUIDE_PORTAGE.md
- Étudier 2-3 modules v1.12.2

**Cette semaine** (5-10h):
- Porter 1 module simple
- Tester en jeu
- PR

**Ce mois** (20-40h):
- Porter 5-10 modules Phase 2
- Atteindre 45/124 (36%)

---

## 📞 Support

**Questions?**
→ Lire [INDEX.md](INDEX.md) pour voir quelle doc

**Bloqué?**
→ Vérifier GUIDE_PORTAGE.md - Problèmes courants

**Idée?**
→ GitHub Discussions

**Bug?**
→ GitHub Issues

---

**C'est parti! À vous de jouer! 🎮**

Plus d'infos → [README_PROJET.md](README_PROJET.md)

---

**Créé**: 10 février 2026  
**Version**: 1.0
