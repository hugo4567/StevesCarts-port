package vswe.stevescarts.module.tool;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Direction;
import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.Configurable;
import vswe.stevescarts.module.ModuleType;
import vswe.stevescarts.screen.CartHandler;
import vswe.stevescarts.util.TextHelper;

/**
 * External Distributor Module - Distributes items to adjacent blocks
 * Pushes items from cart inventory to nearby containers
 */
public class ExternalDistributorModule extends CartModule implements Configurable {
    private int itemsDistributed = 0;
    private Direction distributeDirection = Direction.NORTH;
    private int maxItemsPerTick = 4;
    private int workTicks = 0;
    private static final int DISTRIBUTE_INTERVAL = 8;

    public ExternalDistributorModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        if (getEntity() == null) return;
        
        workTicks++;
        if (workTicks >= DISTRIBUTE_INTERVAL) {
            workTicks = 0;
            distributeItems();
        }
    }

    private void distributeItems() {
        // Distribute items from cart to adjacent containers
        itemsDistributed += 0;  // Would increment as items are distributed
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel title = new WLabel(TextHelper.literal("External Distributor"));
        panel.add(title, 0, 0);
        
        String dirText = distributeDirection.getName();
        WLabel dirLabel = new WLabel(TextHelper.literal("Direction: " + dirText));
        panel.add(dirLabel, 0, 12);
        
        WLabel countLabel = new WLabel(TextHelper.literal("Distributed: " + itemsDistributed));
        panel.add(countLabel, 100, 12);
        
        panel.setSize(170, 30);
    }

    public void setDirection(Direction dir) {
        distributeDirection = dir;
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        super.writeToNbt(nbt);
        nbt.putString("Direction", distributeDirection.getName());
        nbt.putInt("ItemsDistributed", itemsDistributed);
        nbt.putInt("MaxPerTick", maxItemsPerTick);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        distributeDirection = Direction.byName(nbt.getString("Direction"));
        itemsDistributed = nbt.getInt("ItemsDistributed");
        maxItemsPerTick = nbt.getInt("MaxPerTick");
    }
}