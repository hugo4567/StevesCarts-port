package vswe.stevescarts.module.processor;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.ModuleType;

public class StoneCutterModule extends CartModule {

    public StoneCutterModule(CartEntity cart, ModuleType<?> type) {
        super(cart, type);
    }

    // @Override
    public void update() {
        // Découper la pierre
    }
}
