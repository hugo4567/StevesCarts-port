package vswe.stevescarts.module.manager;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.Configurable;
import vswe.stevescarts.module.ModuleType;
import vswe.stevescarts.screen.CartHandler;
import vswe.stevescarts.util.TextHelper;

/**
 * Detector Manager Module - Manages detector outputs
 * Combines results from multiple detector modules
 */
public class DetectorManagerModule extends CartModule implements Configurable {
    private boolean anyDetected = false;
    private boolean allDetected = false;
    private int detectorCount = 0;
    private int detectorsTriggers = 0;
    private int checkInterval = 10;
    private int lastCheckTick = 0;

    public DetectorManagerModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        if (getEntity() == null) return;
        
        lastCheckTick++;
        if (lastCheckTick >= checkInterval) {
            lastCheckTick = 0;
            updateDetectorStatus();
        }
    }

    private void updateDetectorStatus() {
        // This would check all detector modules in the cart
        // and combine their results
        anyDetected = false;
        allDetected = true;
        detectorsTriggers = 0;
        
        if (getEntity() != null && getEntity().getModules() != null) {
            detectorCount = 0;
            // Count detector modules - implementation depends on module system
        }
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel title = new WLabel(TextHelper.literal("Detector Manager"));
        panel.add(title, 0, 0);
        
        WLabel countLabel = new WLabel(TextHelper.literal("Detectors: " + detectorCount));
        panel.add(countLabel, 0, 12);
        
        String statusText = anyDetected ? "Detection Active" : "Idle";
        WLabel statusLabel = new WLabel(TextHelper.literal(statusText));
        panel.add(statusLabel, 80, 12);
        
        panel.setSize(160, 30);
    }

    public boolean isAnyDetected() {
        return anyDetected;
    }

    public boolean areAllDetected() {
        return allDetected;
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        super.writeToNbt(nbt);
        nbt.putBoolean("AnyDetected", anyDetected);
        nbt.putBoolean("AllDetected", allDetected);
        nbt.putInt("DetectorCount", detectorCount);
        nbt.putInt("DetectorsTriggers", detectorsTriggers);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        anyDetected = nbt.getBoolean("AnyDetected");
        allDetected = nbt.getBoolean("AllDetected");
        detectorCount = nbt.getInt("DetectorCount");
        detectorsTriggers = nbt.getInt("DetectorsTriggers");
    }
}