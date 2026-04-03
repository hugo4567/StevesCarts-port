package vswe.stevescarts.module.detector;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.entity.passive.VillagerEntity;
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
 * Entity Detector - Villager Module - Detects nearby villagers
 * Triggers when villagers are within detection range
 */
public class EntityDetectorVillagerModule extends CartModule implements Configurable {
    private int villagerCount = 0;
    private static final int DETECTION_RANGE = 16;
    private int lastDetectionTick = 0;

    public EntityDetectorVillagerModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        if (getEntity() == null || getEntity().world == null) return;
        
        lastDetectionTick++;
        if (lastDetectionTick >= 20) {
            lastDetectionTick = 0;
            detectVillagers();
        }
    }

    private void detectVillagers() {
        villagerCount = 0;
        
        Box searchBox = getEntity().getBoundingBox().expand(DETECTION_RANGE);
        List<VillagerEntity> villagers = getEntity().world.getEntitiesByClass(
            VillagerEntity.class,
            searchBox,
            villager -> true
        );
        
        villagerCount = villagers.size();
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel label = new WLabel(TextHelper.literal("Entity Detector - Villager"));
        panel.add(label, 0, 0);
        
        WLabel countLabel = new WLabel(TextHelper.literal("Villagers: " + villagerCount));
        panel.add(countLabel, 0, 12);
        
        panel.setSize(140, 30);
    }

    public int getVillagerCount() {
        return villagerCount;
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        super.writeToNbt(nbt);
        nbt.putInt("VillagerCount", villagerCount);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        villagerCount = nbt.getInt("VillagerCount");
    }
}