package vswe.stevescarts.module.realtimer;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.ModuleType;

public class FarmerModule extends CartModule {

    public FarmerModule(CartEntity cart, ModuleType<?> type) {
        super(cart, type);
    }

    // @Override
    public void update() {
        // Fermage en temps réel
    }
}
