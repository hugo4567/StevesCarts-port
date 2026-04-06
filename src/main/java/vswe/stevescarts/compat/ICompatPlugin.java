package vswe.stevescarts.compat;

/**
 * Base interface for compatibility plugins.
 * Each mod compat implements this to register its handlers.
 */
public interface ICompatPlugin {
    
    /**
     * Called to load addons for this compatibility plugin.
     * Register tree modules, crop modules, etc. via the helpers.
     * 
     * @param helpers The helpers instance to register modules with
     */
    void loadAddons(CompatHelpers helpers);
}
