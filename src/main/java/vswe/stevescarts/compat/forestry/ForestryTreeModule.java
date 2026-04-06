package vswe.stevescarts.compat.forestry;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vswe.stevescarts.api.farms.EnumHarvestResult;
import vswe.stevescarts.api.farms.ITreeModule;
import vswe.stevescarts.entity.CartEntity;

/**
 * Forestry Tree Module (STUB).
 * 
 * NOTE: This is a non-functional stub. Forestry is not available for Fabric 1.18.2.
 * 
 * Original Forestry features:
 * - Support for all Forestry tree species
 * - Genetic sapling detection via ITreeRoot API
 * - Special planting logic using Forestry's genetics system
 * - Integration with Forestry's arboriculture module
 * 
 * Forestry trees have complex genetics that determine:
 * - Growth speed
 * - Yield
 * - Fruit/product drops
 * - Leaf color and shape
 */
public class ForestryTreeModule implements ITreeModule {

    // Forestry identifiers (for reference)
    // private static final Identifier FORESTRY_SAPLING = new Identifier("forestry", "sapling");
    // private static final Identifier FORESTRY_LEAVES = new Identifier("forestry", "leaves");
    // The log blocks vary by tree species

    // Original code used:
    // ITreeRoot treeRoot = (ITreeRoot) AlleleManager.alleleRegistry.getSpeciesRoot("rootTrees");

    @Override
    public EnumHarvestResult isLeaves(BlockState blockState, BlockPos pos, CartEntity cart) {
        // TODO: Forestry not available for Fabric
        // Original: blockState.getBlock() == leaves (forestry:leaves)
        return EnumHarvestResult.SKIP;
    }

    @Override
    public EnumHarvestResult isWood(BlockState blockState, BlockPos pos, CartEntity cart) {
        // TODO: Forestry not available for Fabric
        // Original: Check if block registry name is forestry:logs.*
        return EnumHarvestResult.SKIP;
    }

    @Override
    public boolean isSapling(ItemStack itemStack) {
        // TODO: Forestry not available for Fabric
        // Original: itemStack.getItem() == forestry sapling item
        return false;
    }

    @Override
    public boolean plantSapling(World world, BlockPos pos, ItemStack stack) {
        // TODO: Forestry not available for Fabric
        // Original implementation:
        // 1. Get ITree from stack via treeRoot.getMember(stack)
        // 2. Check tree.canStay(world, pos)
        // 3. Use treeRoot.plantSapling(world, tree, gameProfile, pos)
        return false;
    }
}
