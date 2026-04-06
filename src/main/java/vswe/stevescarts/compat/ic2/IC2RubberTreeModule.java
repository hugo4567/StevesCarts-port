package vswe.stevescarts.compat.ic2;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vswe.stevescarts.api.farms.EnumHarvestResult;
import vswe.stevescarts.api.farms.ITreeProduceModule;
import vswe.stevescarts.entity.CartEntity;

/**
 * IC2 Rubber Tree Module (STUB).
 * 
 * NOTE: This is a non-functional stub. IC2 is not available for Fabric 1.18.2.
 * 
 * Original IC2 features:
 * - Rubber wood detection
 * - Rubber leaves detection
 * - Rubber sapling planting
 * - Resin extraction from wet rubber wood spots
 * 
 * For rubber tree functionality, use Tech Reborn instead (see TechRebornRubberTreeModule).
 */
public class IC2RubberTreeModule implements ITreeProduceModule {

    // IC2 block identifiers (for reference)
    // private static final Identifier IC2_RUBBER_WOOD = new Identifier("ic2", "rubber_wood");
    // private static final Identifier IC2_LEAVES = new Identifier("ic2", "leaves");
    // private static final Identifier IC2_SAPLING = new Identifier("ic2", "sapling");

    @Override
    public EnumHarvestResult isLeaves(BlockState blockState, BlockPos pos, CartEntity cart) {
        // TODO: IC2 not available for Fabric
        return EnumHarvestResult.SKIP;
    }

    @Override
    public EnumHarvestResult isWood(BlockState blockState, BlockPos pos, CartEntity cart) {
        // TODO: IC2 not available for Fabric
        return EnumHarvestResult.SKIP;
    }

    @Override
    public boolean isSapling(ItemStack itemStack) {
        // TODO: IC2 not available for Fabric
        return false;
    }

    @Override
    public boolean plantSapling(World world, BlockPos pos, ItemStack stack) {
        // TODO: IC2 not available for Fabric
        return false;
    }

    @Override
    public boolean harvest(BlockState blockState, BlockPos pos, CartEntity cart,
                          DefaultedList<ItemStack> drops, boolean simulate) {
        // TODO: IC2 not available for Fabric
        // Original implementation would:
        // 1. Check for BlockRubWood with wet rubber spots
        // 2. Extract resin and add to drops
        // 3. Set the spot to dry state
        // 4. Play tree tap sound
        return false;
    }
}
