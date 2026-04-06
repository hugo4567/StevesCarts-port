package vswe.stevescarts.compat.ic2;

import vswe.stevescarts.compat.CompatHelpers;
import vswe.stevescarts.compat.ICompatPlugin;

/**
 * IndustrialCraft 2 compatibility (STUB).
 * 
 * NOTE: IC2 is NOT available for Fabric 1.18.2.
 * This is a placeholder for potential future compatibility if:
 * - IC2 gets ported to Fabric
 * - A compatible mod (like IC2 Classic) becomes available
 * - Tech Reborn is used as an alternative (see CompatTechReborn)
 * 
 * Original IC2 features supported:
 * - Rubber tree harvesting with tree tap
 * - EU energy system
 */
public class CompatIC2 implements ICompatPlugin {

    @Override
    public void loadAddons(CompatHelpers helpers) {
        // TODO: IC2 is not available for Fabric 1.18.2
        // If IC2 becomes available, register: helpers.registerTree(new IC2RubberTreeModule());
        
        // For now, Tech Reborn provides similar functionality
        // See CompatTechReborn for rubber tree support
    }
}
