package vswe.stevescarts.module.processor;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.Configurable;
import vswe.stevescarts.module.ModuleType;
import vswe.stevescarts.module.Worker;
import vswe.stevescarts.screen.CartHandler;
import vswe.stevescarts.util.TextHelper;

/**
 * Cleaning Machine Module - Cleans contaminated items
 * Removes unwanted NBT data and enchantments from items
 */
public class CleaningMachineModule extends CartModule implements Configurable, Worker {
    private int itemsCleaned = 0;
    private boolean removeEnchantments = false;
    private int workTicks = 0;
    private static final int CLEAN_INTERVAL = 35;

    public CleaningMachineModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        if (getEntity() == null) return;
        
        workTicks++;
        if (workTicks >= CLEAN_INTERVAL) {
            workTicks = 0;
            work();
        }
    }

    @Override
    public void work() {
        // Clean items in cart inventory
        // Remove NBT data, enchantments if configured
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel title = new WLabel(TextHelper.literal("Cleaning Machine"));
        panel.add(title, 0, 0);
        WLabel countLabel = new WLabel(TextHelper.literal("Cleaned: " + itemsCleaned));
        panel.add(countLabel, 0, 12);
        WLabel optionLabel = new WLabel(TextHelper.literal(removeEnchantments ? "Remove All" : "NBT Only"));
        panel.add(optionLabel, 80, 12);
        panel.setSize(160, 30);
    }

    public void setRemoveEnchantments(boolean remove) {
        removeEnchantments = remove;
    }

    @Override
    public int getPriority() {
        return Worker.NORMAL_PRIORITY;
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        super.writeToNbt(nbt);
        nbt.putInt("ItemsCleaned", itemsCleaned);
        nbt.putBoolean("RemoveEnchantments", removeEnchantments);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        itemsCleaned = nbt.getInt("ItemsCleaned");
        removeEnchantments = nbt.getBoolean("RemoveEnchantments");
    }
}