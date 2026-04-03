package vswe.stevescarts.module.detector;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.block.RedstoneWireBlock;
import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.Configurable;
import vswe.stevescarts.module.ModuleType;
import vswe.stevescarts.screen.CartHandler;
import vswe.stevescarts.util.TextHelper;

/**
 * Detector Redstone Unit Module - Detects redstone signals
 * Detects redstone power levels and activates based on threshold
 */
public class DetectorRedstoneUnitModule extends CartModule implements Configurable {
    private int redstoneLevel = 0;
    private int threshold = 8;
    private int detectionRadius = 3;

    public DetectorRedstoneUnitModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        if (getEntity() == null || getEntity().world == null) return;
        
        checkRedstoneLevel();
    }

    private void checkRedstoneLevel() {
        var railPos = getRailPos();
        var centerState = getEntity().world.getBlockState(railPos);
        
        // Get redstone power level if it's a redstone wire
        if (centerState.getBlock() instanceof RedstoneWireBlock) {
            redstoneLevel = centerState.get(RedstoneWireBlock.POWER);
        } else {
            // Check redstone power from adjacent redstone
            redstoneLevel = getEntity().world.getReceivedRedstonePower(railPos);
        }
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel label = new WLabel(TextHelper.literal("Detector Redstone Unit"));
        panel.add(label, 0, 0);
        
        WLabel levelLabel = new WLabel(TextHelper.literal("Power: " + redstoneLevel + "/15"));
        panel.add(levelLabel, 0, 12);
        
        WLabel threshLabel = new WLabel(TextHelper.literal("Threshold: " + threshold));
        panel.add(threshLabel, 70, 12);
        
        panel.setSize(140, 30);
    }

    public int getRedstoneLevel() {
        return redstoneLevel;
    }

    public boolean isAboveThreshold() {
        return redstoneLevel >= threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = Math.min(Math.max(threshold, 0), 15);
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        super.writeToNbt(nbt);
        nbt.putInt("RedstoneLevel", redstoneLevel);
        nbt.putInt("Threshold", threshold);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        redstoneLevel = nbt.getInt("RedstoneLevel");
        threshold = nbt.getInt("Threshold");
    }
}