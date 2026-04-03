package vswe.stevescarts.module.addon;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Box;
import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.Configurable;
import vswe.stevescarts.module.ModuleType;
import vswe.stevescarts.screen.CartHandler;
import vswe.stevescarts.util.TextHelper;

/**
 * Trick-or-Treat Cake Server Module - Seasonal cake delivery
 * Special Halloween-themed cake server variant
 */
public class TrickOrTreatCakeServerModule extends CartModule implements Configurable, Toggleable {
    private boolean active = true;
    private int tickCounter = 0;
    private int treatCount = 0;

    public TrickOrTreatCakeServerModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        if (!active || getEntity() == null) return;
        
        tickCounter++;
        if (tickCounter >= 40) {
            tickCounter = 0;
            distributeTreats();
        }
    }

    private void distributeTreats() {
        if (getEntity() == null || getEntity().world == null) return;
        
        Box searchBox = getEntity().getBoundingBox().expand(16);
        var players = getEntity().world.getEntitiesByClass(
            PlayerEntity.class,
            searchBox,
            p -> true
        );
        
        for (PlayerEntity player : players) {
            // Distribute treats (could be items or buffs)
            treatCount++;
        }
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel title = new WLabel(TextHelper.literal("Trick-or-Treat Server"));
        panel.add(title, 0, 0);
        
        WLabel countLabel = new WLabel(TextHelper.literal("Treats: " + treatCount));
        panel.add(countLabel, 0, 12);
        
        panel.setSize(120, 30);
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
        nbt.putInt("TreatCount", treatCount);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        active = nbt.getBoolean("Active");
        treatCount = nbt.getInt("TreatCount");
    }
}