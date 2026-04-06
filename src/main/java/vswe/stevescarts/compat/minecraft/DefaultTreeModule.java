package vswe.stevescarts.compat.minecraft;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SaplingBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vswe.stevescarts.api.farms.EnumHarvestResult;
import vswe.stevescarts.api.farms.ITreeModule;
import vswe.stevescarts.entity.CartEntity;

/**
 * Default tree module for vanilla Minecraft trees.
 * Uses block tags to identify logs and leaves.
 */
public class DefaultTreeModule implements ITreeModule {

    @Override
    public EnumHarvestResult isLeaves(BlockState blockState, BlockPos pos, CartEntity cart) {
        // Check using vanilla tags
        if (blockState.isIn(BlockTags.LEAVES)) {
            return EnumHarvestResult.ALLOW;
        }
        return EnumHarvestResult.SKIP;
    }

    @Override
    public EnumHarvestResult isWood(BlockState blockState, BlockPos pos, CartEntity cart) {
        // Check using vanilla tags for logs
        if (blockState.isIn(BlockTags.LOGS)) {
            return EnumHarvestResult.ALLOW;
        }
        return EnumHarvestResult.SKIP;
    }

    @Override
    public boolean isSapling(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return false;
        }
        
        // Check if item is a sapling block item
        if (itemStack.getItem() instanceof BlockItem blockItem) {
            if (blockItem.getBlock() instanceof SaplingBlock) {
                return true;
            }
            // Also check via tags
            BlockState state = blockItem.getBlock().getDefaultState();
            if (state.isIn(BlockTags.SAPLINGS)) {
                return true;
            }
        }
        
        return false;
    }

    @Override
    public boolean plantSapling(World world, BlockPos pos, ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        
        if (!(stack.getItem() instanceof BlockItem blockItem)) {
            return false;
        }
        
        if (!(blockItem.getBlock() instanceof SaplingBlock saplingBlock)) {
            return false;
        }
        
        BlockPos plantPos = pos.up();
        
        // Check if we can place the sapling here
        if (!world.isAir(plantPos)) {
            return false;
        }
        
        BlockState groundState = world.getBlockState(pos);
        if (!saplingBlock.canPlaceAt(saplingBlock.getDefaultState(), world, plantPos)) {
            return false;
        }
        
        // Place the sapling
        world.setBlockState(plantPos, saplingBlock.getDefaultState());
        stack.decrement(1);
        return true;
    }
}
