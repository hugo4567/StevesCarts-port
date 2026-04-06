package vswe.stevescarts.api.farms;

/**
 * Result of checking if a block can be harvested by a farming module.
 */
public enum EnumHarvestResult {
    /**
     * The block can be harvested.
     */
    ALLOW,
    /**
     * Skip this block, let other modules check.
     */
    SKIP,
    /**
     * Block should NOT be harvested (e.g., rubber tree with sap).
     */
    DISALLOW
}
