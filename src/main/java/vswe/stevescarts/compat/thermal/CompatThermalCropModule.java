package vswe.stevescarts.compat.thermal;

import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import vswe.stevescarts.api.farms.ICropModule;
import vswe.stevescarts.api.farms.EnumHarvestResult;

public class CompatThermalCropModule implements ICropModule {
    @Override
    public boolean isSeedValid(ItemStack stack) {
        // Détection via tag c:seeds ou namespace thermal/cofh
        TagKey<Item> seedsTag = TagKey.of(Registry.ITEM_KEY, new Identifier("c", "seeds"));
        return stack.isIn(seedsTag) ||
               Registry.ITEM.getId(stack.getItem()).getNamespace().equals("thermal") ||
               Registry.ITEM.getId(stack.getItem()).getNamespace().equals("cofh");
    }

    @Override
    public BlockState getCropFromSeed(ItemStack stack, World world, BlockPos pos) {
        Identifier id = Registry.ITEM.getId(stack.getItem());
        if (id.getNamespace().equals("thermal")) {
            String cropName = id.getPath().replace("_seeds", "_crop");
            Identifier cropId = new Identifier("thermal", cropName);
            BlockState cropState = Registry.BLOCK.get(cropId).getDefaultState();
            if (Registry.BLOCK.get(cropId) instanceof CropBlock) return cropState;
        }
        return null;
    }

    @Override
    public boolean isReadyToHarvest(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof CropBlock crop) {
            return crop.isMature(state);
        }
        return false;
    }


}
