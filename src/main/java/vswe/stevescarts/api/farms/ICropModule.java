package vswe.stevescarts.api.farms;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Interface for crop handling modules.
 * Implementations provide support for different crop types (vanilla, mod crops).
 */
public interface ICropModule {

    /**
     * Check if the item is a valid seed/plantable item.
     *
     * @param itemStack The item stack to check
     * @return true if this is a valid seed for this module
     */
    boolean isSeedValid(ItemStack itemStack);

    /**
     * Get the crop block state that should be planted for the given seed.
     *
     * @param itemStack The seed item
     * @param world The world
     * @param pos The position where it will be planted
     * @return The block state to place, or null if cannot determine
     */
    BlockState getCropFromSeed(ItemStack itemStack, World world, BlockPos pos);

    /**
     * Check if the crop at the given position is ready to harvest.
     *
     * @param world The world
     * @param pos The position of the crop
     * @return true if the crop is mature and ready to harvest
     */
    boolean isReadyToHarvest(World world, BlockPos pos);
}
