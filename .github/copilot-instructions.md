# Steve's Carts 3 - Development Guide

## Overview

**Steve's Carts Reborn** is a Minecraft 1.19 Fabric mod that allows players to create and customize modular minecarts for automation. This is a port from Forge 1.12.2 to Fabric 1.19, modernizing a 124-module codebase.

**Current Status**: 35/124 modules ported (28%)  
**Goal**: Full parity with v1.12.2 + modern Fabric improvements

## Build & Test Commands

```bash
# Build the mod (JAR outputs to build/libs/)
./gradlew build

# Clean and rebuild
./gradlew clean build

# Run client for testing (launches Minecraft with the mod)
./gradlew runClient

# Generate data (recipes, models, loot tables)
./gradlew runDatagen

# Run in development mode
./gradlew runDatagenClient
```

**⚠️ Critical Build Issue on Some Windows Systems**:

On some Windows configurations, Fabric Loom fails to download the Minecraft version manifest due to SSL/TLS connection issues, despite Java being able to access the URLs directly. This manifests as:

```
Failed to setup Minecraft, net.fabricmc.loom.util.download.DownloadException: Failed download after 3 attempts
Forcing downloading C:\Users\...\fabric-loom\version_manifest.json as existing lock file was found
```

**Workarounds**:
1. **Copy cache from working PC**: Copy the entire `~/.gradle/caches` directory from a PC where the build works
2. **Use the fix script**: Run `.\fix-build.ps1` before each build to manually download the manifest and clean locks
3. **Manual download**: 
   ```powershell
   $cacheDir = "$env:USERPROFILE\.gradle\caches\fabric-loom"
   New-Item -ItemType Directory -Force -Path $cacheDir
   Invoke-WebRequest -Uri "https://piston-meta.mojang.com/mc/game/version_manifest_v2.json" `
       -OutFile "$cacheDir\version_manifest.json" -UseBasicParsing
   ```

**Additional Known Issues**:
- **bbkr.space SSL errors**: The CottonMC repository (`server.bbkr.space`) fails with `unrecognized_name` SSL errors on some systems. LibGui should be resolved from Maven Central as fallback.
- **Loom cache corruption**: Delete `.gradle/loom-cache/` if build fails after interruption

**Output artifacts**:
- `build/libs/StevesCarts-3.0.0.jar` - Production JAR (obfuscated)
- `build/libs/StevesCarts-3.0.0-sources.jar` - Source JAR
- `src/main/generated/` - Auto-generated data files (recipes, models, etc.)

## Architecture

### Module System (Core Design)

The mod's architecture is built on a **plugin-based module system**:

1. **`CartModule`** (abstract base class): All 124+ module implementations inherit from this
   - Located in: `vswe.stevescarts.module`
   - Each module has a corresponding `ModuleType<T>` for registry-based instantiation
   
2. **`ModuleType<T>`**: Type-safe factory pattern using Fabric's registry system
   - Registry: `ModuleType.REGISTRY` (custom Fabric registry)
   - Metadata: ID, cost, compatibility rules, placement sides, module group, renderer config
   - Factory: `BiFunction<CartEntity, ModuleType<T>, T>` for lazy instantiation

3. **Module Categories** (organized by functionality):
   - `HULL` - Cart bodies (Standard, Wooden, Reinforced, Creative, etc.)
   - `ENGINE` - Power sources (Coal, Solar, Thermal, Creative)
   - `TOOL` - Utility modules (Railer, Torch Placer, Bridge Builder)
   - `STORAGE` - Inventory/tanks (Chests, Liquid Storage, Open Tanks)
   - `ADDON` - Miscellaneous (Brake, Seat, Invisibility Core)
   - `DETECTOR` - Sensors (Entity detectors, Block sensors)
   - `FARMING` - Agricultural automation
   - `WEAPON` - Combat modules
   - `PROCESSOR` - Item/fluid processing (Smelter, Freezer, etc.)

### Registration Pattern

All content is registered through static initializers in central registry classes:

```java
// Main entrypoint: StevesCarts.onInitialize()
StevesCartsItems.init()         // Items
StevesCartsBlocks.init()        // Blocks + Block Items
StevesCartsBlockEntities.init() // Block Entities
StevesCartsScreenHandlers.init()// GUI handlers
StevesCartsEntities.init()      // Cart entities
StevesCartsModules.init()       // Module types
```

**Pattern**: All registrations use `Registry.register()` with identifiers from `StevesCarts.id(name)`.

### Data Generation

The mod uses Fabric's data generation API (`fabric-datagen` entrypoint):

- **Entry**: `vswe.stevescarts.data.StevesCartsDatagen`
- **Providers**:
  - `StevesCartsModelProvider` - Block/item models and textures
  - `StevesCartsRecipeProvider` - Crafting recipes
  - `StevesCartsLootTableProvider` - Loot tables
  
Generated files go to `src/main/generated/` and are included in resources at build time.

### Mixins

**Minimal mixin usage** - Only 2 mixins for unavoidable Minecraft internals:

- `TagManagerLoaderAccessor` - Access to private `DIRECTORIES` field
- `DispenserBlockAccessor` - Access to private `getBehaviorForItem()` method

**Pattern**: Use `@Accessor` and `@Invoker` for reflection-free access (declared as interfaces).

## Key Conventions

### Naming Conventions

- **Packages**: `vswe.stevescarts.<category>` (e.g., `module.engine`, `module.storage`)
- **Classes**: `<Function>Module` suffix for all modules (e.g., `CoalEngineModule`, `BasicSolarEngineModule`)
- **Tiers**: Use tier prefixes consistently:
  - `Basic` → `Iron` → `Hardened` → `Diamond` → `Galgadorian` (highest tier)
  - `Advanced`, `Extreme`, `Creative` (special variants)
- **Identifiers**: Always use `StevesCarts.id("name")` to create `Identifier` objects

### Text Localization

**Always use `TextHelper`** (`vswe.stevescarts.util.TextHelper`) for text:

```java
// Translatable text
TextHelper.translatable("item.stevescarts.coal_engine")
TextHelper.translatable("tooltip.stevescarts.cost", cost)

