package vswe.stevescarts.module.processor;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
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
 * Extreme Melter Module - Melts blocks into fluids at high temperature
 * Converts ores and other blocks into molten form
 */
public class ExtremeMelterModule extends CartModule implements Configurable, Worker {
    private long fluidProduced = 0;
    private int temperature = 0;
    private static final int MAX_TEMP = 3000;
    private int workTicks = 0;
    private static final int MELT_INTERVAL = 15;

    public ExtremeMelterModule(CartEntity minecart, ModuleType<?> type) {
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
        // Melt items into fluids at extreme temperatures
        if (temperature >= 2000) {
            fluidProduced += 100;  // 100 mB per item
        }
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel title = new WLabel(TextHelper.literal("Extreme Melter"));
        panel.add(title, 0, 0);
        WLabel fluidLabel = new WLabel(TextHelper.literal("Fluid: " + (fluidProduced / 1000) + "L"));
        panel.add(fluidLabel, 0, 12);
        WLabel tempLabel = new WLabel(TextHelper.literal("Temp: " + temperature + "/" + MAX_TEMP + "°K"));
        panel.add(tempLabel, 80, 12);
        panel.setSize(170, 30);
    }

    public void addHeat(int amount) {
        temperature = Math.min(temperature + amount, MAX_TEMP);
    }

    @Override
    public int getPriority() {
        return Worker.HIGHEST_PRIORITY;
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        super.writeToNbt(nbt);
        nbt.putLong("FluidProduced", fluidProduced);
        nbt.putInt("Temperature", temperature);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        fluidProduced = nbt.getLong("FluidProduced");
        temperature = nbt.getInt("Temperature");
    }
}