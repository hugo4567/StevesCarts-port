# Steve's Carts - Mod Compatibility System

## Overview

This package provides optional compatibility with other mods. All compat modules
are **optional** - the mod works without any of the supported mods installed.

## Architecture

### API (`vswe.stevescarts.api.farms`)

- `EnumHarvestResult` - Result enum for harvest checks (ALLOW, SKIP, DISALLOW)
- `ITreeModule` - Interface for tree detection and sapling planting
- `ITreeProduceModule` - Extended tree interface for sap/produce harvesting
- `ICropModule` - Interface for crop detection and planting

### Compat Core (`vswe.stevescarts.compat`)

- `ICompatPlugin` - Interface for compatibility plugins
- `CompatHelpers` - Registry for tree/crop modules
- `CompatManager` - Detects installed mods and loads appropriate handlers

## Supported Mods

### Minecraft (Always Active)

- Vanilla trees (oak, birch, spruce, jungle, acacia, dark oak, mangrove)
- Vanilla crops (wheat, carrots, potatoes, beetroot, melon, pumpkin)

### Tech Reborn (Fabric 1.18.2+)

**Mod ID**: `techreborn`

Features:
- Rubber tree harvesting
- Sap extraction with Tree Tap module
- Rubber sapling planting

### IndustrialCraft 2 (STUB - Not available for Fabric)

**Mod ID**: `ic2`

Status: **NOT AVAILABLE** for Fabric 1.18.2

The IC2 compat is a stub placeholder. For rubber tree functionality,
use Tech Reborn instead.

### Forestry (STUB - Not available for Fabric)

**Mod ID**: `forestry`

Status: **NOT AVAILABLE** for Fabric 1.18.2

The Forestry compat is a stub placeholder. Features like genetic
trees are not available in Fabric.

## Usage

### Initialization

The compat system is automatically initialized on first use:

```java
// In your mod initialization or when needed:
CompatManager.getInstance().init();
```

Or just access modules - initialization happens automatically:

```java
List<ITreeModule> treeModules = CompatManager.getInstance().getTreeModules();
List<ICropModule> cropModules = CompatManager.getInstance().getCropModules();
```

### Checking Mod Availability

```java
if (CompatManager.isModLoaded("techreborn")) {
    // Tech Reborn is installed
}
```

### Using Tree Modules

```java
for (ITreeModule module : CompatManager.getInstance().getTreeModules()) {
    EnumHarvestResult result = module.isWood(blockState, pos, cartEntity);
    if (result == EnumHarvestResult.ALLOW) {
        // Harvest the wood
        break;
    } else if (result == EnumHarvestResult.DISALLOW) {
        // Don't harvest (e.g., rubber tree with sap)
        break;
    }
    // SKIP means try next module
}
```

### Using Crop Modules

```java
for (ICropModule module : CompatManager.getInstance().getCropModules()) {
    if (module.isSeedValid(seedStack)) {
        BlockState cropState = module.getCropFromSeed(seedStack, world, pos);
        if (cropState != null) {
            // Plant the crop
            break;
        }
    }
}
```

## Adding New Compat

1. Create a package under `vswe.stevescarts.compat.yourmod`
2. Implement `ICompatPlugin`:

```java
package vswe.stevescarts.compat.yourmod;

import vswe.stevescarts.compat.CompatHelpers;
import vswe.stevescarts.compat.ICompatPlugin;

public class CompatYourMod implements ICompatPlugin {
    @Override
    public void loadAddons(CompatHelpers helpers) {
        helpers.registerTree(new YourModTreeModule());
        helpers.registerCrop(new YourModCropModule());
    }
}
```

3. Add loading logic in `CompatManager.init()`:

```java
private void loadYourModCompat() {
    if (isModLoaded("yourmodid")) {
        LOGGER.info("YourMod detected, loading compatibility");
        try {
            CompatYourMod compat = new CompatYourMod();
            compat.loadAddons(helpers);
        } catch (Exception e) {
            LOGGER.error("Failed to load YourMod compatibility", e);
        }
    }
}
```

## Priority

Compat modules are loaded in this order (first match wins):

1. Tech Reborn (mod-specific, high priority)
2. IC2 (stub)
3. Forestry (stub)
4. Minecraft (vanilla fallback, lowest priority)

When checking blocks, the first module that returns `ALLOW` or `DISALLOW`
takes precedence. `SKIP` passes to the next module.

## Notes

- All compat is loaded with try-catch to prevent crashes if mod APIs change
- Use `FabricLoader.getInstance().isModLoaded()` for runtime checks
- Never hard-depend on other mods - use reflection or optional interfaces
- The vanilla Minecraft compat is always loaded last as a fallback
