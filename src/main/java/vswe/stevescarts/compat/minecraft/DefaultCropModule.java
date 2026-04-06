package vswe.stevescarts.compat.minecraft;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.PlantBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vswe.stevescarts.api.farms.ICropModule;

/**
 * Default crop module for vanilla Minecraft crops.
 * Handles wheat, carrots, potatoes, beetroot, and other plantable items.
 */
public class DefaultCropModule implements ICropModule {

    @Override
    public boolean isSeedValid(ItemStack seed) {
        if (seed.isEmpty()) {
            return false;
        }
        
        // Check vanilla seeds
        if (seed.isOf(Items.WHEAT_SEEDS) || 
            seed.isOf(Items.POTATO) || 
            seed.isOf(Items.CARROT) ||
            seed.isOf(Items.BEETROOT_SEEDS) ||
            seed.isOf(Items.MELON_SEEDS) ||
            seed.isOf(Items.PUMPKIN_SEEDS)) {
            return true;
        }
        
        // Check if item is a BlockItem that places a PlantBlock
        if (seed.getItem() instanceof BlockItem blockItem) {
            if (blockItem.getBlock() instanceof CropBlock) {
                return true;
            }
        }
        
        return false;
    }

    @Override
    public BlockState getCropFromSeed(ItemStack seed, World world, BlockPos pos) {
        if (seed.isEmpty()) {
            return null;
        }
        
        // Map vanilla seeds to their crop blocks
        if (seed.isOf(Items.WHEAT_SEEDS)) {
            return Blocks.WHEAT.getDefaultState();
        }
        if (seed.isOf(Items.CARROT)) {
            return Blocks.CARROTS.getDefaultState();
        }
        if (seed.isOf(Items.POTATO)) {
            return Blocks.POTATOES.getDefaultState();
        }
        if (seed.isOf(Items.BEETROOT_SEEDS)) {
            return Blocks.BEETROOTS.getDefaultState();
        }
        if (seed.isOf(Items.MELON_SEEDS)) {
            return Blocks.MELON_STEM.getDefaultState();
        }
        if (seed.isOf(Items.PUMPKIN_SEEDS)) {
            return Blocks.PUMPKIN_STEM.getDefaultState();
        }
        
        // For mod crops that use BlockItem
        if (seed.getItem() instanceof BlockItem blockItem) {
            if (blockItem.getBlock() instanceof CropBlock) {
                return blockItem.getBlock().getDefaultState();
            }
        }
        
        return null;
    }

    @Override
    public boolean isReadyToHarvest(World world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        
        // Check if it's a crop block and fully grown
        if (blockState.getBlock() instanceof CropBlock cropBlock) {
            return cropBlock.isMature(blockState);
        }
        
        // Check Fertilizable interface - if it can't grow anymore, it's ready
        if (blockState.getBlock() instanceof Fertilizable fertilizable) {
            // If it can't grow anymore in world, it's mature
            if (!fertilizable.isFertilizable(world, pos, blockState, false)) {
                return true;
            }
        }
        
        return false;
    }
}
