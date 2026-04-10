package vswe.stevescarts.compat.tconstruct;

import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import vswe.stevescarts.api.farms.ICropModule;
import vswe.stevescarts.api.farms.EnumHarvestResult;

import net.minecraft.util.Identifier;

public class CompatTinkersCropModule implements ICropModule {
    @Override
    public boolean isSeedValid(ItemStack stack) {
        // Tinkers n'a pas de crops natifs, mais on supporte les tags standards
        TagKey<Item> seedsTag = TagKey.of(Registry.ITEM_KEY, new Identifier("c", "seeds"));
        return stack.isIn(seedsTag);
    }

    @Override
    public BlockState getCropFromSeed(ItemStack stack, World world, BlockPos pos) {
        // Utilisation générique : on ne cible que les crops vanilla/standards
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
