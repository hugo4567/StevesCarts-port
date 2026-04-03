package vswe.stevescarts.module.processor;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.Configurable;
import vswe.stevescarts.module.ModuleType;
import vswe.stevescarts.module.Worker;
import vswe.stevescarts.screen.CartHandler;
import vswe.stevescarts.util.TextHelper;

/**
 * Creative Incinerator Module - Destroys items instantly
 * Infinite item destruction without fuel
 */
public class CreativeIncineratorModule extends CartModule implements Configurable, Worker {
    private long itemsDestroyed = 0;
    private int workTicks = 0;

    public CreativeIncineratorModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        if (getEntity() == null) return;
        
        workTicks++;
        if (workTicks >= 10) {
            workTicks = 0;
            work();
        }
    }

    @Override
    public void work() {
        // Destroy items from cart inventory
        // No fuel needed
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel title = new WLabel(TextHelper.literal("Creative Incinerator"));
        panel.add(title, 0, 0);
        WLabel countLabel = new WLabel(TextHelper.literal("Destroyed: " + itemsDestroyed));
        panel.add(countLabel, 0, 12);
        WLabel powerLabel = new WLabel(TextHelper.literal("Power: ∞"));
        panel.add(powerLabel, 90, 12);
        panel.setSize(160, 30);
    }

    @Override
    public int getPriority() {
        return Worker.HIGHEST_PRIORITY;
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        super.writeToNbt(nbt);
        nbt.putLong("ItemsDestroyed", itemsDestroyed);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        itemsDestroyed = nbt.getLong("ItemsDestroyed");
    }
}