package vswe.stevescarts.module.storage;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.ModuleType;

public class InternalTankModule extends TankModule {
    public InternalTankModule(CartEntity cart, ModuleType<?> type) {
        super(cart, type, 5000); // Capacité de 5000 unités
    }
}