package vswe.stevescarts.module.tool;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.block.BlockState;
import net.minecraft.block.RailBlock;
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
 * Track Remover Module - Removes rails behind the cart
 * Automatically removes track as the cart passes
 */
public class TrackRemoverModule extends CartModule implements Configurable, Worker {
    private int tracksRemoved = 0;
    private static final int REMOVE_RANGE = 1;
    private static final int REMOVE_HEIGHT = 1;
    private int workTicks = 0;
    private static final int REMOVE_INTERVAL = 15;

    public TrackRemoverModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        if (getEntity() == null) return;
        
        workTicks++;
        if (workTicks >= REMOVE_INTERVAL) {
            workTicks = 0;
            work();
        }
    }

    @Override
    public void work() {
        if (getEntity() == null || getEntity().world == null) return;
        if (!checkMovement()) return;
        
        BlockPos railPos = getRailPos();
        
        // Check behind and below the cart
        for (int x = -REMOVE_RANGE; x <= REMOVE_RANGE; x++) {
            for (int z = -2; z <= -1; z++) {  // Behind the cart
                for (int y = -REMOVE_HEIGHT; y <= REMOVE_HEIGHT; y++) {
                    BlockPos checkPos = railPos.add(x, y, z);
                    BlockState state = getEntity().world.getBlockState(checkPos);
                    
                    if (state.getBlock() instanceof RailBlock) {
                        getEntity().world.breakBlock(checkPos, true);
                        tracksRemoved++;
                    }
                }
            }
        }
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel title = new WLabel(TextHelper.literal("Track Remover"));
        panel.add(title, 0, 0);
        WLabel countLabel = new WLabel(TextHelper.literal("Removed: " + tracksRemoved));
        panel.add(countLabel, 0, 12);
        panel.setSize(140, 30);
    }

    @Override
    public int getPriority() {
        return Worker.LOW_PRIORITY;
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        super.writeToNbt(nbt);
        nbt.putInt("TracksRemoved", tracksRemoved);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        tracksRemoved = nbt.getInt("TracksRemoved");
    }
}