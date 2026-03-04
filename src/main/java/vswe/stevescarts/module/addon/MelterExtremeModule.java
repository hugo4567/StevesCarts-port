package vswe.stevescarts.module.addon;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.ModuleType;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Extreme Melter Module - Melts snow AND ice around the cart
 *
 * Extended version of the Melter that also converts ice blocks to water.
 */
public class MelterExtremeModule extends MelterModule {

	public MelterExtremeModule(CartEntity minecart, ModuleType<?> type) {
		super(minecart, type);
	}

	@Override
	protected void meltBlock(World world, BlockPos pos, BlockState state) {
		Block block = state.getBlock();
		if (block == Blocks.SNOW) {
			world.removeBlock(pos, false);
		} else if (block == Blocks.ICE) {
			world.setBlockState(pos, Blocks.WATER.getDefaultState());
		}
	}
}
