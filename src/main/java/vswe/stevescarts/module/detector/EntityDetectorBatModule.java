package vswe.stevescarts.module.detector;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.ModuleType;

public class EntityDetectorBatModule extends CartModule {

    public EntityDetectorBatModule(CartEntity cart, ModuleType<?> type) {
        super(cart, type);
    }

    // @Override
    public void update() {
        // Détecter les chauves-souris
    }
}
