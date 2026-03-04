package vswe.stevescarts.module.addon;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.ModuleType;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

/**
 * Fertilizer Module - Applies bone meal effect to nearby crops
 *
 * This addon uses bone meal or bones from its internal buffer to
 * fertilize crops around the cart. Each bone gives 12 fertilizer,
 * each bone meal gives 4. Has a 1/25 chance per block per work cycle.
 *
 * Range is 1 block by default (3x3 area around cart).
 */
public class FertilizerModule extends CartModule {
	private int fertBuffer = 0;
	private int cooldown = 0;
	private final Random random = new Random();

	private static final int MAX_FERT = 768;
	private static final int FERT_PER_BONE = 12;
	private static final int FERT_PER_BONEMEAL = 4;
	private static final int WORK_INTERVAL = 20;
	private static final int FERT_COST = 2;

	public FertilizerModule(CartEntity minecart, ModuleType<?> type) {
		super(minecart, type);
	}

	@Override
	public void tick() {
		if (getEntity() == null || getEntity().world == null || getEntity().world.isClient) {
			return;
		}

		// Try to load fuel from cart storage
		loadSupplies();

		if (cooldown > 0) {
			--cooldown;
			return;
		}

		cooldown = WORK_INTERVAL;

		if (fertBuffer >= FERT_COST && getEntity().getModules().stream().anyMatch(CartModule::canPropel)) {
			work();
		}
	}

	/**
	 * Scans nearby blocks and fertilizes growable crops.
	 */
	private void work() {
		int range = getRange();
		BlockPos cartPos = getEntity().getBlockPos();

		for (int x = -range; x <= range; x++) {
			for (int z = -range; z <= range; z++) {
				if (fertBuffer < FERT_COST) return;

				// 1/25 chance per block
				if (random.nextInt(25) != 0) continue;

				BlockPos pos = cartPos.add(x, 0, z);
				fertilize(pos);
			}
		}
	}

	/**
	 * Attempts to fertilize a block at the given position.
	 */
	private void fertilize(BlockPos pos) {
		if (getEntity().world instanceof ServerWorld serverWorld) {
			BlockState state = serverWorld.getBlockState(pos);
			Block block = state.getBlock();

			if (block instanceof Fertilizable growable) {
				if (growable.isFertilizable(serverWorld, pos, state, false)
					&& growable.canGrow(serverWorld, serverWorld.random, pos, state)) {
					growable.grow(serverWorld, serverWorld.random, pos, state);
					fertBuffer -= FERT_COST;
				}
			}
		}
	}

	/**
	 * Loads bone/bone meal from cart chest modules into the fert buffer.
	 */
	private void loadSupplies() {
		if (fertBuffer >= MAX_FERT) return;

		for (CartModule module : getEntity().getModules()) {
			if (module instanceof vswe.stevescarts.module.storage.ChestModule chest) {
				for (int i = 0; i < chest.size(); i++) {
					ItemStack stack = chest.getStack(i);
					if (stack.isEmpty()) continue;

					if (stack.isOf(Items.BONE_MEAL)) {
						int canAdd = (MAX_FERT - fertBuffer) / FERT_PER_BONEMEAL;
						int toConsume = Math.min(canAdd, stack.getCount());
						if (toConsume > 0) {
							stack.decrement(toConsume);
							fertBuffer += toConsume * FERT_PER_BONEMEAL;
						}
					} else if (stack.isOf(Items.BONE)) {
						int canAdd = (MAX_FERT - fertBuffer) / FERT_PER_BONE;
						int toConsume = Math.min(canAdd, stack.getCount());
						if (toConsume > 0) {
							stack.decrement(toConsume);
							fertBuffer += toConsume * FERT_PER_BONE;
						}
					}

					if (fertBuffer >= MAX_FERT) return;
				}
			}
		}
	}

	protected int getRange() {
		return 1;
	}

	public int getFertBuffer() {
		return fertBuffer;
	}

	public int getMaxFert() {
		return MAX_FERT;
	}

	public boolean hasSupplies() {
		return fertBuffer > 0;
	}

	@Override
	public void writeToNbt(NbtCompound nbt) {
		nbt.putShort("Fert", (short) fertBuffer);
		super.writeToNbt(nbt);
	}

	@Override
	public void readFromNbt(NbtCompound nbt) {
		fertBuffer = nbt.getShort("Fert");
		super.readFromNbt(nbt);
	}
}
