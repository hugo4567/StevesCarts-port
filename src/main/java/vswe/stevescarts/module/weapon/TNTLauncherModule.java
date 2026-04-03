package vswe.stevescarts.module.weapon;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.ModuleType;

public class TNTLauncherModule extends CartModule {

    public TNTLauncherModule(CartEntity cart, ModuleType<?> type) {
        super(cart, type);
    }

    // @Override
    public void update() {
        // Lancer des TNT
    }
}
