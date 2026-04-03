package vswe.stevescarts.module.processor;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.Configurable;
import vswe.stevescarts.module.ModuleType;
import vswe.stevescarts.module.Worker;
import vswe.stevescarts.screen.CartHandler;
import vswe.stevescarts.util.TextHelper;

/**
 * Basic Smelter Module - Simple smelting furnace
 * Smelts ores and other items into finished products
 */
public class BasicSmelterModule extends CartModule implements Configurable, Worker {
    private int itemsSmelted = 0;
    private int fuelLevel = 0;
    private static final int MAX_FUEL = 1800;  // Coal = 1600 ticks
    private int workTicks = 0;
    private static final int SMELT_TIME = 30;  // Ticks per item

    public BasicSmelterModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        if (getEntity() == null) return;
        
        workTicks++;
        if (workTicks >= SMELT_TIME) {
            workTicks = 0;
            work();
        }
    }

    @Override
    public void work() {
        if (fuelLevel > 0) {
            itemsSmelted++;
            fuelLevel = Math.max(0, fuelLevel - 1);
        }
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel title = new WLabel(TextHelper.literal("Basic Smelter"));
        panel.add(title, 0, 0);
        WLabel countLabel = new WLabel(TextHelper.literal("Smelted: " + itemsSmelted));
        panel.add(countLabel, 0, 12);
        panel.setSize(140, 30);
    }

    public void addFuel(int amount) {
        fuelLevel = Math.min(fuelLevel + amount, MAX_FUEL);
    }

    @Override
    public int getPriority() {
        return Worker.NORMAL_PRIORITY;
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        super.writeToNbt(nbt);
        nbt.putInt("ItemsSmelted", itemsSmelted);
        nbt.putInt("FuelLevel", fuelLevel);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        itemsSmelted = nbt.getInt("ItemsSmelted");
        fuelLevel = nbt.getInt("FuelLevel");
    }
}