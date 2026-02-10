package vswe.stevescarts.util;

import vswe.stevescarts.util.TextHelper;

import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Items;

/**
 * Une classe FluidUtils simplifiÃ©e compatible avec Minecraft 1.18.2
 * Cette classe est basÃ©e sur le fonctionnement de FluidUtils de RebornCore mais adaptÃ©e pour notre usage
 */
public class FluidUtils {
    public static boolean drainContainers(Tank tank, SimpleInventory inventory, int inputSlot, int outputSlot) {
        ItemStack input = inventory.getStack(inputSlot);
        ItemStack output = inventory.getStack(outputSlot);
        
        // Cas simple pour le seau d'eau
        if (input.getItem() == Items.WATER_BUCKET) {
            if (output.isEmpty() || (output.getItem() == Items.BUCKET && output.getCount() < output.getMaxCount())) {
                if (tank.fill(Fluids.WATER, 1000, false) == 1000) {
                    tank.fill(Fluids.WATER, 1000, true);
                    input.decrement(1);
                    
                    if (output.isEmpty()) {
                        inventory.setStack(outputSlot, new ItemStack(Items.BUCKET));
                    } else {
                        output.increment(1);
                    }
                    return true;
                }
            }
        }
        // Cas simple pour le seau de lave
        else if (input.getItem() == Items.LAVA_BUCKET) {
            if (output.isEmpty() || (output.getItem() == Items.BUCKET && output.getCount() < output.getMaxCount())) {
                if (tank.fill(Fluids.LAVA, 1000, false) == 1000) {
                    tank.fill(Fluids.LAVA, 1000, true);
                    input.decrement(1);
                    
                    if (output.isEmpty()) {
                        inventory.setStack(outputSlot, new ItemStack(Items.BUCKET));
                    } else {
                        output.increment(1);
                    }
                    return true;
                }
            }
        }
        
        return false;
    }
    
    public static boolean fillContainers(Tank tank, SimpleInventory inventory, int inputSlot, int outputSlot) {
        ItemStack input = inventory.getStack(inputSlot);
        ItemStack output = inventory.getStack(outputSlot);
        
        if (input.getItem() == Items.BUCKET && !tank.isEmpty() && tank.getAmount() >= 1000) {
            ItemStack filledBucket = ItemStack.EMPTY;
            
            if (tank.getFluid() == Fluids.WATER) {
                filledBucket = new ItemStack(Items.WATER_BUCKET);
            } else if (tank.getFluid() == Fluids.LAVA) {
                filledBucket = new ItemStack(Items.LAVA_BUCKET);
            }
            
            if (!filledBucket.isEmpty() && (output.isEmpty() || (output.isItemEqual(filledBucket) && output.getCount() < output.getMaxCount()))) {
                tank.drain(1000, true);
                input.decrement(1);
                
                if (output.isEmpty()) {
                    inventory.setStack(outputSlot, filledBucket);
                } else {
                    output.increment(1);
                }
                return true;
            }
        }
        
        return false;
    }
    
    public static Text getFluidName(Fluid fluid) {
        return TextHelper.translatable(fluid.getDefaultState().getBlockState().getBlock().getTranslationKey());
    }
}
