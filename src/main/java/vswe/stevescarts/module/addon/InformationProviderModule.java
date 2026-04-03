package vswe.stevescarts.module.addon;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.Configurable;
import vswe.stevescarts.module.ModuleType;
import vswe.stevescarts.screen.CartHandler;
import vswe.stevescarts.util.TextHelper;

/**
 * Information Provider Module - Displays cart information
 * Shows detailed information about the cart's status and modules
 */
public class InformationProviderModule extends CartModule implements Configurable {
    private int displayMode = 0;  // 0=position, 1=velocity, 2=modules

    public InformationProviderModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        // Update information display
        if (getEntity() != null) {
            // Could broadcast information to nearby players
        }
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel title = new WLabel(TextHelper.literal("Information Provider"));
        panel.add(title, 0, 0);
        
        if (getEntity() != null) {
            String info = switch(displayMode) {
                case 0 -> String.format("Position: %.1f, %.1f, %.1f", 
                    getEntity().getX(), getEntity().getY(), getEntity().getZ());
                case 1 -> String.format("Velocity: %.2f m/s", 
                    getEntity().getVelocity().length());
                case 2 -> "Module Count: " + (getEntity().getModules() != null ? getEntity().getModules().size() : 0);
                default -> "Ready";
            };
            WLabel infoLabel = new WLabel(TextHelper.literal(info));
            panel.add(infoLabel, 0, 12);
        }
        
        panel.setSize(140, 30);
    }

    public void nextDisplayMode() {
        displayMode = (displayMode + 1) % 3;
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        super.writeToNbt(nbt);
        nbt.putInt("DisplayMode", displayMode);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        displayMode = nbt.getInt("DisplayMode");
    }
}