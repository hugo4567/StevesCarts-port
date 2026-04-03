package vswe.stevescarts.module.hull;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.ModuleType;

public abstract class HullModule extends CartModule {
    public HullModule(CartEntity cart, ModuleType<?> type) {
        super(cart, type);
    }
}
