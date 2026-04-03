package vswe.stevescarts.module.farming;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.block.BlockState;
import net.minecraft.block.LogBlock;
import net.minecraft.block.LeavesBlock;
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
 * Galgadorian Wood Cutter Module - Advanced timber harvesting
 * Cuts down larger trees with leaves clearing
 */
public class GalgadorianWoodCutterModule extends CartModule implements Configurable, Worker {
    private static final int RANGE = 4;  // 4 block radius
    private static final int HEIGHT = 8;  // Check up to 8 blocks high
    private int treesCut = 0;
    private int workTicks = 0;

    public GalgadorianWoodCutterModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        if (getEntity() == null) return;
        
        workTicks++;
        if (workTicks >= 30) {  // Work every 1.5 seconds
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
                    // Cut logs
                    if (state.getBlock() instanceof LogBlock) {
                        getEntity().world.breakBlock(checkPos, true);
                        treesCut++;
                    }
                    // Clear leaves
                    else if (state.getBlock() instanceof LeavesBlock) {
                        getEntity().world.breakBlock(checkPos, true);
                    }
                }
            }
        }
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel label = new WLabel(TextHelper.literal("Galgadorian Wood Cutter"));
        panel.add(label, 0, 0);
        WLabel countLabel = new WLabel(TextHelper.literal("Trees Cut: " + treesCut));
        panel.add(countLabel, 0, 12);
        panel.setSize(160, 30);
    }

    @Override
    public int getPriority() {
        return Worker.HIGHER_PRIORITY;
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