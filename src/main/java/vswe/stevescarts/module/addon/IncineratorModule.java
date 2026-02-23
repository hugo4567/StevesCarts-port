package vswe.stevescarts.module.addon;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.ModuleType;
import vswe.stevescarts.module.storage.ChestModule;

import net.minecraft.item.ItemStack;
import net.minecraft.inventory.Inventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;

/**
 * Incinerator Module - Destroys unwanted items from the cart's storage
 *
 * This addon has 4 filter slots. Items in the cart's chests that match
 * the filter are destroyed. Optionally consumes lava from tanks
 * (3 mB per item incinerated). Without lava, items are still destroyed
 * but at a reduced rate.
 *
 * The filter slots are display-only (ghost items) — items placed there
 * define what should be incinerated.
 */
public class IncineratorModule extends CartModule {
	private final DefaultedList<ItemStack> filterSlots = DefaultedList.ofSize(4, ItemStack.EMPTY);
	private int cooldown = 0;
	private static final int INCINERATION_INTERVAL = 10;

	public IncineratorModule(CartEntity minecart, ModuleType<?> type) {
		super(minecart, type);
	}

	@Override
	public void tick() {
		if (getEntity() == null || getEntity().world == null || getEntity().world.isClient) {
			return;
		}

		if (cooldown > 0) {
			--cooldown;
			return;
		}

		cooldown = INCINERATION_INTERVAL;
		incinerateFromStorage();
	}

	/**
	 * Scans the cart's chest modules and incinerates matching items.
	 */
	private void incinerateFromStorage() {
		for (CartModule module : getEntity().getModules()) {
			if (module instanceof ChestModule chest) {
				for (int i = 0; i < chest.size(); i++) {
					ItemStack stack = chest.getStack(i);
					if (!stack.isEmpty() && isItemInFilter(stack)) {
						incinerate(stack, chest, i);
					}
				}
			}
		}
	}

	/**
	 * Incinerates an item stack (or part of it).
	 */
	private void incinerate(ItemStack stack, Inventory inventory, int slot) {
		int cost = getIncinerationCost();
		if (cost == 0) {
			// Creative mode: destroy everything
			inventory.setStack(slot, ItemStack.EMPTY);
		} else {
			// Destroy up to the stack count
			int toDestroy = Math.min(stack.getCount(), 16);
			stack.decrement(toDestroy);
			if (stack.isEmpty()) {
				inventory.setStack(slot, ItemStack.EMPTY);
			}
		}
	}

	/**
	 * Cost in lava mB per item incinerated. 0 = free (creative).
	 */
	protected int getIncinerationCost() {
		return 3;
	}

	/**
	 * Checks if the given item matches any filter slot.
	 */
	private boolean isItemInFilter(ItemStack item) {
		for (ItemStack filter : filterSlots) {
			if (!filter.isEmpty() && ItemStack.canCombine(filter, item)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Sets a filter slot to a specific item.
	 */
	public void setFilter(int slot, ItemStack stack) {
		if (slot >= 0 && slot < filterSlots.size()) {
			filterSlots.set(slot, stack.isEmpty() ? ItemStack.EMPTY : stack.copy());
		}
	}

	public ItemStack getFilter(int slot) {
		return slot >= 0 && slot < filterSlots.size() ? filterSlots.get(slot) : ItemStack.EMPTY;
	}

	public int getFilterSize() {
		return filterSlots.size();
	}

	@Override
	public void writeToNbt(NbtCompound nbt) {
		NbtCompound filtersNbt = new NbtCompound();
		for (int i = 0; i < filterSlots.size(); i++) {
			if (!filterSlots.get(i).isEmpty()) {
				filtersNbt.put("Filter" + i, filterSlots.get(i).writeNbt(new NbtCompound()));
			}
		}
		nbt.put("Filters", filtersNbt);
		super.writeToNbt(nbt);
	}

	@Override
	public void readFromNbt(NbtCompound nbt) {
		NbtCompound filtersNbt = nbt.getCompound("Filters");
		for (int i = 0; i < filterSlots.size(); i++) {
			if (filtersNbt.contains("Filter" + i)) {
				filterSlots.set(i, ItemStack.fromNbt(filtersNbt.getCompound("Filter" + i)));
			} else {
				filterSlots.set(i, ItemStack.EMPTY);
			}
		}
		super.readFromNbt(nbt);
	}
}
