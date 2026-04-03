package vswe.stevescarts.module;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import vswe.stevescarts.entity.CartEntity;

public class CreativeTankModule extends Module {

    public CreativeTankModule(CartEntity cart) {
        super(cart);
    }

    @Override
    public void onInteract(PlayerEntity player, Hand hand, ItemStack stack) {
        // Logic for interacting with the module
    }

    @Override
    public void update() {
        // Logic for updating the module each tick
    }
}