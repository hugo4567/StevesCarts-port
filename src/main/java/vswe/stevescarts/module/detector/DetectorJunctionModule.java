package vswe.stevescarts.module.detector;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Direction;
import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.Configurable;
import vswe.stevescarts.module.ModuleType;
import vswe.stevescarts.screen.CartHandler;
import vswe.stevescarts.util.TextHelper;

/**
 * Detector Junction Module - Detects and manages junctions
 * Detects track junctions and can switch tracks
 */
public class DetectorJunctionModule extends CartModule implements Configurable {
    private boolean junctionDetected = false;
    private int detectionRange = 5;
    private Direction detectedDirection = Direction.UP;

    public DetectorJunctionModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        if (getEntity() == null || checkMovement()) {
            detectJunction();
        }
    }

    private void detectJunction() {
        junctionDetected = false;
        if (getEntity() != null && getEntity().world != null) {
            var railPos = getRailPos();
            // Check adjacent track positions for junctions
            for (Direction dir : Direction.values()) {
                var checkPos = railPos.offset(dir);
                var state = getEntity().world.getBlockState(checkPos);
                // Check if it's a rail block
                if (state.getBlock() instanceof net.minecraft.block.RailBlock) {
                    junctionDetected = true;
                    detectedDirection = dir;
                    break;
                }
            }
        }
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel label = new WLabel(TextHelper.literal("Detector Junction"));
        panel.add(label, 0, 0);
        
        WLabel statusLabel = new WLabel(TextHelper.literal(junctionDetected ? "Junction Found" : "No Junction"));
        panel.add(statusLabel, 0, 12);
        
        panel.setSize(120, 30);
    }

    public boolean isJunctionDetected() {
        return junctionDetected;
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        super.writeToNbt(nbt);
        nbt.putBoolean("JunctionDetected", junctionDetected);
        nbt.putInt("DetectionRange", detectionRange);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        junctionDetected = nbt.getBoolean("JunctionDetected");
        detectionRange = nbt.getInt("DetectionRange");
    }
}