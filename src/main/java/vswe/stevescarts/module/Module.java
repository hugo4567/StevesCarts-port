package vswe.stevescarts.module;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import vswe.stevescarts.entity.CartEntity;

/**
 * Base class for all cart modules.
 * This is a simplified module class that can be extended for different module types.
 */
public abstract class Module {
    protected CartEntity cart;
    protected ModuleType<?> type;
    protected String moduleName;

    public Module(CartEntity cart) {
        this.cart = cart;
    }

    public Module(String moduleName) {
        this.moduleName = moduleName;
        this.cart = null;
    }

    public void initialize() {
    }

    public void update() {
    }

    public void onInteract(PlayerEntity player, Hand hand, ItemStack stack) {
    }

    public void readFromNbt(NbtCompound nbt) {
    }

    public void writeToNbt(NbtCompound nbt) {
    }

    public CartEntity getCart() {
        return cart;
    }

    public void setCart(CartEntity cart) {
        this.cart = cart;
    }

    public ModuleType<?> getType() {
        return type;
    }

    public void setType(ModuleType<?> type) {
        this.type = type;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String name) {
        this.moduleName = name;
    }
}
