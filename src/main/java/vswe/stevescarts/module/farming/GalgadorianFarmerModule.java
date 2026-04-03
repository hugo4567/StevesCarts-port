package vswe.stevescarts.module.farming;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
 * Galgadorian Farmer Module - Advanced farming with larger range
 * Harvests crops and maintains farmland automatically
 */
public class GalgadorianFarmerModule extends CartModule implements Configurable, Worker {
    private static final int RANGE = 3;  // 3 block radius
    private static final int HORIZONTAL_RANGE = 8;  // 8 blocks ahead
    private int harvestedCount = 0;
    private int tiledCount = 0;
    private int workTicks = 0;

    public GalgadorianFarmerModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        if (getEntity() == null) return;
        
        workTicks++;
        if (workTicks >= 20) {  // Work every second
            workTicks = 0;
            work();
        }
    }

    @Override
    public void work() {
        if (getEntity() == null || getEntity().world == null) return;
        
        BlockPos centerPos = getRailPos();
        
        // Harvest crops
        for (int x = -RANGE; x <= RANGE; x++) {
            for (int z = -HORIZONTAL_RANGE; z <= HORIZONTAL_RANGE; z++) {
                BlockPos checkPos = centerPos.add(x, 0, z);
                BlockState state = getEntity().world.getBlockState(checkPos);
                if (state.getBlock() instanceof CropBlock crop && crop.isMature(state)) {
                    getEntity().world.breakBlock(checkPos, true);
                    harvestedCount++;
                }
            }
        }
        
        // Maintain farmland moisture
        for (int x = -RANGE; x <= RANGE; x++) {
            for (int z = -HORIZONTAL_RANGE; z <= HORIZONTAL_RANGE; z++) {
                BlockPos checkPos = centerPos.add(x, -1, z);
                BlockState state = getEntity().world.getBlockState(checkPos);
                if (state.getBlock() instanceof FarmlandBlock) {
                    int moisture = state.get(FarmlandBlock.MOISTURE);
                    if (moisture < 7) {  // Keep farmland moist
                        getEntity().world.setBlockState(
                            checkPos, 
                            state.with(FarmlandBlock.MOISTURE, 7),
                            net.minecraft.block.Block.NOTIFY_ALL
                        );
                        tiledCount++;
                    }
                }
            }
        }
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel label = new WLabel(TextHelper.literal("Galgadorian Farmer"));
        panel.add(label, 0, 0);
        WLabel countLabel = new WLabel(TextHelper.literal("Harvested: " + harvestedCount));
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
        nbt.putInt("HarvestedCount", harvestedCount);
        nbt.putInt("TiledCount", tiledCount);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        harvestedCount = nbt.getInt("HarvestedCount");
        tiledCount = nbt.getInt("TiledCount");
    }
}