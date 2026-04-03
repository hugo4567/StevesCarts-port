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
 * Advanced Smelter Module - Fast smelting
 * More efficient smelting with higher throughput
 */
public class AdvancedSmelterModule extends CartModule implements Configurable, Worker {
    private int itemsSmelted = 0;
    private int fuel = 0;
    private static final int MAX_FUEL = 3000;
    private int workTicks = 0;
    private static final int SMELT_INTERVAL = 25;  // Smelt every 1.25 seconds

    public AdvancedSmelterModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        if (getEntity() == null) return;
        
        workTicks++;
        if (workTicks >= SMELT_INTERVAL) {
            workTicks = 0;
            work();
        }
    }

    @Override
    public void work() {
        // Smelt items if we have fuel
        if (fuel > 0) {
            itemsSmelted++;
            fuel--;
        }
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel title = new WLabel(TextHelper.literal("Advanced Smelter"));
        panel.add(title, 0, 0);
        WLabel countLabel = new WLabel(TextHelper.literal("Smelted: " + itemsSmelted));
        panel.add(countLabel, 0, 12);
        WLabel fuelLabel = new WLabel(TextHelper.literal("Fuel: " + fuel + "/" + MAX_FUEL));
        panel.add(fuelLabel, 90, 12);
        panel.setSize(160, 30);
    }

    public void addFuel(int amount) {
        fuel = Math.min(fuel + amount, MAX_FUEL);
    }

    @Override
    public int getPriority() {
        return Worker.HIGHER_PRIORITY;
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        super.writeToNbt(nbt);
        nbt.putInt("ItemsSmelted", itemsSmelted);
        nbt.putInt("Fuel", fuel);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        itemsSmelted = nbt.getInt("ItemsSmelted");
        fuel = nbt.getInt("Fuel");
    }
}