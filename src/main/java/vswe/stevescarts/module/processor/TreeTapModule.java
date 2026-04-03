package vswe.stevescarts.module.processor;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.ModuleType;

public class TreeTapModule extends CartModule {

    public TreeTapModule(CartEntity cart, ModuleType<?> type) {
        super(cart, type);
    }

    // @Override
    public void update() {
        // Extraire la résine des arbres
    }
}
