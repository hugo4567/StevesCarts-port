# 📑 INDEX - Tous les documents du projet

Bienvenue dans Steve's Carts 3 v1.19! Voici où trouver chaque information.

---

## 🚀 Premiers pas (LIRE EN PREMIER)

### Pour comprendre le projet
1. **[README_PROJET.md](README_PROJET.md)** ⭐ START HERE
   - Qu'est-ce que c'est?
   - État actuel vs objectif
   - Phase par phase
   - Comment contribuer

2. **[ROADMAP.md](ROADMAP.md)** - Plan détaillé
   - 4 phases du développement
   - Timeline prévue
   - Ressources nécessaires
   - Checkpoints clés

---

## 📊 Suivi et progression

### Pour voir l'état
3. **[ETAT_PROGRESSION.md](ETAT_PROGRESSION.md)**
   - Stats actuelle (35/124 modules)
   - Détail par catégorie
   - Modules manquants listés
   - Estimation temps restant
   - Comparaison v1.12.2

4. **[LOG_MENSUEL.md](LOG_MENSUEL.md)**
   - Progression mois par mois
   - Février 2026 = état actuel
   - Template pour futures updates
   - Métriques de santé du projet

5. **[docs/module_checklist.md](docs/module_checklist.md)**
   - ✅ Modules complétés
   - ❌ Modules à faire
   - État blocs spécialisés
   - À mettre à jour chaque mois

---

## 🔧 Documentation technique

### Pour développeurs
6. **[STRUCTURE_DU_MOD.md](STRUCTURE_DU_MOD.md)**
   - Arborescence complète du projet
   - Rôle de chaque dossier/fichier
   - Système d'entrypoints
   - Architecture modulaire
   - Flux de compilation

7. **[GUIDE_PORTAGE.md](GUIDE_PORTAGE.md)** ⭐ ESSENTIAL POUR CODER
   - Différences Forge 1.12.2 vs Fabric 1.19
   - Comment adapter un module
   - Checklist de portage
   - Problèmes courants & solutions
   - Exemples code-to-code

---

## 📁 Répertoires importants

### Code source
- **`src/main/java/vswe/stevescarts/`** - Code v1.19 actuel (35 modules)
- **`src_old/src/main/java/vswe/stevescarts/`** - Source v1.12.2 (124 modules) ⭐ RÉFÉRENCE

### Données
- **`src/main/resources/`** - Textures, modèles, recettes, traductions
- **`src/main/generated/`** - Fichiers auto-générés (datagen)
- **`docs/`** - Documentation du projet

### Configuration
- **`build.gradle`** - Dépendances, plugins, build config
- **`gradle.properties`** - Variables de version
- **`fabric.mod.json`** - Manifest du mod

---

## 🎯 Par type de tâche

### Je veux COMPRENDRE le projet
```
1. README_PROJET.md (5 min)
2. ROADMAP.md - Phase 1 (10 min)
3. STRUCTURE_DU_MOD.md (15 min)
```

### Je veux PORTER un module
```
1. GUIDE_PORTAGE.md (lecture complète)
2. Trouver code v1.12.2 dans src_old/
3. Suivre checklist de portage
4. Tester en jeu
5. Mettre à jour docs/module_checklist.md
```

### Je veux CONTRIBUER
```
1. README_PROJET.md (comprendre la vision)
2. ROADMAP.md - Phase 2 (voir modules prioritaires)
3. GUIDE_PORTAGE.md (apprendre la technique)
4. Fork repo & créer branche
5. Porter 1 module simple comme test
```

### Je veux TRACKER les progrès
```
1. ETAT_PROGRESSION.md (stats actuelles)
2. LOG_MENSUEL.md (logs détaillées)
3. docs/module_checklist.md (checklist complète)
4. GitHub Issues (blockers)
```

### Je veux INNOVER
```
1. ROADMAP.md - Phase 4 (ideas futures)
2. GUIDE_PORTAGE.md - Stratégie innovation
3. Fabric API documentation
4. Proposer idées dans GitHub Discussions
```

---

## 📚 Tous les documents

