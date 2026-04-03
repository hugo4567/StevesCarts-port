package vswe.stevescarts.module.tool;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.block.BlockState;
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
 * Basic Drill Module - Breaks blocks ahead of the cart
 * Drills through stone and ore blocks
 */
public class BasicDrillModule extends CartModule implements Configurable, Worker {
    private int blocksDestroyed = 0;
    private static final int DRILL_RANGE = 1;  // 1 block in front
    private static final int DRILL_HEIGHT = 2;  // 2 blocks high
    private int workTicks = 0;
    private static final int DRILL_INTERVAL = 10;

    public BasicDrillModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        if (getEntity() == null) return;
        
        workTicks++;
        if (workTicks >= DRILL_INTERVAL) {
            workTicks = 0;
            work();
        }
    }

    @Override
    public void work() {
        if (getEntity() == null || getEntity().world == null) return;
        if (!checkMovement()) return;  // Only drill when moving
        
        BlockPos railPos = getRailPos();
        
        for (int range = 1; range <= DRILL_RANGE; range++) {
            for (int height = 0; height <= DRILL_HEIGHT; height++) {
                BlockPos drillPos = railPos.add(0, height, range);
                BlockState state = getEntity().world.getBlockState(drillPos);
                
                // Check if block can be drilled
                if (state.getHardness(getEntity().world, drillPos) >= 0) {  // Not bedrock
                    getEntity().world.breakBlock(drillPos, true);
                    blocksDestroyed++;
                }
            }
        }
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel title = new WLabel(TextHelper.literal("Basic Drill"));
        panel.add(title, 0, 0);
        WLabel countLabel = new WLabel(TextHelper.literal("Blocks: " + blocksDestroyed));
        panel.add(countLabel, 0, 12);
        panel.setSize(120, 30);
    }

    @Override
    public int getPriority() {
        return Worker.NORMAL_PRIORITY;
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        super.writeToNbt(nbt);
        nbt.putInt("BlocksDestroyed", blocksDestroyed);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        blocksDestroyed = nbt.getInt("BlocksDestroyed");
    }
}