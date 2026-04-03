package vswe.stevescarts.module.tool;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.ModuleType;

public class DiamondDrillModule extends CartModule {

    public DiamondDrillModule(CartEntity cart, ModuleType<?> type) {
        super(cart, type);
    }

    // @Override
    public void update() {
        // Percer avec diamant
    }
}
