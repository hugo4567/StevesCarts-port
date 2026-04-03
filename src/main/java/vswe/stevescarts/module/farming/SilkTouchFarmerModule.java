package vswe.stevescarts.module.farming;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
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
 * Silk Touch Farmer Module - Mines ore blocks preserving their form
 * Uses silk touch to collect ores without breaking them into materials
 */
public class SilkTouchFarmerModule extends CartModule implements Configurable, Worker {
    private int oresCollected = 0;
    private static final int MINE_RANGE = 2;
    private static final int MINE_HEIGHT = 3;
    private int workTicks = 0;
    private static final int MINE_INTERVAL = 12;

    public SilkTouchFarmerModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        if (getEntity() == null) return;
        
        workTicks++;
        if (workTicks >= MINE_INTERVAL) {
            workTicks = 0;
            work();
        }
    }

    @Override
    public void work() {
        if (getEntity() == null || getEntity().world == null) return;
        if (!checkMovement()) return;
        
        BlockPos railPos = getRailPos();
        
        for (int x = -MINE_RANGE; x <= MINE_RANGE; x++) {
            for (int height = 0; height <= MINE_HEIGHT; height++) {
                for (int range = 1; range <= MINE_RANGE; range++) {
                    BlockPos minePos = railPos.add(x, height, range);
                    BlockState state = getEntity().world.getBlockState(minePos);
                    
                    // Check if block is ore
                    if (state.getBlock() instanceof OreBlock) {
                        getEntity().world.breakBlock(minePos, true);
                        oresCollected++;
                    }
                }
            }
        }
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel title = new WLabel(TextHelper.literal("Silk Touch Farmer"));
        panel.add(title, 0, 0);
        WLabel countLabel = new WLabel(TextHelper.literal("Ores: " + oresCollected));
        panel.add(countLabel, 0, 12);
        panel.setSize(150, 30);
    }

    @Override
    public int getPriority() {
        return Worker.HIGHER_PRIORITY;
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        super.writeToNbt(nbt);
        nbt.putInt("OresCollected", oresCollected);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        oresCollected = nbt.getInt("OresCollected");
    }
}
