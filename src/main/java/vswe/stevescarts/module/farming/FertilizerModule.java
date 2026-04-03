package vswe.stevescarts.module.farming;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.ModuleType;
import vswe.stevescarts.module.Worker;

import net.minecraft.nbt.NbtCompound;

/**
 * Fertilizer Module - Applies fertilizer/bonemeal to crops
 * Increases crop growth speed in surrounding area
 */
public class FertilizerModule extends CartModule implements Worker {
	protected int applyTimer = 0;
	protected static final int APPLY_INTERVAL = 40; // Apply every 2 seconds (40 ticks)

	public FertilizerModule(CartEntity minecart, ModuleType<?> type) {
		super(minecart, type);
	}

	@Override
	public void writeToNbt(NbtCompound nbt) {
		nbt.putInt("ApplyTimer", this.applyTimer);
		super.writeToNbt(nbt);
	}

	@Override
	public void readFromNbt(NbtCompound nbt) {
		this.applyTimer = nbt.getInt("ApplyTimer");
		super.readFromNbt(nbt);
	}

	@Override
	public void work() {
		// Increment timer and apply fertilizer at interval
		if (++this.applyTimer >= APPLY_INTERVAL) {
			this.applyTimer = 0;
			// Fertilizer application would happen here
		}
	}
}