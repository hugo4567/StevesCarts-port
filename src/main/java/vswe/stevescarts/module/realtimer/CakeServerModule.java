package vswe.stevescarts.module.realtimer;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.ModuleType;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;

/**
 * Cake Server Module - Serves cake slices to nearby players
 *
 * This module stores cake slices internally (up to 10 cakes = 60 slices).
 * It consumes Cake items from the cart's inventory to refill its buffer.
 * Players can interact with the cart to eat a slice, restoring 2 hunger
 * points with 0.1 saturation per slice.
 *
 * In creative mode, the buffer slowly refills automatically.
 */
public class CakeServerModule extends CartModule {
	private int cakeBuffer = 0;
	private int cooldown = 0;

	private static final int MAX_CAKES = 10;
	private static final int SLICES_PER_CAKE = 6;
	private static final int MAX_TOTAL_SLICES = MAX_CAKES * SLICES_PER_CAKE; // 60

	public CakeServerModule(CartEntity minecart, ModuleType<?> type) {
		super(minecart, type);
	}

	@Override
	public void tick() {
		if (getEntity() == null || getEntity().world == null || getEntity().world.isClient) {
			return;
		}

		// Creative mode auto-refill
		if (getEntity().world.getServer() != null) {
			boolean isCreative = getEntity().world.getPlayers().stream()
				.anyMatch(p -> p.isCreative() && p.squaredDistanceTo(getEntity()) < 100);
			if (isCreative && cooldown <= 0) {
				if (cakeBuffer < MAX_TOTAL_SLICES) {
					cakeBuffer++;
				}
				cooldown = 20;
			} else if (cooldown > 0) {
				--cooldown;
			}
		}

		// Try to consume cake items from cart storage modules
		if (cakeBuffer <= MAX_TOTAL_SLICES - SLICES_PER_CAKE) {
			tryConsumeCake();
		}
	}

	/**
	 * Attempts to consume a cake item from the cart's storage to add slices.
	 */
	private void tryConsumeCake() {
		for (CartModule module : getEntity().getModules()) {
			if (module instanceof vswe.stevescarts.module.storage.ChestModule chest) {
				for (int i = 0; i < chest.size(); i++) {
					ItemStack stack = chest.getStack(i);
					if (stack.isOf(Items.CAKE)) {
						stack.decrement(1);
						cakeBuffer += SLICES_PER_CAKE;
						return;
					}
				}
			}
		}
	}

	/**
	 * Called when a player interacts with the cart.
	 * Serves one cake slice if the player can eat.
	 * @return true if a slice was served
	 */
	public boolean serveSlice(PlayerEntity player) {
		if (cakeBuffer <= 0) {
			return false;
		}
		if (!player.getHungerManager().isNotFull()) {
			return false;
		}

		cakeBuffer--;
		player.getHungerManager().add(2, 0.1f);
		return true;
	}

	/**
	 * Returns the number of full cakes remaining.
	 */
	public int getCakes() {
		return cakeBuffer / SLICES_PER_CAKE;
	}

	/**
	 * Returns the number of remaining slices (partial cake).
	 */
	public int getSlices() {
		return cakeBuffer % SLICES_PER_CAKE;
	}

	/**
	 * Returns true if there are any slices available.
	 */
	public boolean hasSupplies() {
		return cakeBuffer > 0;
	}

	public int getCakeBuffer() {
		return cakeBuffer;
	}

	public int getMaxSlices() {
		return MAX_TOTAL_SLICES;
	}

	@Override
	public void writeToNbt(NbtCompound nbt) {
		nbt.putShort("CakeBuffer", (short) cakeBuffer);
		super.writeToNbt(nbt);
	}

	@Override
	public void readFromNbt(NbtCompound nbt) {
		cakeBuffer = nbt.getShort("CakeBuffer");
		super.readFromNbt(nbt);
	}
}
