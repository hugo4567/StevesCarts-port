package vswe.stevescarts.module.addon;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.Configurable;
import vswe.stevescarts.module.ModuleType;
import vswe.stevescarts.module.addon.Toggleable;

/**
 * Liquid Cleaner Module - Removes unwanted fluids
 *
 * This module drains fluids from the world and stores them in
 * the cart's internal tanks. It supports multiple fluid types.
 */
public class LiquidCleanerModule extends CartModule implements Configurable, Toggleable {
	public LiquidCleanerModule(CartEntity minecart, ModuleType<?> type) {
		super(minecart, type);
	}

	@Override
	public void tick() {
		// Implement fluid cleaning logic here
	}
}