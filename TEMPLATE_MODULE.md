# 📋 TEMPLATE - Documentation module nouvellement porté

**Copier ce template pour chaque module porté et le mettre à jour avec les détails réels.**

---

## [NOM_MODULE] - Module porté de v1.12.2 → v1.19

**Statut**: ✅ Complet / 🔄 En cours / ❌ À faire  
**Date portage**: [DATE]  
**Temps investi**: [TEMPS]h  
**Responsable**: [VOTRE_NOM]

---

### 📌 Informations de base

| Aspect | Détail |
|--------|--------|
| **Nom v1.12.2** | ModuleName.java |
| **Nom v1.19** | NameModule.java |
| **Catégorie** | [Engine/Hull/Storage/Tool/etc] |
| **Dépendances** | Aucune / [Dépendance X] |
| **Complexité** | ⭐ Simple / ⭐⭐ Moyen / ⭐⭐⭐ Complexe |

---

### 📚 Code source

```
v1.12.2 Source:
src_old/src/main/java/vswe/stevescarts/modules/[Category]/ModuleName.java

v1.19 Implementation:
src/main/java/vswe/stevescarts/module/[category]/NameModule.java
```

---

### 🔄 Changements effectués

#### Imports adaptés
```
Forge imports échangés pour:
- net.fabricmc imports
- net.minecraft imports (mise à jour version)
- Tech Reborn APIs
```

#### Types de données modifiés
```
NBTTagCompound → NbtCompound
BlockState → BlockState (compatible)
Entity → Entity (compatible)
TileEntity → BlockEntity
Container → ScreenHandler
GuiContainer → HandledScreen
[autres changements]
```

#### Méthodes renommées
```
update() → tick()
readFromNBT() → readNbt() ou load()
writeToNBT() → writeNbt() ou save()
[autres changements]
```

#### Logique adaptée
```
- Capabilities system → [Implémentation custom]
- Forge Events → Fabric Events
- [autres changements]
```

---

### ✅ Checklist de portage

- [ ] Classe principale copiée et adaptée
- [ ] Tous les imports mis à jour
- [ ] Types de données converti
- [ ] Méthodes cycliques adaptées
- [ ] Logique métier testée
- [ ] Renderer créé (si visuel)
- [ ] Recette datagen créée
- [ ] Traductions ajoutées (FR/EN)
- [ ] Texture item créée (si besoin)
- [ ] Enregistré dans ModuleType
- [ ] Compilé sans erreurs
- [ ] Testé en jeu
- [ ] Merge vers main

---

### 🔧 Implémentation technique

#### Classe principale
```java
public class NameModule extends Module {
    // Description du module
    // Propriétés principales
    // Méthodes clés
}
```

**Fichier**: [Lien vers fichier]

#### Renderer (si visuel)
```java
public class NameModuleRenderer extends ModuleRenderer<NameModule> {
    // Rendu du module
}
```

**Fichier**: [Lien vers fichier]

#### Recette (datagen)
```
Entrée: [Items input]
Sortie: [Item module]
Temps craft: Xm Xs
```

**Fichier**: `src/main/java/vswe/stevescarts/data/...`

---

### 🌐 Traductions

**Clés ajoutées**:
- `module.stevescarts.[name].name` = "Nom du module"
- `module.stevescarts.[name].info` = "Description"

**Fichiers mis à jour**:
- `src/main/resources/assets/stevescarts/lang/en_us.json`
- `src/main/resources/assets/stevescarts/lang/fr_fr.json`

---

### 🎨 Ressources graphiques

**Texture item**:
- `src/main/resources/assets/stevescarts/textures/item/[name].png`
- Résolution: 16x16 (ou legacy: 64x64)

**Modèle (si custom)**:
- `src/main/resources/assets/stevescarts/models/item/[name].json`

**Blockstate (si bloc)**:
- N/A pour modules

---

### 🧪 Tests en jeu

#### Critères de succès
- [ ] Module peut être crafté
- [ ] Module peut être placé dans cart
- [ ] Fonctionnalité primaire marche
- [ ] Fonctionnalité secondaire marche
- [ ] Pas d'erreur logs
- [ ] Performance acceptable

