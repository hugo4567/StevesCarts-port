package vswe.stevescarts.module.addon;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.Configurable;
import vswe.stevescarts.module.ModuleType;
import vswe.stevescarts.module.addon.Toggleable;

/**
 * Tree Tap Module - Harvests sap and plants trees
 *
 * This module interacts with trees to harvest sap and replant
 * saplings. It supports a variety of tree types.
 */
public class TreeTapModule extends CartModule implements Configurable, Toggleable {
	public TreeTapModule(CartEntity minecart, ModuleType<?> type) {
		super(minecart, type);
	}

	@Override
	public void tick() {
		// Implement tree tapping logic here
	}
}