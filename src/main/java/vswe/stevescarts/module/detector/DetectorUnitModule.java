package vswe.stevescarts.module.detector;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.Configurable;
import vswe.stevescarts.module.ModuleType;
import vswe.stevescarts.screen.CartHandler;
import vswe.stevescarts.util.TextHelper;

/**
 * Detector Unit Module - Basic detection module
 * Detects various block types and triggers based on configuration
 */
public class DetectorUnitModule extends CartModule implements Configurable {
    private int blockTypeToDetect = 0;  // 0=any, 1=ore, 2=wood, etc
    private boolean blockDetected = false;
    private static final int DETECTION_RANGE = 8;
    private int detectionX = 0;
    private int detectionY = 0;
    private int detectionZ = 0;

    public DetectorUnitModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        if (getEntity() == null || getEntity().world == null) return;
        
        performDetection();
    }

    private void performDetection() {
        blockDetected = false;
        var railPos = getRailPos();
        
        // Scan range around cart
        for (int x = -DETECTION_RANGE; x <= DETECTION_RANGE; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -DETECTION_RANGE; z <= DETECTION_RANGE; z++) {
                    var checkPos = railPos.add(x, y, z);
                    var state = getEntity().world.getBlockState(checkPos);
                    
                    if (matchesDetectionType(state)) {
                        blockDetected = true;
                        detectionX = x;
                        detectionY = y;
                        detectionZ = z;
                        return;
                    }
                }
            }
        }
    }

    private boolean matchesDetectionType(net.minecraft.block.BlockState state) {
        return switch(blockTypeToDetect) {
            case 0 -> true;  // detect any block
            case 1 -> state.getMaterial().isReplaceable();
            case 2 -> false;  // simplified: skip WoodType check
            default -> false;
        };
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel label = new WLabel(TextHelper.literal("Detector Unit"));
        panel.add(label, 0, 0);
        
        WLabel statusLabel = new WLabel(TextHelper.literal(blockDetected ? "Block Found" : "Searching..."));
        panel.add(statusLabel, 0, 12);
        
        panel.setSize(120, 30);
    }

    public boolean isBlockDetected() {
        return blockDetected;
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        super.writeToNbt(nbt);
        nbt.putInt("BlockType", blockTypeToDetect);
        nbt.putBoolean("BlockDetected", blockDetected);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        blockTypeToDetect = nbt.getInt("BlockType");
        blockDetected = nbt.getBoolean("BlockDetected");
    }
}