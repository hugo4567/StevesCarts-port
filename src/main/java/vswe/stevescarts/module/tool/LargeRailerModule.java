package vswe.stevescarts.module.tool;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.ModuleType;

public class LargeRailerModule extends CartModule {

    public LargeRailerModule(CartEntity cart, ModuleType<?> type) {
        super(cart, type);
    }

    @Override
    public void tick() {
        // Placer des rails à large écart
    }
}
