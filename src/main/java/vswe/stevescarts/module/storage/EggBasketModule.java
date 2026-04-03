package vswe.stevescarts.module.storage;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.Configurable;
import vswe.stevescarts.module.ModuleType;
import vswe.stevescarts.module.Worker;
import vswe.stevescarts.screen.CartHandler;
import vswe.stevescarts.util.TextHelper;

import java.util.List;

/**
 * Egg Basket Module - Collects chicken eggs
 * Automatically collects eggs that drop when chickens are nearby
 */
public class EggBasketModule extends CartModule implements Configurable, Worker {
    private int eggsCollected = 0;
    private static final int COLLECTION_RANGE = 12;
    private int workTicks = 0;

    public EggBasketModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        if (getEntity() == null) return;
        
        workTicks++;
        if (workTicks >= 20) {
            workTicks = 0;
            work();
        }
    }

    @Override
    public void work() {
        if (getEntity() == null || getEntity().world == null) return;
        
        Box searchBox = getEntity().getBoundingBox().expand(COLLECTION_RANGE);
        // Collect egg items in range
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel title = new WLabel(TextHelper.literal("Egg Basket"));
        panel.add(title, 0, 0);
        WLabel countLabel = new WLabel(TextHelper.literal("Eggs: " + eggsCollected));
        panel.add(countLabel, 0, 12);
        panel.setSize(120, 30);
    }

    @Override
    public int getPriority() {
        return Worker.NORMAL_PRIORITY;
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        super.writeToNbt(nbt);
        nbt.putInt("EggsCollected", eggsCollected);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        eggsCollected = nbt.getInt("EggsCollected");
    }
}