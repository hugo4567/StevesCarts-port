package vswe.stevescarts.module.addon;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.ModuleType;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

/**
 * Freezer Module (Snow Cannon) - Places snow around the cart
 *
 * Every 70 ticks (~3.5 seconds), this addon scans a 15x3x15 area
 * around the cart and places snow layers on valid positions where
 * the biome temperature allows it (≤ 1.0).
 */
public class FreezerModule extends CartModule {
	private int tick = 0;

	public FreezerModule(CartEntity minecart, ModuleType<?> type) {
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
				generateSnow();
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

	private void generateSnow() {
		World world = getEntity().world;
		BlockPos cartPos = getEntity().getBlockPos();

		for (int x = -getBlocksOnSide(); x <= getBlocksOnSide(); ++x) {
			for (int z = -getBlocksOnSide(); z <= getBlocksOnSide(); ++z) {
				for (int y = -getBlocksFromLevel(); y <= getBlocksFromLevel(); ++y) {
					BlockPos pos = cartPos.add(x, y, z);
					BlockState snowState = Blocks.SNOW.getDefaultState();

					if (world.isAir(pos)
						&& getTemperature(world, pos) <= 1.0f
						&& snowState.canPlaceAt(world, pos)) {
						world.setBlockState(pos, snowState);
					}
				}
			}
		}
	}

	private float getTemperature(World world, BlockPos pos) {
		Biome biome = world.getBiome(pos).value();
		return biome.getTemperature();
	}
}
