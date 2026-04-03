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
 * Galgadorian Drill Module - Advanced drilling with larger radius
 * Drills through blocks in a wider area
 */
public class GalgadorianDrillModule extends CartModule implements Configurable, Worker {
    private int blocksDestroyed = 0;
    private static final int DRILL_RANGE = 2;  // 2 blocks radius
    private static final int DRILL_HEIGHT = 3;  // 3 blocks high
    private int workTicks = 0;
    private static final int DRILL_INTERVAL = 8;

    public GalgadorianDrillModule(CartEntity minecart, ModuleType<?> type) {
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
        if (!checkMovement()) return;
        
        BlockPos railPos = getRailPos();
        
        for (int x = -DRILL_RANGE; x <= DRILL_RANGE; x++) {
            for (int height = 0; height <= DRILL_HEIGHT; height++) {
                for (int range = 1; range <= DRILL_RANGE; range++) {
                    BlockPos drillPos = railPos.add(x, height, range);
                    BlockState state = getEntity().world.getBlockState(drillPos);
                    
                    if (!state.getHardness(getEntity().world, drillPos).equals(-1.0F)) {
                        getEntity().world.breakBlock(drillPos, true);
                        blocksDestroyed++;
                    }
                }
            }
        }
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel title = new WLabel(TextHelper.literal("Galgadorian Drill"));
        panel.add(title, 0, 0);
        WLabel countLabel = new WLabel(TextHelper.literal("Blocks: " + blocksDestroyed));
        panel.add(countLabel, 0, 12);
        panel.setSize(140, 30);
    }

    @Override
    public int getPriority() {
        return Worker.HIGHER_PRIORITY;
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