package vswe.stevescarts.module.engine;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.ModuleType;

// Moteur solaire compact - version améliorée du moteur solaire standard
public class CompactSolarEngineModule extends SolarEngineModule {
    public CompactSolarEngineModule(CartEntity cart, ModuleType<?> type, long maxPower) {
        super(cart, type, maxPower);
    }
}
