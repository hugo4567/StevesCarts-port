package vswe.stevescarts.module.detector;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.entity.passive.AnimalEntity;
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
 * Entity Detector - Animal Module - Detects nearby animals
 * Triggers when animals are within detection range
 */
public class EntityDetectorAnimalModule extends CartModule implements Configurable {
    private int animalCount = 0;
    private static final int DETECTION_RANGE = 16;
    private int lastDetectionTick = 0;

    public EntityDetectorAnimalModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        if (getEntity() == null || getEntity().world == null) return;
        
        lastDetectionTick++;
        if (lastDetectionTick >= 20) {
            lastDetectionTick = 0;
            detectAnimals();
        }
    }

    private void detectAnimals() {
        animalCount = 0;
        
        Box searchBox = getEntity().getBoundingBox().expand(DETECTION_RANGE);
        List<AnimalEntity> animals = getEntity().world.getEntitiesByClass(
            AnimalEntity.class,
            searchBox,
            animal -> true
        );
        
        animalCount = animals.size();
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel label = new WLabel(TextHelper.literal("Entity Detector - Animal"));
        panel.add(label, 0, 0);
        
        WLabel countLabel = new WLabel(TextHelper.literal("Animals nearby: " + animalCount));
        panel.add(countLabel, 0, 12);
        
        panel.setSize(140, 30);
    }

    public int getAnimalCount() {
        return animalCount;
    }

    public boolean detectorsHaveAnimals() {
        return animalCount > 0;
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        super.writeToNbt(nbt);
        nbt.putInt("AnimalCount", animalCount);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        animalCount = nbt.getInt("AnimalCount");
    }
}