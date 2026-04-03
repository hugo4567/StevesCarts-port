package vswe.stevescarts.module.manager;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.ModuleType;

public class InventoryEvalizerModule extends CartModule {

    public InventoryEvalizerModule(CartEntity cart, ModuleType<?> type) {
        super(cart, type);
    }

    // @Override
    public void update() {
        // Rééquilibrer l'inventaire
    }
}
