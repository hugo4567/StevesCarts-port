package vswe.stevescarts.module.engine;

import vswe.stevescarts.module.ModuleType;
import vswe.stevescarts.entity.CartEntity;

/**
 * Compact Solar Engine Module
 * Smaller than the standard solar engine but still effective
 * Max capacity: 12,500 (half of BasicSolarEngine)
 * Generation speed: 3 per tick (slower than standard)
 */
public class CompactSolarEngineModule extends SolarEngineModule {

	public CompactSolarEngineModule(CartEntity minecart, ModuleType<?> type) {
		super(minecart, type, 12500L);
	}
}
