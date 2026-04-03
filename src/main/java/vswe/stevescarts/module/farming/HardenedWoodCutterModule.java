package vswe.stevescarts.module.farming;

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
 * Hardened Wood Cutter Module - Heavy-duty timber harvesting
 * Fastest wood cutting with maximum efficiency
 */
public class HardenedWoodCutterModule extends CartModule implements Configurable, Worker {
    private static final int RANGE = 5;  // 5 block radius
    private static final int HEIGHT = 10;  // Check up to 10 blocks high
    private int treesCut = 0;
    private int workTicks = 0;

    public HardenedWoodCutterModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        if (getEntity() == null) return;
        
        workTicks++;
        if (workTicks >= 15) {  // Work more frequently (every 0.75 seconds)
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
                    if (state.getBlock().getName().getString().contains("log")) {
                        getEntity().world.breakBlock(checkPos, true);
                        treesCut++;
                    } else if (state.getBlock().getName().getString().contains("leaves")) {
                        getEntity().world.breakBlock(checkPos, false);
                    }
                }
            }
        }
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel label = new WLabel(TextHelper.literal("Hardened Wood Cutter"));
        panel.add(label, 0, 0);
        WLabel countLabel = new WLabel(TextHelper.literal("Trees Cut: " + treesCut));
        panel.add(countLabel, 0, 12);
        panel.setSize(160, 30);
    }

    @Override
    public int getPriority() {
        return Worker.HIGHEST_PRIORITY;
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