package vswe.stevescarts.module.addon;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.ModuleType;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Melter Module - Melts snow around the cart
 *
 * Every 70 ticks (~3.5 seconds), this addon scans a 15x3x15 area
 * around the cart and removes snow layers. Opposite of the Freezer.
 *
 * The Extreme Melter subclass also converts ice to water.
 */
public class MelterModule extends CartModule {
	private int tick = 0;

	public MelterModule(CartEntity minecart, ModuleType<?> type) {
		super(minecart, type);
	}

	@Override
	public void tick() {
		if (getEntity() == null || getEntity().world == null || getEntity().world.isClient) {
			return;
		}

		if (getEntity().getModules().stream().anyMatch(CartModule::canPropel)) {
			if (tick >= getInterval()) {
				tick = 0;
				meltArea();
			} else {
				++tick;
			}
		}
	}

	protected int getInterval() {
		return 70;
	}

	protected int getBlocksOnSide() {
		return 7;
	}

	protected int getBlocksFromLevel() {
		return 1;
	}

	private void meltArea() {
		World world = getEntity().world;
		BlockPos cartPos = getEntity().getBlockPos();

		for (int x = -getBlocksOnSide(); x <= getBlocksOnSide(); ++x) {
			for (int z = -getBlocksOnSide(); z <= getBlocksOnSide(); ++z) {
				for (int y = -getBlocksFromLevel(); y <= getBlocksFromLevel(); ++y) {
					BlockPos pos = cartPos.add(x, y, z);
					BlockState state = world.getBlockState(pos);
					meltBlock(world, pos, state);
				}
			}
		}
	}

	/**
	 * Attempts to melt a block at the given position.
	 * Base version only melts snow. Override for ice melting.
	 */
	protected void meltBlock(World world, BlockPos pos, BlockState state) {
		Block block = state.getBlock();
		if (block == Blocks.SNOW) {
			world.removeBlock(pos, false);
		}
	}
}
