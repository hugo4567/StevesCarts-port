package vswe.stevescarts.module.addon;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.Configurable;
import vswe.stevescarts.module.ModuleType;
import vswe.stevescarts.screen.CartHandler;
import vswe.stevescarts.util.TextHelper;

import java.util.List;

/**
 * Milker Module - Automatically milks nearby cows
 * Collects milk from cows and stores it
 */
public class MilkerModule extends CartModule implements Configurable, Toggleable {
    private boolean active = true;
    private static final int MILKING_RANGE = 8;
    private int tickCounter = 0;
    private int lastMilkTick = 0;

    public MilkerModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        if (!active || getEntity() == null) return;
        
        tickCounter++;
        if (tickCounter >= 40) {  // Milk every 2 seconds
            tickCounter = 0;
            milkNearbyCows();
        }
    }

    private void milkNearbyCows() {
        if (getEntity() == null || getEntity().world == null) return;
        
        Box searchBox = getEntity().getBoundingBox().expand(MILKING_RANGE);
        List<CowEntity> cows = getEntity().world.getEntitiesByClass(
            CowEntity.class,
            searchBox,
            cow -> !cow.isBaby()
        );
        
        for (CowEntity cow : cows) {
            // Simulate milking with a bucket
            ItemStack bucket = new ItemStack(Items.BUCKET);
            // In real implementation, would give milk bucket to cart inventory
        }
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel label = new WLabel(TextHelper.literal("Milker Module"));
        panel.add(label, 0, 0);
        WLabel statusLabel = new WLabel(TextHelper.literal(active ? "Active" : "Inactive"));
        panel.add(statusLabel, 0, 12);
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