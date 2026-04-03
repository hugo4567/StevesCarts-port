package vswe.stevescarts.module.hull;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.ModuleType;

public class ReinforcedHullModule extends HullModule {
    public ReinforcedHullModule(CartEntity cart, ModuleType<?> type) {
        super(cart, type);
    }

    @Override
    public int getConsumption(boolean isMoving) {
        return isMoving ? 3 : 0;
    }
}