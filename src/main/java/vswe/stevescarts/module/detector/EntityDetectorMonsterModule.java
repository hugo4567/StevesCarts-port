package vswe.stevescarts.module.detector;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.entity.mob.HostileEntity;
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
 * Entity Detector - Monster Module - Detects nearby hostile mobs
 * Triggers when hostile mobs are within detection range
 */
public class EntityDetectorMonsterModule extends CartModule implements Configurable {
    private int monsterCount = 0;
    private static final int DETECTION_RANGE = 16;
    private int lastDetectionTick = 0;

    public EntityDetectorMonsterModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        if (getEntity() == null || getEntity().world == null) return;
        
        lastDetectionTick++;
        if (lastDetectionTick >= 20) {
            lastDetectionTick = 0;
            detectMonsters();
        }
    }

    private void detectMonsters() {
        monsterCount = 0;
        
        Box searchBox = getEntity().getBoundingBox().expand(DETECTION_RANGE);
        List<HostileEntity> monsters = getEntity().world.getEntitiesByClass(
            HostileEntity.class,
            searchBox,
            mob -> true
        );
        
        monsterCount = monsters.size();
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel label = new WLabel(TextHelper.literal("Entity Detector - Monster"));
        panel.add(label, 0, 0);
        
        WLabel countLabel = new WLabel(TextHelper.literal("Monsters: " + monsterCount));
        panel.add(countLabel, 0, 12);
        
        WLabel warningLabel = new WLabel(TextHelper.literal(monsterCount > 0 ? "⚠ THREAT" : "Safe"));
        panel.add(warningLabel, 100, 12);
        
        panel.setSize(140, 30);
    }

    public int getMonsterCount() {
        return monsterCount;
    }

    public boolean isThreatDetected() {
        return monsterCount > 0;
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        super.writeToNbt(nbt);
        nbt.putInt("MonsterCount", monsterCount);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        monsterCount = nbt.getInt("MonsterCount");
    }
}