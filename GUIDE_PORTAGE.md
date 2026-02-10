# 🔄 Guide de Portage v1.12.2 → v1.19

## 📊 Résumé de la situation

**Vous avez une mine d'or dans `src_old/`!**

### Chiffres clés
- ✅ **Source originale (v1.12.2 - Forge)**: 124 modules COMPLETS
- ✅ **Code de référence**: Prêt à être "traduit" vers Fabric 1.19
- ⏳ **Code 1.19 actuel**: Seulement 35 modules portés (28%)
- 📋 **Modules à porter**: 89 modules restants

---

## 🔧 Différences Forge 1.12.2 → Fabric 1.19

### 1️⃣ Framework
| Aspect | 1.12.2 (Forge) | 1.19 (Fabric) |
|--------|---|---|
| Modloader | **Forge** | **Fabric** |
| Build system | **Gradle 2.x** (MCP) | **Gradle 7.x** (Loom) |
| Mappings | **MCP Stable_39** | **Yarn** |
| Java | Java 8 | Java 17 |

**Impact**: ~30-40% du code à adapter (imports, APIs)

### 2️⃣ Structures principales à changer

#### Registries (Enregistrement)
```java
// ❌ v1.12.2 (Forge)
GameRegistry.register(new Block(...));
GameRegistry.register(new Item(...));

// ✅ v1.19 (Fabric)
Registry.register(Registries.BLOCK, new Identifier(...), new Block(...));
Registry.register(Registries.ITEM, new Identifier(...), new Item(...));
```

#### Événements (Events)
```java
// ❌ v1.12.2 (Forge)
@Mod.EventBusSubscriber
public class MyEvents {
    @SubscribeEvent
    public static void onUpdate(PlayerTickEvent e) { }
}

// ✅ v1.19 (Fabric)
ServerTickEvents.END_SERVER_TICK.register(server -> {
    // Logique
});
```

#### Containers/Screens
```java
// ❌ v1.12.2 (Forge)
class MyContainer extends Container { }
class MyScreen extends GuiContainer { }

// ✅ v1.19 (Fabric)
class MyScreenHandler extends ScreenHandler { }
class MyScreen extends HandledScreen<MyScreenHandler> { }
```

#### NBT (Données persistantes)
```java
// ❌ v1.12.2 - Forge
NBTTagCompound nbt = new NBTTagCompound();
nbt.setInteger("key", value);

// ✅ v1.19 - Fabric
NbtCompound nbt = new NbtCompound();
nbt.putInt("key", value);
```

#### Capabilities (Forge) → Interfaces (Fabric)
```java
// ❌ v1.12.2 - Forge
entity.getCapability(ENERGY_CAP, direction);

// ✅ v1.19 - Fabric
// Utiliser des interfaces ou des attachements personnalisés
```

### 3️⃣ Systèmes à remplacer

| v1.12.2 | v1.19 |
|---------|-------|
| **Capabilities** | Custom interfaces / Fabric API |
| **Fluids (IFluidHandler)** | FluidStorage, FluidVariant |
| **Energy (IEnergyStorage)** | Tech Reborn Energy API |
| **Packets (NetworkRegistry)** | ServerPlayNetworking, ClientPlayNetworking |
| **Rendering (TileEntitySpecialRenderer)** | BlockEntityRenderer |
| **Item Properties (IItemPropertyGetter)** | Fabric API |

---

## 📁 Structure de `src_old/`

```
src_old/src/main/java/vswe/stevescarts/
├── modules/
│   ├── engines/           # Moteurs (Coal, Solar, etc.)
│   ├── hull/              # Coques
│   ├── storage/           # Stockage (chests, tanks)
│   ├── workers/           # Outils (torch, rail, drill, etc.)
│   ├── upgrades/          # Améliorations
│   ├── workers/
│   │   ├── tools/         # Outils de minage
│   │   ├── farming/       # Modules de culture
│   │   ├── detectors/     # Détecteurs
│   │   ├── weapons/       # Armes
│   │   └── ...
│   └── [123 fichiers Module*.java] 🎯
├── blocks/                # Blocs spécialisés
├── containers/            # Interfaces (GUIs)
├── entitys/               # Entités minecarts
├── handlers/              # Logique d'événements
├── api/                   # API publique
└── ...
```

**C'est votre guide de portage!** Chaque module v1.12.2 peut servir de référence.

---

## 🎯 Stratégie de portage recommandée

### Phase 1: Fondations (FAIT ✅)
- ✅ Structure Fabric de base
- ✅ System de modules (classe abstraite Module)
- ✅ Registries (blocs, items, entités)
- ✅ Cart Assembler (bloc spécialisé)

### Phase 2: Modules simples (À faire)
**Modules sans dépendances externes, faciles à adapter**

```
1. Moteurs supplémentaires (2-3h par moteur)
   - Compact Solar Engine
   - Creative Engine

2. Coques supplémentaires (1-2h par coque)
   - Creative Tank
   - Creative Supplies

3. Stockage (fluides) (4-6h)
   - Internal Tank
   - Internal Storage

4. Outils simples (2-3h chacun)
   - Track Remover
   - Lawn Mower
```

### Phase 3: Modules complexes (À faire)
**Modules nécessitant une logique nouvelle ou des dépendances**

