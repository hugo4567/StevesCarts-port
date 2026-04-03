package vswe.stevescarts.module.tool;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.ModuleType;
import vswe.stevescarts.module.Worker;

import net.minecraft.nbt.NbtCompound;

/**
 * Cutter Module - Harvests wood and foliage
 * Base woodcutting implementation
 */
public class CutterModule extends CartModule implements Worker {
	protected int cuttingCooldown;

	public CutterModule(CartEntity minecart, ModuleType<?> type) {
		super(minecart, type);
		this.cuttingCooldown = 0;
	}

	@Override
	public void writeToNbt(NbtCompound nbt) {
		nbt.putInt("CuttingCooldown", this.cuttingCooldown);
		super.writeToNbt(nbt);
	}

	@Override
	public void readFromNbt(NbtCompound nbt) {
		this.cuttingCooldown = nbt.getInt("CuttingCooldown");
		super.readFromNbt(nbt);
	}

	@Override
	public void work() {
		// Woodcutting logic would go here
		// For now, just a placeholder
	}
}
