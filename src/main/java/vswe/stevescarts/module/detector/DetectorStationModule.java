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
 * Detector Station Module - Detects station blocks
 * Detects special detector rail station blocks for triggering actions
 */
public class DetectorStationModule extends CartModule implements Configurable {
    private boolean stationNearby = false;
    private int stationDistance = -1;
    private static final int DETECTION_RANGE = 10;

    public DetectorStationModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        if (getEntity() == null || getEntity().world == null) return;
        
        detectStation();
    }

    private void detectStation() {
        stationNearby = false;
        stationDistance = -1;
        
        var railPos = getRailPos();
        
        // Check for detector rail blocks in range
        for (int x = -DETECTION_RANGE; x <= DETECTION_RANGE; x++) {
            for (int z = -DETECTION_RANGE; z <= DETECTION_RANGE; z++) {
                var checkPos = railPos.add(x, 0, z);
                var state = getEntity().world.getBlockState(checkPos);
                if (state.getBlock() instanceof net.minecraft.block.DetectorRailBlock) {
                    int distance = (int) Math.sqrt(x*x + z*z);
                    if (!stationNearby || distance < stationDistance) {
                        stationNearby = true;
                        stationDistance = distance;
                    }
                }
            }
        }
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel label = new WLabel(TextHelper.literal("Detector Station"));
        panel.add(label, 0, 0);
        
        String statusText = stationNearby ? "Station at " + stationDistance + "m" : "No station";
        WLabel statusLabel = new WLabel(TextHelper.literal(statusText));
        panel.add(statusLabel, 0, 12);
        
        panel.setSize(120, 30);
    }

    public boolean isStationNearby() {
        return stationNearby;
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        super.writeToNbt(nbt);
        nbt.putBoolean("StationNearby", stationNearby);
        nbt.putInt("StationDistance", stationDistance);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        stationNearby = nbt.getBoolean("StationNearby");
        stationDistance = nbt.getInt("StationDistance");
    }
}