```
1. Systèmes de forage (15-20h total)
   - Basic Drill → Iron Drill → Hardened Drill
   - Galgadorian Drill (versions avancées)

2. Fermage (15-25h total)
   - Basic Farmer
   - Basic Wood Cutter
   - Hydrator, Height Controller, Fertilizer
   - Galgadorian variants

3. Détecteurs (10-15h total)
   - Entity Detectors (Animal, Monster, Player, etc.)
   - Fluid Sensors
```

### Phase 4: Systèmes spécialisés (À faire)
**Blocs et systèmes complexes**

```
1. Blocs spécialisés (20-40h)
   - Cargo Manager
   - Detector Manager
   - Junction Rail
   
2. Systèmes d'armes (15-25h)
   - Shooters
   - Projectiles
   - Dynamite Carrier

3. Traitement (15-25h)
   - Smelter, Advanced Smelter
   - Freezer, Liquid Cleaner
   - Enchanter
```

---

## 💡 Comment utiliser `src_old/` pour le portage

### Exemple: Porter le module "Basic Farmer"

**1. Trouvez le code source v1.12.2**
```
src_old/src/main/java/vswe/stevescarts/modules/workers/farming/ModuleBasicFarmer.java
```

**2. Étudiez la structure**
```java
public class ModuleBasicFarmer extends Module {
    private int cropType;
    private boolean rightClickSeed;
    
    @Override
    public void update() { }  // Logique de culture
    
    @Override
    public void readFromNBT(NBTTagCompound nbt) { }
}
```

**3. Adaptez pour Fabric 1.19**
```java
public class BasicFarmerModule extends Module {
    private int cropType;
    private boolean rightClickSeed;
    
    @Override
    public void tick() { }  // Fabric utilise tick() pas update()
    
    @Override
    public void writeNbt(NbtCompound nbt) { }  // NbtCompound pas NBTTagCompound
}
```

**4. Changements typiques**
- `NBTTagCompound` → `NbtCompound`
- `update()` → `tick()`
- `IBlockState` → `BlockState`
- `EntityPlayer` → `PlayerEntity`
- `WorldServer` → `ServerWorld`
- Imports Forge → Imports Fabric

---

## 🚀 Checklist de portage d'un module

Pour chaque module à porter, vérifiez:

```
□ Trouver le code v1.12.2 dans src_old/
□ Copier la classe Module et renommer
□ Adapter les imports (Forge → Fabric)
□ Adapter les types de données (NBT, BlockState, etc.)
□ Adapter les méthodes cycliques (update → tick)
□ Créer le renderer (si nécessaire)
□ Créer la recette (datagen)
□ Ajouter les traductions (lang files)
□ Ajouter la texture de l'item
□ Enregistrer dans ModuleType
□ Tester en jeu
□ Mettre à jour module_checklist.md
```

---

## 🔍 Problèmes courants lors du portage

### ❌ Problème 1: Imports Forge incompatibles
```java
// ❌ Ne compilera pas
import net.minecraftforge.event.TickEvent;

// ✅ Solution Fabric
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
```

### ❌ Problème 2: Capabilities inexistants
```java
// ❌ v1.12.2 (Forge)
entity.getCapability(FLUID_HANDLER_CAPABILITY, direction);

// ✅ v1.19 (Fabric) - Utiliser des interfaces custom
interface FluidStorage {
    int insert(Fluid fluid, int amount);
}
```

### ❌ Problème 3: APIs dépréciées
```java
// ❌ Déprecié
GuiScreen, Container

// ✅ Remplacé par
Screen, ScreenHandler
```

### ✅ Solutions
- Utiliser **LibGUI** (déjà dans les deps) pour les interfaces
- Utiliser **TechReborn Core** pour l'énergie
- Utiliser **Fabric API** pour les événements

---

## 📊 Ressources de portage disponibles

| Ressource | Emplacement | Utilité |
|-----------|-----------|---------|
| Code complet v1.12.2 | `src_old/` | Logique de référence |
| Code v1.19 partiel | `src/` | Patterns déjà adaptés |
| Dependencies | `build.gradle` | APIs modernes |
| Module checklist | `docs/module_checklist.md` | Suivi des tâches |

---

## ⏱️ Estimation réaliste de portage

### Par module simple (Moteur, Coque, etc.)
- **Temps**: 2-4h
- **Complexité**: ⭐⭐
- **Dépendances externes**: Peu

### Par module moyen (Drill, Farmer, etc.)
- **Temps**: 5-10h
- **Complexité**: ⭐⭐⭐
- **Dépendances externes**: Quelques-unes

### Par module complexe (Shooter, Detector, etc.)
- **Temps**: 8-15h
- **Complexité**: ⭐⭐⭐⭐
- **Dépendances externes**: Beaucoup

### Par bloc spécialisé
- **Temps**: 10-20h
- **Complexité**: ⭐⭐⭐⭐⭐
- **Dépendances externes**: API personnalisée nécessaire

---

## 🎓 Recommandations finales

✅ **Vous POUVEZ porter le mod complet!**

Vous avez:
1. Le code source complet (src_old/)
2. Les fondations adaptées (src/)
3. Les dépendances modernes (build.gradle)

**Prochaines étapes**:
1. Choisir 5-10 modules simples à porter en priorité
2. Établir un pattern de portage clair
3. Documenter chaque changement
4. Tester en jeu régulièrement
5. Mettre à jour la checklist

**Temps estimé pour complétude**: 2-4 mois (1 dev temps plein)

---

**Source**: Analyse de `src_old/` (124 modules Forge 1.12.2)  
**Généré**: Février 2026
