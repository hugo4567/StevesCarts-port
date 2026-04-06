package vswe.stevescarts.compat.techreborn;

import vswe.stevescarts.compat.CompatHelpers;
import vswe.stevescarts.compat.ICompatPlugin;

/**
 * Tech Reborn compatibility.
 * Provides rubber tree handling for Tech Reborn mod.
 * 
 * Tech Reborn is available for Fabric and includes:
 * - Rubber trees with sap harvesting
 * - Energy systems (Team Reborn Energy API)
 */
public class CompatTechReborn implements ICompatPlugin {

    @Override
    public void loadAddons(CompatHelpers helpers) {
        helpers.registerTree(new TechRebornRubberTreeModule());
    }
}
