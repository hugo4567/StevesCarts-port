package vswe.stevescarts.module.attachment;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.ModuleType;

public class LiquidDrainerModule extends CartModule {

    public LiquidDrainerModule(CartEntity cart, ModuleType<?> type) {
        super(cart, type);
    }

    @Override
    public void tick() {
        // Drainer les liquides
    }
}
