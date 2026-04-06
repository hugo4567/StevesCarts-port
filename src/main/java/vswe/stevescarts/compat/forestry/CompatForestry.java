package vswe.stevescarts.compat.forestry;

import vswe.stevescarts.compat.CompatHelpers;
import vswe.stevescarts.compat.ICompatPlugin;

/**
 * Forestry compatibility (STUB).
 * 
 * NOTE: Forestry is NOT available for Fabric 1.18.2.
 * This is a placeholder for potential future compatibility.
 * 
 * Original Forestry features supported:
 * - Forestry tree species (arboriculture)
 * - Bee integration (apiculture)
 * - Special saplings with genetics
 * 
 * Potential alternatives for Fabric:
 * - Industrial Foregoing (if ported)
 * - Other tree farming mods
 */
public class CompatForestry implements ICompatPlugin {

    @Override
    public void loadAddons(CompatHelpers helpers) {
        // TODO: Forestry is not available for Fabric 1.18.2
        // If Forestry becomes available, register: helpers.registerTree(new ForestryTreeModule());
    }
}
