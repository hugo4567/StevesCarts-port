package vswe.stevescarts.module.manager;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WButton;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.Configurable;
import vswe.stevescarts.module.ModuleType;
import vswe.stevescarts.screen.CartHandler;
import vswe.stevescarts.util.TextHelper;

/**
 * Cargo Manager Module - Manages inventory distribution
 * Distributes items to and from connected chests and containers
 */
public class CargoManagerModule extends CartModule implements Configurable {
    private int distributionMode = 0;  // 0=auto, 1=front, 2=sides, 3=back
    private int lastManageTick = 0;
    private static final int MANAGE_INTERVAL = 10;
    private int itemsMoved = 0;

    public CargoManagerModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        if (getEntity() == null) return;
        
        lastManageTick++;
        if (lastManageTick >= MANAGE_INTERVAL) {
            lastManageTick = 0;
            manageInventory();
        }
    }

    private void manageInventory() {
        // This would normally handle inventory distribution
        // In a real implementation, would interact with adjacent containers
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel title = new WLabel(TextHelper.literal("Cargo Manager"));
        panel.add(title, 0, 0);
        
        String modeText = switch(distributionMode) {
            case 0 -> "Auto";
            case 1 -> "Front";
            case 2 -> "Sides";
            case 3 -> "Back";
            default -> "Unknown";
        };
        
        WLabel modeLabel = new WLabel(TextHelper.literal("Mode: " + modeText));
        panel.add(modeLabel, 0, 12);
        
        WLabel movedLabel = new WLabel(TextHelper.literal("Moved: " + itemsMoved));
        panel.add(movedLabel, 60, 12);
        
        panel.setSize(140, 30);
    }

    public void setDistributionMode(int mode) {
        distributionMode = mode % 4;
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        super.writeToNbt(nbt);
        nbt.putInt("DistributionMode", distributionMode);
        nbt.putInt("ItemsMoved", itemsMoved);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        distributionMode = nbt.getInt("DistributionMode");
        itemsMoved = nbt.getInt("ItemsMoved");
    }
}