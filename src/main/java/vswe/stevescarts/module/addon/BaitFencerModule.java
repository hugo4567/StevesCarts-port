package vswe.stevescarts.module.addon;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.ModuleType;

public class BaitFencerModule extends CartModule {

    public BaitFencerModule(CartEntity cart, ModuleType<?> type) {
        super(cart, type);
    }

    // @Override
    public void update() {
        // Appâter et clôturer
    }
}
