package vswe.stevescarts.compat;

import vswe.stevescarts.api.farms.ICropModule;
import vswe.stevescarts.api.farms.ITreeModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for registering compat modules.
 * Used by compat plugins to register their tree/crop handlers.
 */
public class CompatHelpers {
    private final List<ITreeModule> treeModules = new ArrayList<>();
    private final List<ICropModule> cropModules = new ArrayList<>();

    /**
     * Register a tree module handler.
     * 
     * @param treeModule The tree module to register
     */
    public void registerTree(ITreeModule treeModule) {
        treeModules.add(treeModule);
    }

    /**
     * Register a crop module handler.
     * 
     * @param cropModule The crop module to register
     */
    public void registerCrop(ICropModule cropModule) {
        cropModules.add(cropModule);
    }

    /**
     * Get all registered tree modules.
     * 
     * @return List of tree modules
     */
    public List<ITreeModule> getTreeModules() {
        return treeModules;
    }

    /**
     * Get all registered crop modules.
     * 
     * @return List of crop modules
     */
    public List<ICropModule> getCropModules() {
        return cropModules;
    }
}
