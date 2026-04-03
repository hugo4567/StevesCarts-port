package vswe.stevescarts.module.tool;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.block.BlockState;
import net.minecraft.block.TallPlantBlock;
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
 * Lawn Mower Module - Cuts grass and tall plants
 * Flattens grass and removes tall vegetation
 */
public class LawnMowerModule extends CartModule implements Configurable, Worker {
    private int grassCut = 0;
    private static final int MOWER_RANGE = 3;  // 3 blocks on each side
    private static final int MOWER_HEIGHT = 3;  // 3 blocks high for tall plants
    private int workTicks = 0;
    private static final int MOW_INTERVAL = 6;

    public LawnMowerModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        if (getEntity() == null) return;
        
        workTicks++;
        if (workTicks >= MOW_INTERVAL) {
            workTicks = 0;
            work();
        }
    }

    @Override
    public void work() {
        if (getEntity() == null || getEntity().world == null) return;
        if (!checkMovement()) return;
        
        BlockPos railPos = getRailPos();
        
        for (int x = -MOWER_RANGE; x <= MOWER_RANGE; x++) {
            for (int z = 0; z <= MOWER_RANGE; z++) {
                for (int height = 0; height <= MOWER_HEIGHT; height++) {
                    BlockPos checkPos = railPos.add(x, height, z);
                    BlockState state = getEntity().world.getBlockState(checkPos);
                    
                    // Check for tall plants
                    if (state.getBlock() instanceof TallPlantBlock) {
                        getEntity().world.breakBlock(checkPos, true);
                        grassCut++;
                    }
                }
            }
        }
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel title = new WLabel(TextHelper.literal("Lawn Mower"));
        panel.add(title, 0, 0);
        WLabel countLabel = new WLabel(TextHelper.literal("Cut: " + grassCut));
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
        nbt.putInt("GrassCut", grassCut);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        grassCut = nbt.getInt("GrassCut");
    }
}