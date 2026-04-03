package vswe.stevescarts.module.addon;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.ModuleType;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;

/**
 * Creative Supplies Module - Provides infinite creative-mode supplies
 * Generates items infinitely for creative/testing purposes
 */
public class CreativeSuppliesModule extends CartModule {
	protected final SimpleInventory inventory = new SimpleInventory(27);
	protected int refillTimer = 0;
	protected static final int REFILL_INTERVAL = 100; // Refill every 5 seconds

	public CreativeSuppliesModule(CartEntity minecart, ModuleType<?> type) {
		super(minecart, type);
	}

	@Override
	public void writeToNbt(NbtCompound nbt) {
		nbt.putInt("RefillTimer", this.refillTimer);
		super.writeToNbt(nbt);
	}

	@Override
	public void readFromNbt(NbtCompound nbt) {
		this.refillTimer = nbt.getInt("RefillTimer");
		super.readFromNbt(nbt);
	}

	public SimpleInventory getInventory() {
		return this.inventory;
	}
}