// Literal text
TextHelper.literal("Debug: " + value)
```

**Why**: `TextHelper` abstracts Minecraft's text API changes across versions.

### Fluid Handling

Use the `Tank` class (`vswe.stevescarts.util.Tank`) and `FluidUtils` for all fluid operations:
- `Tank` wraps Fabric's `SingleFluidStorage` with convenient methods
- Never use Fabric fluid APIs directly in module code

### Animation

For UI/rendering animations, use the animator utilities:
- `DualAnimator` - 2-state animations
- `TrialAnimator` - 3-state animations
- `QuadralAnimator` - 4-state animations

## Porting Modules from v1.12.2

**Reference codebase**: The complete v1.12.2 Forge implementation is in `src_old/` (124 modules).

### Porting Checklist

When porting a module from `src_old/`:

1. **Find**: Locate the module in `src_old/src/main/java/vswe/stevescarts/modules/`
2. **Copy**: Copy the class to the appropriate category in `src/main/java/vswe/stevescarts/module/`
3. **Adapt imports**:
   - `net.minecraftforge.*` → `net.fabricmc.fabric.api.*`
   - `net.minecraft.nbt.NBTTagCompound` → `net.minecraft.nbt.NbtCompound`
   - `net.minecraft.entity.player.EntityPlayer` → `net.minecraft.entity.player.PlayerEntity`
4. **Update methods**:
   - `update()` → `tick()`
   - `readFromNBT()` → `readNbt()` / `writeToNbt()`
   - `IBlockState` → `BlockState`
5. **Register**: Add to `StevesCartsModules.init()` with `ModuleType` registration
6. **Data generation**: Add crafting recipe in `StevesCartsRecipeProvider`
7. **Localization**: Add translation keys to `src/main/resources/assets/stevescarts/lang/en_us.json`
8. **Test**: Run `./gradlew runClient` and verify in-game

### Common Forge → Fabric Conversions

| Forge 1.12.2 | Fabric 1.19 |
|--------------|-------------|
| `GameRegistry.register()` | `Registry.register(Registries.X, id, obj)` |
| `@SubscribeEvent` | Fabric event callbacks (e.g., `ServerTickEvents.END_SERVER_TICK.register()`) |
| `Container` | `ScreenHandler` |
| `GuiContainer` | `HandledScreen<T>` |
| `NBTTagCompound` | `NbtCompound` |
| Capabilities (`IFluidHandler`, etc.) | Custom interfaces or Fabric API lookups |

### Module Complexity Estimates

- **Simple** (2-4h): Engines, Hulls, basic Storage
- **Medium** (5-10h): Tools, Detectors, basic Workers
- **Complex** (8-15h): Drills, Farmers, Weapons, Processors

## File Organization

**Don't modify** (archive):
- `src_old/` - Original v1.12.2 code for reference only

**Active development**:
- `src/main/java/` - All Java source code
- `src/main/resources/` - Assets (textures, lang files, configs)
- `src/main/generated/` - Auto-generated by datagen (don't edit manually)

**Documentation** (French):
- `GUIDE_PORTAGE.md` - Detailed porting guide (Forge → Fabric)
- `STRUCTURE_DU_MOD.md` - Complete project structure
- `QUICKSTART.md` - Quick start guide
- `ROADMAP.md` - Development phases and priorities
- `docs/module_checklist.md` - Module porting checklist

**Tracking files**:
- `MODULES_IMPLEMENTATION.json` - JSON database of module status
- `ETAT_PROGRESSION.md` - Monthly progress reports

## Dependencies

Declared in `build.gradle`:

- **Minecraft**: 1.19.2
- **Fabric Loader**: 0.14.8
- **Fabric API**: 0.76.0+1.19.2
- **LibGui**: 5.4.0+1.19.2 (UI framework)
- **LibBlockAttributes**: 0.10.0 (fluid/energy API)

## Testing

**In-game testing workflow**:

1. Build: `./gradlew build`
2. Copy: `build/libs/StevesCarts-3.0.0.jar` → `~/.minecraft/mods/`
3. Launch Minecraft (Fabric loader with Fabric API installed)
4. Creative mode → Search for "stevescarts" items
5. Test crafting and module functionality

**Module testing checklist**:
- ✓ Item appears in creative inventory
- ✓ Crafting recipe works
- ✓ Module can be placed in Cart Assembler
- ✓ Module functions correctly on cart
- ✓ NBT data persists (save/load)
- ✓ No console errors

## Common Issues

### Build Failures

- **TLS protocol error**: Known issue with Gradle config. On systems where Fabric Loom cannot download Minecraft manifests:
  - **Solution**: Copy `~/.gradle/caches` from a working PC (one-time)
  - **Important**: Never run `./gradlew clean` - it will wipe the cache
  - Use `Remove-Item build -Recurse -Force` instead to clean build outputs only
- **Loom cache corruption**: Run `.\fix-build.ps1` to repair locks (don't delete cache)

### Module Porting Issues

- **Missing imports**: Forge dependencies removed - use Fabric API equivalents
- **Capabilities not available**: Implement custom interfaces or use Fabric's `ApiLookup`
- **Event system changed**: Replace `@SubscribeEvent` with Fabric event callbacks

## Style Guidelines

- **Comments**: Only comment code that needs clarification. Avoid obvious comments.
- **TextHelper**: Always use for localized or literal text
- **Registry pattern**: Follow existing patterns in `StevesCarts*.init()` methods
- **Module hierarchy**: Extend appropriate base class (`CartModule`, `EngineModule`, `StorageModule`, etc.)
- **Factory pattern**: All modules created via `ModuleType` factories, never direct constructors

## Progress Tracking

When completing a module port:

1. Update `docs/module_checklist.md` with `[x]` for completed module
2. Update `MODULES_IMPLEMENTATION.json` with implementation details
3. Consider updating `ETAT_PROGRESSION.md` for monthly milestones

**Current priorities** (see `ROADMAP.md` Phase 2):
- Simple modules: Engines, Hulls, Storage (2-4h each)
- Medium modules: Detectors, Tools (5-10h each)
- Complex modules: Drills, Farmers, Weapons (8-15h each)
