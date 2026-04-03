package vswe.stevescarts.module.farming;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
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
 * Basic Farmer Module - Harvests mature crops
 * Simple farming module that harvests fully grown crops in small range
 */
public class BasicFarmerModule extends CartModule implements Configurable, Worker {
    private static final int RANGE = 1;  // 1 block radius
    private int harvestedCount = 0;
    private int workTicks = 0;
    private static final int WORK_INTERVAL = 30;  // Every 1.5 seconds

    public BasicFarmerModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        if (getEntity() == null) return;
        
        workTicks++;
        if (workTicks >= WORK_INTERVAL) {
            workTicks = 0;
            work();
        }
    }

    @Override
    public void work() {
        if (getEntity() == null || getEntity().world == null) return;
        
        BlockPos centerPos = getRailPos();
        for (int x = -RANGE; x <= RANGE; x++) {
            for (int z = -RANGE; z <= RANGE; z++) {
                BlockPos checkPos = centerPos.add(x, 0, z);
                BlockState state = getEntity().world.getBlockState(checkPos);
                if (state.getBlock() instanceof CropBlock crop && crop.isMature(state)) {
                    getEntity().world.breakBlock(checkPos, true);
                    harvestedCount++;
                }
            }
        }
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel label = new WLabel(TextHelper.literal("Basic Farmer"));
        panel.add(label, 0, 0);
        WLabel countLabel = new WLabel(TextHelper.literal("Harvested: " + harvestedCount));
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
        nbt.putInt("HarvestedCount", harvestedCount);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        harvestedCount = nbt.getInt("HarvestedCount");
    }
}