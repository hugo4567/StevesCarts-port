package vswe.stevescarts.module.processor;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.ModuleType;
import vswe.stevescarts.module.Worker;

import net.minecraft.nbt.NbtCompound;

/**
 * Incinerator Module - Burns items to destroy them
 * Converts unwanted items into nothing (useful for waste management)
 */
public class IncineratorModule extends CartModule implements Worker {
	protected int burnTimer = 0;
	protected static final int BURN_INTERVAL = 20; // Burn items every 1 second (20 ticks)

	public IncineratorModule(CartEntity minecart, ModuleType<?> type) {
		super(minecart, type);
	}

	@Override
	public void writeToNbt(NbtCompound nbt) {
		nbt.putInt("BurnTimer", this.burnTimer);
		super.writeToNbt(nbt);
	}

	@Override
	public void readFromNbt(NbtCompound nbt) {
		this.burnTimer = nbt.getInt("BurnTimer");
		super.readFromNbt(nbt);
	}

	@Override
	public void work() {
		// Increment timer and burn items at interval
		if (++this.burnTimer >= BURN_INTERVAL) {
			this.burnTimer = 0;
			// Item burning logic would happen here
		}
	}
}