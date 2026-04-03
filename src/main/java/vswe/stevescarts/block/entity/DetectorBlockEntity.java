package vswe.stevescarts.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DetectorBlockEntity extends BlockEntity {
    public static final Text NAME = Text.of("Detector Unit");
    
    public enum DetectorType {
        ANIMAL("Animal"), PLAYER("Player"), HOSTILE("Hostile"), VILLAGER("Villager"), ALL("All");
        
        public final String displayName;
        DetectorType(String displayName) { this.displayName = displayName; }
    }
    
    private DetectorType detectorType = DetectorType.ANIMAL;
    private int detectionRange = 16;
    private boolean powered = false;
    private int detectedCount = 0;

    public DetectorBlockEntity(BlockPos pos, BlockState state) {
        super(StevesCartsBlockEntities.DETECTOR, pos, state);
    }

    public static void clientTick(World world, BlockPos pos, BlockState state, DetectorBlockEntity entity) {
        // Client-side tick
    }

    public static void serverTick(World world, BlockPos pos, BlockState state, DetectorBlockEntity entity) {
        if (world == null || world.isClient) return;
        
        entity.detectedCount = countNearbyEntities(world, pos, entity.detectorType, entity.detectionRange);
        boolean newPowered = entity.detectedCount > 0;
        
        if (newPowered != entity.powered) {
            entity.powered = newPowered;
            world.updateListeners(pos, state, state, 2);
            entity.markDirty();
        }
    }

    private static int countNearbyEntities(World world, BlockPos pos, DetectorType type, int range) {
        int count = 0;
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        
        for (Entity entity : world.getOtherEntities(null, 
                new net.minecraft.util.math.Box(x - range, y - range, z - range, 
                                              x + range, y + range, z + range))) {
            if (matches(entity, type)) {
                count++;
            }
        }
        return count;
    }

    private static boolean matches(Entity entity, DetectorType type) {
        return switch (type) {
            case ANIMAL -> entity instanceof AnimalEntity;
            case PLAYER -> entity instanceof PlayerEntity && !((PlayerEntity) entity).isSpectator();
            case HOSTILE -> entity instanceof HostileEntity;
            case VILLAGER -> entity instanceof VillagerEntity;
            case ALL -> !(entity instanceof PlayerEntity);
        };
    }

    public DetectorType getDetectorType() { return detectorType; }
    public void setDetectorType(DetectorType type) { this.detectorType = type; markDirty(); }
    
    public int getDetectionRange() { return detectionRange; }
    public void setDetectionRange(int range) { this.detectionRange = range; markDirty(); }
    
    public boolean isPowered() { return powered; }
    public int getDetectedCount() { return detectedCount; }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt.contains("Type")) {
            try {
                detectorType = DetectorType.valueOf(nbt.getString("Type"));
            } catch (IllegalArgumentException e) {
                detectorType = DetectorType.ANIMAL;
            }
        }
        detectionRange = nbt.getInt("Range");
        if (detectionRange == 0) detectionRange = 16;
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putString("Type", detectorType.name());
        nbt.putInt("Range", detectionRange);
    }
}