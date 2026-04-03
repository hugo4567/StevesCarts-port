package vswe.stevescarts.module.addon;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.ModuleType;

import net.minecraft.nbt.NbtCompound;

/**
 * Module Toggler Module - Enables/disables other modules dynamically
 * Allows redstone-controlled management of module functionality
 */
public class ModuleTogglerModule extends CartModule {
	protected boolean masterEnabled = true;
	protected byte enabledModulesMask = (byte) 0xFF; // Bitfield for which modules are active
	protected int toggleCooldown = 0;
	protected static final int TOGGLE_DELAY = 10; // 10 tick cooldown between toggles

	public ModuleTogglerModule(CartEntity minecart, ModuleType<?> type) {
		super(minecart, type);
	}

	@Override
	public void writeToNbt(NbtCompound nbt) {
		nbt.putBoolean("MasterEnabled", this.masterEnabled);
		nbt.putByte("EnabledModulesMask", this.enabledModulesMask);
		nbt.putInt("ToggleCooldown", this.toggleCooldown);
		super.writeToNbt(nbt);
	}

	@Override
	public void readFromNbt(NbtCompound nbt) {
		this.masterEnabled = nbt.getBoolean("MasterEnabled");
		this.enabledModulesMask = nbt.getByte("EnabledModulesMask");
		this.toggleCooldown = nbt.getInt("ToggleCooldown");
		super.readFromNbt(nbt);
	}

	public boolean isModuleEnabled(int moduleIndex) {
		return (this.enabledModulesMask & (1 << moduleIndex)) != 0;
	}

	public void toggleModule(int moduleIndex) {
		if (this.toggleCooldown <= 0) {
			this.enabledModulesMask ^= (1 << moduleIndex);
			this.toggleCooldown = TOGGLE_DELAY;
		}
	}
}