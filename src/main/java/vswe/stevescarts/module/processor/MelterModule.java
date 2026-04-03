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
 * Melter Module - Melts items into fluids
 * Converts ores and blocks into liquid form
 */
public class MelterModule extends CartModule implements Configurable, Worker {
    private long fluidProduced = 0;
    private int heat = 0;
    private static final int MAX_HEAT = 2000;
    private int workTicks = 0;
    private static final int MELT_INTERVAL = 25;

    public MelterModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        if (getEntity() == null) return;
        
        workTicks++;
        if (workTicks >= MELT_INTERVAL) {
            workTicks = 0;
            work();
        }
    }

    @Override
    public void work() {
        // Melt items into fluids
        if (heat >= 1000) {
            fluidProduced += 75;  // 75 mB per item
            heat = Math.max(0, heat - 50);  // Cool down from work
        }
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel title = new WLabel(TextHelper.literal("Melter"));
        panel.add(title, 0, 0);
        WLabel fluidLabel = new WLabel(TextHelper.literal("Fluid: " + (fluidProduced / 1000) + "L"));
        panel.add(fluidLabel, 0, 12);
        WLabel heatLabel = new WLabel(TextHelper.literal("Heat: " + heat + "/" + MAX_HEAT + "°"));
        panel.add(heatLabel, 90, 12);
        panel.setSize(160, 30);
    }

    public void addHeat(int amount) {
        heat = Math.min(heat + amount, MAX_HEAT);
    }

    @Override
    public int getPriority() {
        return Worker.HIGHER_PRIORITY;
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        super.writeToNbt(nbt);
        nbt.putLong("FluidProduced", fluidProduced);
        nbt.putInt("Heat", heat);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        fluidProduced = nbt.getLong("FluidProduced");
        heat = nbt.getInt("Heat");
    }
}