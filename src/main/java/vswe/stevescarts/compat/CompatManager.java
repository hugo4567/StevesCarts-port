package vswe.stevescarts.compat;

import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vswe.stevescarts.api.farms.ICropModule;
import vswe.stevescarts.api.farms.ITreeModule;
import vswe.stevescarts.compat.minecraft.CompatMinecraft;
import vswe.stevescarts.compat.techreborn.CompatTechReborn;
import vswe.stevescarts.compat.forestry.CompatForestry;
import vswe.stevescarts.compat.ic2.CompatIC2;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages compatibility modules for different mods.
 * Automatically detects installed mods and loads appropriate handlers.
 */
public class CompatManager {
    private static final Logger LOGGER = LoggerFactory.getLogger("StevesCarts/Compat");
    
    private static final CompatManager INSTANCE = new CompatManager();
    
    private final CompatHelpers helpers = new CompatHelpers();
    private boolean initialized = false;

    private CompatManager() {}

    public static CompatManager getInstance() {
        return INSTANCE;
    }

    /**
     * Initialize compatibility modules.
     * Should be called during mod initialization.
     */
    public void init() {
        if (initialized) {
            return;
        }
        initialized = true;
        
        LOGGER.info("Initializing Steve's Carts compatibility modules...");
        
        // Load mod-specific compat first (higher priority)
        loadTechRebornCompat();
        loadIC2Compat();
        loadForestryCompat();
        
        // Load vanilla compat last (lowest priority, fallback)
        loadMinecraftCompat();
        
        LOGGER.info("Loaded {} tree modules and {} crop modules", 
            helpers.getTreeModules().size(), 
            helpers.getCropModules().size());
    }

    private void loadMinecraftCompat() {
        LOGGER.info("Loading Minecraft vanilla compatibility");
        CompatMinecraft compat = new CompatMinecraft();
        compat.loadAddons(helpers);
    }

    private void loadTechRebornCompat() {
        if (isModLoaded("techreborn")) {
            LOGGER.info("Tech Reborn detected, loading compatibility");
            try {
                CompatTechReborn compat = new CompatTechReborn();
                compat.loadAddons(helpers);
            } catch (Exception e) {
                LOGGER.error("Failed to load Tech Reborn compatibility", e);
            }
        }
    }

    private void loadIC2Compat() {
        // IC2 doesn't exist for Fabric 1.18, but we keep the structure for future
        if (isModLoaded("ic2")) {
            LOGGER.info("IndustrialCraft 2 detected, loading compatibility");
            try {
                CompatIC2 compat = new CompatIC2();
                compat.loadAddons(helpers);
            } catch (Exception e) {
                LOGGER.error("Failed to load IC2 compatibility", e);
            }
        }
    }

    private void loadForestryCompat() {
        // Forestry doesn't exist for Fabric 1.18, but we keep the structure
        if (isModLoaded("forestry")) {
            LOGGER.info("Forestry detected, loading compatibility");
            try {
                CompatForestry compat = new CompatForestry();
                compat.loadAddons(helpers);
            } catch (Exception e) {
                LOGGER.error("Failed to load Forestry compatibility", e);
            }
        }
    }

    /**
     * Check if a mod is loaded.
     * 
     * @param modId The mod ID to check
     * @return true if the mod is loaded
     */
    public static boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    /**
     * Get all registered tree modules.
     * 
     * @return List of tree modules
     */
    public List<ITreeModule> getTreeModules() {
        if (!initialized) {
            init();
        }
        return helpers.getTreeModules();
    }

    /**
     * Get all registered crop modules.
     * 
     * @return List of crop modules
     */
    public List<ICropModule> getCropModules() {
        if (!initialized) {
            init();
        }
        return helpers.getCropModules();
    }
}
