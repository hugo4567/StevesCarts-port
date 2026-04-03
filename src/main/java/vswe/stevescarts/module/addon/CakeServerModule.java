package vswe.stevescarts.module.addon;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CakeBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.Configurable;
import vswe.stevescarts.module.ModuleType;
import vswe.stevescarts.screen.CartHandler;
import vswe.stevescarts.util.TextHelper;

/**
 * Cake Server Module - Places and harvests cakes
 * Can place cakes on farmland and harvest them when consumed
 */
public class CakeServerModule extends CartModule implements Configurable, Toggleable {
    private boolean active = true;
    private static final int RANGE = 5;
    private int tickCounter = 0;

    public CakeServerModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        if (!active || getEntity() == null) return;
        
        tickCounter++;
        if (tickCounter >= 20) {
            tickCounter = 0;
            manageCakes();
        }
    }

    private void manageCakes() {
        if (getEntity() == null || getEntity().world == null) return;
        
        BlockPos railPos = getRailPos();
        
        // Check for cakes nearby that are fully consumed and remove them
        for (int x = -RANGE; x <= RANGE; x++) {
            for (int z = -RANGE; z <= RANGE; z++) {
                BlockPos checkPos = railPos.add(x, 0, z);
                BlockState state = getEntity().world.getBlockState(checkPos);
                if (state.getBlock() instanceof CakeBlock) {
                    if (state.get(CakeBlock.BITES) >= 6) {
                        getEntity().world.breakBlock(checkPos, false);
                    }
                }
            }
        }
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel label = new WLabel(TextHelper.literal("Cake Server Module"));
        panel.add(label, 0, 0);
        panel.setSize(100, 30);
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        super.writeToNbt(nbt);
        nbt.putBoolean("Active", active);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        active = nbt.getBoolean("Active");
    }
}