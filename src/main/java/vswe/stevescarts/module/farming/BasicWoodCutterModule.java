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
 * Basic Wood Cutter Module - Harvests wood logs
 * Cuts down wood trees and collects logs
 */
public class BasicWoodCutterModule extends CartModule implements Configurable, Worker {
    private static final int RANGE = 3;  // 3 block radius
    private static final int HEIGHT = 5;  // Check up to 5 blocks high
    private int treesCut = 0;
    private int workTicks = 0;

    public BasicWoodCutterModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        if (getEntity() == null) return;
        
        workTicks++;
        if (workTicks >= 40) {  // Work every 2 seconds
            workTicks = 0;
            work();
        }
    }

    @Override
    public void work() {
        if (getEntity() == null || getEntity().world == null) return;
        
        BlockPos centerPos = getRailPos();
        for (int x = -RANGE; x <= RANGE; x++) {
            for (int y = 0; y <= HEIGHT; y++) {
                for (int z = -RANGE; z <= RANGE; z++) {
                    BlockPos checkPos = centerPos.add(x, y, z);
                    BlockState state = getEntity().world.getBlockState(checkPos);
                    // Check if block is a log (using name pattern)
                    if (state.getBlock().getName().getString().contains("log")) {
                        getEntity().world.breakBlock(checkPos, true);
                        treesCut++;
                    }
                }
            }
        }
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel label = new WLabel(TextHelper.literal("Basic Wood Cutter"));
        panel.add(label, 0, 0);
        WLabel countLabel = new WLabel(TextHelper.literal("Trees Cut: " + treesCut));
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
        nbt.putInt("TreesCut", treesCut);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        treesCut = nbt.getInt("TreesCut");
    }
}