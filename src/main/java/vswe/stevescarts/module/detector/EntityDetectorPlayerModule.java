package vswe.stevescarts.module.detector;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Box;
import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.Configurable;
import vswe.stevescarts.module.ModuleType;
import vswe.stevescarts.screen.CartHandler;
import vswe.stevescarts.util.TextHelper;

import java.util.List;

/**
 * Entity Detector - Player Module - Detects nearby players
 * Triggers when players are within detection range
 */
public class EntityDetectorPlayerModule extends CartModule implements Configurable {
    private int playerCount = 0;
    private static final int DETECTION_RANGE = 32;
    private String nearestPlayerName = "None";
    private int lastDetectionTick = 0;

    public EntityDetectorPlayerModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        if (getEntity() == null || getEntity().world == null) return;
        
        lastDetectionTick++;
        if (lastDetectionTick >= 20) {
            lastDetectionTick = 0;
            detectPlayers();
        }
    }

    private void detectPlayers() {
        playerCount = 0;
        nearestPlayerName = "None";
        
        Box searchBox = getEntity().getBoundingBox().expand(DETECTION_RANGE);
        List<PlayerEntity> players = getEntity().world.getEntitiesByClass(
            PlayerEntity.class,
            searchBox,
            player -> !player.isSpectator()
        );
        
        playerCount = players.size();
        if (!players.isEmpty()) {
            nearestPlayerName = players.get(0).getName().getString();
        }
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel label = new WLabel(TextHelper.literal("Entity Detector - Player"));
        panel.add(label, 0, 0);
        
        WLabel countLabel = new WLabel(TextHelper.literal("Players: " + playerCount));
        panel.add(countLabel, 0, 12);
        
        WLabel nearestLabel = new WLabel(TextHelper.literal("Nearest: " + nearestPlayerName));
        panel.add(nearestLabel, 60, 12);
        
        panel.setSize(160, 30);
    }

    public int getPlayerCount() {
        return playerCount;
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        super.writeToNbt(nbt);
        nbt.putInt("PlayerCount", playerCount);
        nbt.putString("NearestPlayer", nearestPlayerName);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        playerCount = nbt.getInt("PlayerCount");
        nearestPlayerName = nbt.getString("NearestPlayer");
    }
}