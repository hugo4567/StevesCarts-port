package vswe.stevescarts.module.farming;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.block.BlockState;
import net.minecraft.block.FarmlandBlock;
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
 * Hydrator Module - Keeps farmland moist
 * Automatically hydrates farmland within range
 */
public class HydratorModule extends CartModule implements Configurable, Worker {
    private static final int RANGE = 4;  // 4 block radius
    private static final int HORIZONTAL_RANGE = 6;  // 6 blocks ahead
    private int hydratedCount = 0;
    private int workTicks = 0;

    public HydratorModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        if (getEntity() == null) return;
        
        workTicks++;
        if (workTicks >= 25) {  // Work every 1.25 seconds
            workTicks = 0;
            work();
        }
    }

    @Override
    public void work() {
        if (getEntity() == null || getEntity().world == null) return;
        
        BlockPos centerPos = getRailPos();
        
        // Hydrate farmland
        for (int x = -RANGE; x <= RANGE; x++) {
            for (int z = -HORIZONTAL_RANGE; z <= HORIZONTAL_RANGE; z++) {
                BlockPos checkPos = centerPos.add(x, -1, z);
                BlockState state = getEntity().world.getBlockState(checkPos);
                if (state.getBlock() instanceof FarmlandBlock) {
                    int moisture = state.get(FarmlandBlock.MOISTURE);
                    if (moisture < 7) {
                        getEntity().world.setBlockState(
                            checkPos,
                            state.with(FarmlandBlock.MOISTURE, 7),
                            net.minecraft.block.Block.NOTIFY_ALL
                        );
                        hydratedCount++;
                    }
                }
            }
        }
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel label = new WLabel(TextHelper.literal("Hydrator Module"));
        panel.add(label, 0, 0);
        WLabel countLabel = new WLabel(TextHelper.literal("Hydrated: " + hydratedCount));
        panel.add(countLabel, 0, 12);
        panel.setSize(140, 30);
    }

    @Override
    public int getPriority() {
        return Worker.NORMAL_PRIORITY;
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        super.writeToNbt(nbt);
        nbt.putInt("HydratedCount", hydratedCount);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        hydratedCount = nbt.getInt("HydratedCount");
    }
}