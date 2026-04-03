package vswe.stevescarts.module.farming;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.Configurable;
import vswe.stevescarts.module.ModuleType;
import vswe.stevescarts.module.Worker;
import vswe.stevescarts.screen.CartHandler;
import vswe.stevescarts.util.TextHelper;

/**
 * Height Controller Module - Levels terrain by removing/placing blocks
 * Smooths uneven ground to make paths
 */
public class HeightControllerModule extends CartModule implements Configurable, Worker {
    private int blocksModified = 0;
    private static final int RANGE = 3;  // 3 blocks on each side
    private int targetHeight = 0;  // 0 = auto-detect
    private int workTicks = 0;
    private static final int INTERVAL = 15;

    public HeightControllerModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        if (getEntity() == null) return;
        
        workTicks++;
        if (workTicks >= INTERVAL) {
            workTicks = 0;
            work();
        }
    }

    @Override
    public void work() {
        if (getEntity() == null || getEntity().world == null) return;
        if (!checkMovement()) return;
        
        BlockPos railPos = getRailPos();
        
        // Detect average height at current position
        int sumHeight = 0;
        int count = 0;
        
        for (int x = -RANGE; x <= RANGE; x++) {
            for (int z = 0; z <= RANGE; z++) {
                BlockPos checkPos = railPos.add(x, 0, z);
                if (!getEntity().world.getBlockState(checkPos).isAir()) {
                    sumHeight += checkPos.getY();
                    count++;
                }
            }
        }
        
        if (count > 0) {
            int avgHeight = sumHeight / count;
            
            // Level blocks to average height
            for (int x = -RANGE; x <= RANGE; x++) {
                for (int z = 0; z <= RANGE; z++) {
                    BlockPos levelPos = railPos.add(x, 0, z).withY(avgHeight);
                    BlockState state = getEntity().world.getBlockState(levelPos);
                    
                    if (state.isAir()) {
                        getEntity().world.setBlockState(levelPos, Blocks.DIRT.getDefaultState(), 3);
                        blocksModified++;
                    }
                }
            }
        }
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel title = new WLabel(TextHelper.literal("Height Controller"));
        panel.add(title, 0, 0);
        WLabel countLabel = new WLabel(TextHelper.literal("Modified: " + blocksModified));
        panel.add(countLabel, 0, 12);
        panel.setSize(150, 30);
    }

    @Override
    public int getPriority() {
        return Worker.NORMAL_PRIORITY;
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        super.writeToNbt(nbt);
        nbt.putInt("BlocksModified", blocksModified);
        nbt.putInt("TargetHeight", targetHeight);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        blocksModified = nbt.getInt("BlocksModified");
        targetHeight = nbt.getInt("TargetHeight");
    }
}