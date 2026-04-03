package vswe.stevescarts.module.tool;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.ModuleType;
import vswe.stevescarts.module.Worker;

import net.minecraft.nbt.NbtCompound;

/**
 * Farmer Module - Plants and harvests crops
 * Base farming implementation
 */
public class FarmerModule extends CartModule implements Worker {
	protected int farmingCooldown;

	public FarmerModule(CartEntity minecart, ModuleType<?> type) {
		super(minecart, type);
		this.farmingCooldown = 0;
	}

	@Override
	public void writeToNbt(NbtCompound nbt) {
		nbt.putInt("FarmingCooldown", this.farmingCooldown);
		super.writeToNbt(nbt);
	}

	@Override
	public void readFromNbt(NbtCompound nbt) {
		this.farmingCooldown = nbt.getInt("FarmingCooldown");
		super.readFromNbt(nbt);
	}

	@Override
	public void work() {
		// Farming logic (planting/harvesting) would go here
		// For now, just a placeholder
	}
}
