package vswe.stevescarts.module.storage;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.Configurable;
import vswe.stevescarts.module.ModuleType;
import vswe.stevescarts.screen.CartHandler;
import vswe.stevescarts.util.TextHelper;

/**
 * Internal Storage Module - Built-in item storage
 * Provides 18 slots of internal storage in the cart
 */
public class InternalStorageModule extends CartModule implements Configurable {
    private static final int STORAGE_CAPACITY = 18;
    private ItemStack[] storageItems = new ItemStack[STORAGE_CAPACITY];
    private int filledSlots = 0;
    private int maxStackSize = 64;

    public InternalStorageModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
        for (int i = 0; i < STORAGE_CAPACITY; i++) {
            storageItems[i] = ItemStack.EMPTY;
        }
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel title = new WLabel(TextHelper.literal("Internal Storage"));
        panel.add(title, 0, 0);
        
        int totalItems = 0;
        for (ItemStack stack : storageItems) {
            if (!stack.isEmpty()) {
                totalItems += stack.getCount();
            }
        }
        
        WLabel itemsLabel = new WLabel(TextHelper.literal("Items: " + totalItems));
        panel.add(itemsLabel, 0, 12);
        WLabel slotsLabel = new WLabel(TextHelper.literal("Slots: " + filledSlots + "/" + STORAGE_CAPACITY));
        panel.add(slotsLabel, 80, 12);
        
        panel.setSize(160, 30);
    }

    public boolean addItem(ItemStack stack) {
        if (stack.isEmpty()) return false;
        
        for (int i = 0; i < STORAGE_CAPACITY; i++) {
            if (storageItems[i].isEmpty()) {
                storageItems[i] = stack.copy();
                filledSlots++;
                return true;
            }
        }
        return false;
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        super.writeToNbt(nbt);
        NbtList itemList = new NbtList();
        for (int i = 0; i < STORAGE_CAPACITY; i++) {
            if (!storageItems[i].isEmpty()) {
                NbtCompound itemNbt = new NbtCompound();
                storageItems[i].writeNbt(itemNbt);
                itemNbt.putInt("Slot", i);
                itemList.add(itemNbt);
            }
        }
        nbt.put("Items", itemList);
        nbt.putInt("FilledSlots", filledSlots);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        NbtList itemList = nbt.getList("Items", 10);
        for (int i = 0; i < itemList.size(); i++) {
            NbtCompound itemNbt = itemList.getCompound(i);
            int slot = itemNbt.getInt("Slot");
            if (slot >= 0 && slot < STORAGE_CAPACITY) {
                storageItems[slot] = ItemStack.fromNbt(itemNbt);
            }
        }
        filledSlots = nbt.getInt("FilledSlots");
    }
}