package vswe.stevescarts.api.farms;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vswe.stevescarts.entity.CartEntity;

/**
 * Interface for tree handling modules.
 * Implementations provide support for different tree types (vanilla, mod trees).
 */
public interface ITreeModule {

    /**
     * Check if the block is a valid leaf block.
     *
     * @param blockState The block state to check
     * @param pos The position in the world
     * @param cart The cart entity
     * @return ALLOW if this is a leaf, SKIP to let other modules check, DISALLOW to prevent harvesting
     */
    EnumHarvestResult isLeaves(BlockState blockState, BlockPos pos, CartEntity cart);

    /**
     * Check if the block is a valid wood/log block.
     *
     * @param blockState The block state to check
     * @param pos The position in the world
     * @param cart The cart entity
     * @return ALLOW if this is wood, SKIP to let other modules check, DISALLOW to prevent harvesting
     */
    EnumHarvestResult isWood(BlockState blockState, BlockPos pos, CartEntity cart);

    /**
     * Check if the item is a valid sapling that this module can plant.
     *
     * @param itemStack The item stack to check
     * @return true if this is a valid sapling for this module
     */
    boolean isSapling(ItemStack itemStack);

    /**
     * Plant a sapling in the world.
     *
     * @param world The world
     * @param pos The position to plant (on ground level)
     * @param stack The sapling stack (should be decremented on success)
     * @return true if planting was successful
     */
    boolean plantSapling(World world, BlockPos pos, ItemStack stack);
}