| Document | Objectif | Audience | Lire si... |
|----------|---------|----------|-----------|
| **README_PROJET.md** | Synthèse du projet | Tous | Nouveau venu |
| **ROADMAP.md** | Plan 4 phases | Tous | Besoin de timeline |
| **ETAT_PROGRESSION.md** | Stats & progress | Tous | Vérifier état |
| **STRUCTURE_DU_MOD.md** | Vue technique | Devs | Exploring codebase |
| **GUIDE_PORTAGE.md** | Comment coder | Devs | Vais porter un module |
| **LOG_MENSUEL.md** | Progress tracking | Tous | Updates mensuelles |
| **docs/module_checklist.md** | Checklist modules | Tous | Voir ce qui reste |
| **docs/progress.md** | General notes | Tous | Notes hétéroclites |
| **ARCHITECTURE.md** | Design patterns | Devs | Comprendre patterns |
| **API.md** (futur) | Developer API | Modders | Extend le mod |

---

## 🔄 Workflow de développement

### Chaque jour
```
→ Lire GUIDE_PORTAGE.md pour module du jour
→ Coder/tester
→ Commit avec message clair
```

### Chaque semaine
```
→ Check docs/ pour tracking
→ Mettre à jour module_checklist.md si changement
→ PR si module complet
```

### Chaque mois (1er du mois)
```
→ Créer entrée LOG_MENSUEL.md
→ Mettre à jour ETAT_PROGRESSION.md
→ Mettre à jour ROADMAP.md si ajustements
→ Publish release si applicable
→ Annoncer progrès
```

---

## 🏆 Checklist de démarrage

Si vous commencez maintenant:

```
□ Lire README_PROJET.md (5 min)
□ Lire ROADMAP.md - Phases 1-2 (20 min)
□ Lire GUIDE_PORTAGE.md (30 min)
□ Cloner repo & compiler
  ./gradlew build
□ Tester avec build/libs/StevesCarts-3.0.0-remapped.jar
□ Choisir 1 module Phase 2 simple
  Suggestion: Compact Solar Engine
□ Créer branche
  git checkout -b feature/port-compact-solar
□ Commencer portage
□ Documenter progrès
```

**Temps total**: ~2-3h pour être opérationnel

---

## 🆘 Besoin d'aide?

### Je veux comprendre X
- **Module system**: STRUCTURE_DU_MOD.md + GUIDE_PORTAGE.md
- **Build process**: STRUCTURE_DU_MOD.md (section Build Process)
- **Fabric differences**: GUIDE_PORTAGE.md (section Différences)
- **Project timeline**: ROADMAP.md + LOG_MENSUEL.md
- **What to do next**: README_PROJET.md (section Prochaines étapes)

### Je suis bloqué
1. Vérifier GUIDE_PORTAGE.md - Problèmes courants
2. Vérifier src_old/ pour code de référence
3. Demander dans GitHub Issues
4. Check Fabric documentation

### Je veux proposer une idée
→ GitHub Discussions ou Issues

---

## 📞 Communication

- **Code**: GitHub repo (commits, PRs)
- **Issues**: Blockers & bugs
- **Discussions**: Ideas & feedback
- **Releases**: Versions jouables
- **Monthly**: LOG_MENSUEL.md updates

---

## 🚀 Quick Links

**Pour commencer**: [README_PROJET.md](README_PROJET.md)  
**Pour coder**: [GUIDE_PORTAGE.md](GUIDE_PORTAGE.md)  
**Pour tracker**: [ETAT_PROGRESSION.md](ETAT_PROGRESSION.md)  
**Pour planifier**: [ROADMAP.md](ROADMAP.md)  
**Pour contribuer**: Fork + [GUIDE_PORTAGE.md](GUIDE_PORTAGE.md)  

---

## 📈 Arborescence des documents

```
StevesCarts-1.19/
├── README.md (original mod)
├── README_PROJET.md ⭐ START HERE
├── ROADMAP.md (4 phases plan)
├── ETAT_PROGRESSION.md (stats)
├── STRUCTURE_DU_MOD.md (technique)
├── GUIDE_PORTAGE.md (dev guide)
├── LOG_MENSUEL.md (tracking)
├── INDEX.md (ce fichier)
├── ARCHITECTURE.md (futur)
├── API.md (futur)
├── docs/
│   ├── module_checklist.md (ALL modules)
│   ├── progress.md (notes)
│   └── development/
│       ├── patterns.md (futur)
│       ├── testing.md (futur)
│       └── deployment.md (futur)
└── ...code...
```

---

**Créé**: 10 février 2026  
**Dernière mise à jour**: 10 février 2026  
**Version**: 1.0

**Conseil**: Bookmarkez cette page pour y revenir facilement! 🔖
