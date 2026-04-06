package vswe.stevescarts.compat.techreborn;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import vswe.stevescarts.api.farms.EnumHarvestResult;
import vswe.stevescarts.api.farms.ITreeProduceModule;
import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.addon.TreeTapModule;

/**
 * Tech Reborn rubber tree compatibility module.
 * Handles rubber tree logs, leaves, saplings, and sap extraction.
 * 
 * Tech Reborn block IDs:
 * - techreborn:rubber_log
 * - techreborn:rubber_leaves
 * - techreborn:rubber_sapling
 * - techreborn:sap (item)
 * 
 * Rubber logs have a HAS_SAP property that indicates if sap can be extracted.
 */
public class TechRebornRubberTreeModule implements ITreeProduceModule {

    private static final Identifier RUBBER_LOG_ID = new Identifier("techreborn", "rubber_log");
    private static final Identifier RUBBER_LEAVES_ID = new Identifier("techreborn", "rubber_leaves");
    private static final Identifier RUBBER_SAPLING_ID = new Identifier("techreborn", "rubber_sapling");
    private static final Identifier SAP_ID = new Identifier("techreborn", "sap");
    
    // Property name for sap on rubber logs
    private static final String HAS_SAP_PROPERTY = "has_sap";

    @Override
    public EnumHarvestResult isLeaves(BlockState blockState, BlockPos pos, CartEntity cart) {
        Identifier blockId = Registry.BLOCK.getId(blockState.getBlock());
        
        if (RUBBER_LEAVES_ID.equals(blockId)) {
            // If cart has tree tap, don't harvest leaves (preserve tree for tapping)
            if (cart.hasModule(TreeTapModule.class)) {
                return EnumHarvestResult.DISALLOW;
            }
            return EnumHarvestResult.ALLOW;
        }
        return EnumHarvestResult.SKIP;
    }

    @Override
    public EnumHarvestResult isWood(BlockState blockState, BlockPos pos, CartEntity cart) {
        Identifier blockId = Registry.BLOCK.getId(blockState.getBlock());
        
        if (RUBBER_LOG_ID.equals(blockId)) {
            return EnumHarvestResult.ALLOW;
        }
        return EnumHarvestResult.SKIP;
    }

    @Override
    public boolean isSapling(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return false;
        }
        Identifier itemId = Registry.ITEM.getId(itemStack.getItem());
        return RUBBER_SAPLING_ID.equals(itemId);
    }

    @Override
    public boolean plantSapling(World world, BlockPos pos, ItemStack stack) {
        if (!isSapling(stack)) {
            return false;
        }
        
        Block saplingBlock = Registry.BLOCK.get(RUBBER_SAPLING_ID);
        if (saplingBlock == null) {
            return false;
        }
        
        BlockPos plantPos = pos.up();
        if (!world.isAir(plantPos)) {
            return false;
        }
        
        // Check if sapling can be placed
        BlockState saplingState = saplingBlock.getDefaultState();
        if (!saplingBlock.canPlaceAt(saplingState, world, plantPos)) {
            return false;
        }
        
        world.setBlockState(plantPos, saplingState);
        stack.decrement(1);
        return true;
    }

    @Override
    public boolean harvest(BlockState blockState, BlockPos pos, CartEntity cart,
                          DefaultedList<ItemStack> drops, boolean simulate) {
        // Only harvest sap if cart has tree tap module
        if (!cart.hasModule(TreeTapModule.class)) {
            return false;
        }
        
        World world = cart.world;
        if (world == null) {
            return false;
        }
        
        // Check if this is a rubber log with sap
        Identifier blockId = Registry.BLOCK.getId(blockState.getBlock());
        if (!RUBBER_LOG_ID.equals(blockId)) {
            return false;
        }
        
        // Check for HAS_SAP property using reflection-free approach
        // In Tech Reborn, rubber logs have a boolean property "has_sap"
        boolean hasSap = false;
        try {
            // Try to get the property value from the block state
            // This is a safe way to check without direct dependency
            String stateString = blockState.toString();
            hasSap = stateString.contains("has_sap=true");
        } catch (Exception e) {
            return false;
        }
        
        if (!hasSap) {
            return false;
        }
        
        // Get sap item
        Item sapItem = Registry.ITEM.get(SAP_ID);
        if (sapItem == null) {
            return false;
        }
        
        // Add sap to drops
        drops.add(new ItemStack(sapItem));
        
        if (!simulate) {
            // Remove sap from the log by setting has_sap to false
            // We need to use string manipulation since we don't have direct TR dependency
            try {
                // Get the block and create new state with has_sap=false
                Block rubberLog = blockState.getBlock();
                
                // Use BlockState cycle to get state without sap
                // This is a bit hacky but works without direct dependency
                BlockState newState = blockState;
                for (var property : blockState.getProperties()) {
                    if (property.getName().equals(HAS_SAP_PROPERTY)) {
                        // Cycle the property to false
                        newState = blockState.cycle(property);
                        // Keep cycling until has_sap is false
                        while (newState.toString().contains("has_sap=true")) {
                            newState = newState.cycle(property);
                        }
                        break;
                    }
                }
                
                world.setBlockState(pos, newState);
                
                // Play extraction sound
                world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, 
                    SoundCategory.BLOCKS, 0.6F, 1.0F);
            } catch (Exception e) {
                // If we can't modify the state, still return the drops
            }
        }
        
        return true;
    }
}
