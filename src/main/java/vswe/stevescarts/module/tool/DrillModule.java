package vswe.stevescarts.module.tool;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.ModuleType;
import vswe.stevescarts.module.Worker;

import net.minecraft.nbt.NbtCompound;

/**
 * Drill Module - Extracts blocks in a 3x3 area
 * Base drilling implementation for ore mining
 */
public class DrillModule extends CartModule implements Worker {
	protected int miningCooldown;

	public DrillModule(CartEntity minecart, ModuleType<?> type) {
		super(minecart, type);
		this.miningCooldown = 0;
	}

	@Override
	public void writeToNbt(NbtCompound nbt) {
		nbt.putInt("MiningCooldown", this.miningCooldown);
		super.writeToNbt(nbt);
	}

	@Override
	public void readFromNbt(NbtCompound nbt) {
		this.miningCooldown = nbt.getInt("MiningCooldown");
		super.readFromNbt(nbt);
	}

	@Override
	public void work() {
		// Mining logic would go here
		// For now, just a placeholder
	}
}
