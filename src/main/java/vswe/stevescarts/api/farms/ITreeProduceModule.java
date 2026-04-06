package vswe.stevescarts.api.farms;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import vswe.stevescarts.entity.CartEntity;

/**
 * Extended tree module for trees that produce items (like rubber trees).
 * Used for Tech Reborn rubber trees, IC2 rubber trees, etc.
 */
public interface ITreeProduceModule extends ITreeModule {

    /**
     * Harvest produce from a tree without cutting it down.
     * Used for rubber tapping and similar mechanics.
     *
     * @param blockState The current block state
     * @param pos The position of the block
     * @param cart The cart entity
     * @param drops List to add harvested items to
     * @param simulate If true, only simulate (don't modify world)
     * @return true if any produce was harvested
     */
    boolean harvest(BlockState blockState, BlockPos pos, CartEntity cart, 
                   DefaultedList<ItemStack> drops, boolean simulate);
}
