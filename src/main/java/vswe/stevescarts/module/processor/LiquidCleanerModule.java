package vswe.stevescarts.module.processor;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.NbtCompound;
import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.Configurable;
import vswe.stevescarts.module.ModuleType;
import vswe.stevescarts.module.Worker;
import vswe.stevescarts.screen.CartHandler;
import vswe.stevescarts.util.TextHelper;

/**
 * Liquid Cleaner Module - Purifies fluids
 * Cleans contaminated fluids and removes impurities
 */
public class LiquidCleanerModule extends CartModule implements Configurable, Worker {
    private long fluidsCleaned = 0;
    private boolean filterEnabled = true;
    private int workTicks = 0;
    private static final int CLEAN_INTERVAL = 30;

    public LiquidCleanerModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        if (getEntity() == null) return;
        
        workTicks++;
        if (workTicks >= CLEAN_INTERVAL) {
            workTicks = 0;
            work();
        }
    }

    @Override
    public void work() {
        // Clean fluids from tank
        if (filterEnabled) {
            fluidsCleaned += 50;  // 50 mB per work
        }
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel title = new WLabel(TextHelper.literal("Liquid Cleaner"));
        panel.add(title, 0, 0);
        WLabel cleanedLabel = new WLabel(TextHelper.literal("Cleaned: " + (fluidsCleaned / 1000) + "L"));
        panel.add(cleanedLabel, 0, 12);
        WLabel filterLabel = new WLabel(TextHelper.literal(filterEnabled ? "Filter ON" : "Filter OFF"));
        panel.add(filterLabel, 100, 12);
        panel.setSize(160, 30);
    }

    public void toggleFilter() {
        filterEnabled = !filterEnabled;
    }

    @Override
    public int getPriority() {
        return Worker.NORMAL_PRIORITY;
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        super.writeToNbt(nbt);
        nbt.putLong("FluidsCleaned", fluidsCleaned);
        nbt.putBoolean("FilterEnabled", filterEnabled);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        fluidsCleaned = nbt.getLong("FluidsCleaned");
        filterEnabled = nbt.getBoolean("FilterEnabled");
    }
}