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
 * Gift Storage Module - Stores special/rare items
 * Keeps valuables safe and organized
 */
public class GiftStorageModule extends CartModule implements Configurable {
    private static final int STORAGE_SLOTS = 9;  // 1 stack × 9
    private ItemStack[] inventory = new ItemStack[STORAGE_SLOTS];
    private int itemsStored = 0;

    public GiftStorageModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
        for (int i = 0; i < STORAGE_SLOTS; i++) {
            inventory[i] = ItemStack.EMPTY;
        }
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel title = new WLabel(TextHelper.literal("Gift Storage"));
        panel.add(title, 0, 0);
        WLabel capacityLabel = new WLabel(TextHelper.literal("Slots: " + itemsStored + "/" + STORAGE_SLOTS));
        panel.add(capacityLabel, 0, 12);
        panel.setSize(140, 30);
    }

    public void addItem(ItemStack stack) {
        for (int i = 0; i < STORAGE_SLOTS; i++) {
            if (inventory[i].isEmpty()) {
                inventory[i] = stack.copy();
                inventory[i].setCount(1);
                itemsStored++;
                return;
            }
        }
    }

    public ItemStack getItem(int slot) {
        if (slot >= 0 && slot < STORAGE_SLOTS) {
            return inventory[slot];
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        super.writeToNbt(nbt);
        NbtList itemList = new NbtList();
        for (int i = 0; i < STORAGE_SLOTS; i++) {
            if (!inventory[i].isEmpty()) {
                NbtCompound itemNbt = new NbtCompound();
                inventory[i].writeNbt(itemNbt);
                itemNbt.putInt("Slot", i);
                itemList.add(itemNbt);
            }
        }
        nbt.put("Items", itemList);
        nbt.putInt("ItemsStored", itemsStored);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        NbtList itemList = nbt.getList("Items", 10);
        for (int i = 0; i < itemList.size(); i++) {
            NbtCompound itemNbt = itemList.getCompound(i);
            int slot = itemNbt.getInt("Slot");
            if (slot >= 0 && slot < STORAGE_SLOTS) {
                inventory[slot] = ItemStack.fromNbt(itemNbt);
            }
        }
        itemsStored = nbt.getInt("ItemsStored");
    }
}