#### Procédure test
```
1. Compiler mod
2. Placer JAR dans ~/.minecraft/mods/
3. Lancer Minecraft avec Fabric Loader
4. Créer world créatif
5. Crafted le module
6. Tester via Cart Assembler
7. Vérifier logs pour erreurs
8. Documenter résultats
```

#### Résultats de test
```
Test Date: [DATE]
Tester: [NOM]
Build: [VERSION]

✅ Craft works
✅ Placement works
✅ Primary functionality works
⚠️ Note: [Any issues]
```

---

### 🐛 Problèmes rencontrés et solutions

| Problème | Solution | Statut |
|----------|----------|--------|
| [Problème 1] | [Solution apportée] | ✅ Résolu |
| [Problème 2] | [Solution apportée] | ✅ Résolu |

---

### 📊 Comparaison v1.12.2 vs v1.19

| Aspect | v1.12.2 | v1.19 | Notes |
|--------|---------|-------|-------|
| Fonctionnalité | [Desc] | [Desc] | Parity? |
| Performance | [Perf] | [Perf] | Amélioré? |
| API | [API] | [API] | Changements? |

---

### 📈 Métriques de portage

```
Effort:
- Étude code v1.12.2: Xh
- Adaptation Fabric: Xh
- Tests & debug: Xh
- Documentation: Xh
- TOTAL: Xh

Pourcentage du module:
- Code logic: XX%
- Tests: XX%
- Documentation: XX%
- Ressources (textures, etc): XX%
```

---

### 🔗 Ressources utilisées

- **Source v1.12.2**: [Chemin src_old/]
- **Documentation**: [GUIDE_PORTAGE.md](GUIDE_PORTAGE.md)
- **Références**: [Autre modules similaires]
- **API Fabric**: [Documentation]

---

### 📝 Commit message

```
[PHASE2] Port: Module Name

- Logique métier transposée depuis v1.12.2
- Adaptation Fabric 1.19 complète
- Tests en jeu réussis
- Recettes datagen
- Traductions FR/EN

Performance: [Note]
Compatibility: [Fabric X.X, Minecraft 1.19+]

Closes: #XX (si applicable)
```

---

### 📋 Notes additionnelles

**Ce qui a bien marché**:
- [Processus X était fluide]
- [Adaptation Y était directe]

**Difficultés**:
- [Problème avec feature X]
- [Manque de documentation Y]

**Améliorations futures**:
- [Feature bonus à ajouter]
- [Optimization Z possible]

---

### ✨ Innovations Fabric ajoutées

**Au-delà de v1.12.2**:
- [ ] Utilisation event API Fabric
- [ ] Datagen au lieu de hardcoding
- [ ] Support datapack
- [ ] Optimizations modernes
- [ ] Autre: [Détail]

---

### ✅ Vérification finale

Avant de merger:

- [ ] Compile sans erreurs
- [ ] Compile sans warnings
- [ ] Tests en jeu réussis
- [ ] Documentation complète
- [ ] Code suits conventions
- [ ] Commit message clair
- [ ] PR description complète
- [ ] ETAT_PROGRESSION.md mis à jour
- [ ] module_checklist.md mis à jour
- [ ] Pas de TODOs abandonnés

---

### 📚 Related files

**À mettre à jour après ce module**:
1. `docs/module_checklist.md` - Marquer [x]
2. `ETAT_PROGRESSION.md` - Stats mises à jour
3. `LOG_MENSUEL.md` - Ajouter dans entrée mois
4. `ROADMAP.md` - Si major change

---

### 🎓 Leçons apprises

```
Ce que j'ai appris en portant ce module:
1. [Leçon technique X]
2. [Processus découvert Y]
3. [Pattern à réutiliser Z]
4. [À éviter pour prochains modules]
```

---

**Créé**: [DATE]  
**Complété**: [DATE]  
**Reviewer**: [NOM]  
**Status**: ✅ Merged / 🔄 Review / ❌ Draft
