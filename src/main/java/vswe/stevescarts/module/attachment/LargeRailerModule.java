package vswe.stevescarts.module.attachment;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.ModuleType;

import net.minecraft.nbt.NbtCompound;

/**
 * Large Railer Module - Grand poseur de rails
 * Étend les capacités du Railer standard pour une portée plus grande
 */
public class LargeRailerModule extends RailerModule {
	public LargeRailerModule(CartEntity minecart, ModuleType<?> type) {
		super(minecart, type, 2); // Double portée par rapport au railer standard
	}

	@Override
	public void writeToNbt(NbtCompound nbt) {
		super.writeToNbt(nbt);
		// Sauvegarder l'état du module
	}

	@Override
	public void readFromNbt(NbtCompound nbt) {
		super.readFromNbt(nbt);
		// Charger l'état du module
	}

	protected String getDiscriminator() {
		return "large_railer";
	}
}
