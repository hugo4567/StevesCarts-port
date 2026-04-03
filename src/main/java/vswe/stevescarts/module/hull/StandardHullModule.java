package vswe.stevescarts.module.hull;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.ModuleType;

public class StandardHullModule extends HullModule {
    public StandardHullModule(CartEntity cart, ModuleType<?> type) {
        super(cart, type);
    }

    @Override
    public int getConsumption(boolean isMoving) {
        return isMoving ? 1 : 0;
    }
}