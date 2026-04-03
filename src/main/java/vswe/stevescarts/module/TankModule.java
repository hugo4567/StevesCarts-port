package vswe.stevescarts.module;

import net.minecraft.nbt.NbtCompound;
import vswe.stevescarts.entity.CartEntity;

public class TankModule extends CartModule {
    private int capacity;

    public TankModule(CartEntity cart, ModuleType<?> type, int capacity) {
        super(cart, type);
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        this.capacity = nbt.getInt("Capacity");
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        super.writeToNbt(nbt);
        nbt.putInt("Capacity", this.capacity);
    }
}