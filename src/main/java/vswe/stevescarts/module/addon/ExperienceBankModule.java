package vswe.stevescarts.module.addon;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.Configurable;
import vswe.stevescarts.module.ModuleType;
import vswe.stevescarts.screen.CartHandler;
import vswe.stevescarts.util.TextHelper;

/**
 * Experience Bank Module - Stores experience points
 * Absorbs experience orbs and stores XP for later use
 */
public class ExperienceBankModule extends CartModule implements Configurable, Toggleable {
    private boolean active = true;
    private int storedExperience = 0;
    private static final int MAX_EXPERIENCE = 100000;  // Max XP storage
    private int tickCounter = 0;

    public ExperienceBankModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        if (!active || getEntity() == null) return;
        
        tickCounter++;
        if (tickCounter >= 10) {
            tickCounter = 0;
            // Collect nearby experience
            if (getEntity().world != null) {
                var box = getEntity().getBoundingBox().expand(8);
                var xpOrbs = getEntity().world.getEntitiesByClass(
                    net.minecraft.entity.ExperienceOrbEntity.class,
                    box,
                    e -> true
                );
                for (var orb : xpOrbs) {
                    int xpValue = orb.getExperienceAmount();
                    if (storedExperience + xpValue <= MAX_EXPERIENCE) {
                        storedExperience += xpValue;
                        orb.discard();
                    }
                }
            }
        }
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel label = new WLabel(TextHelper.literal("Experience Bank"));
        panel.add(label, 0, 0);
        
        WLabel xpLabel = new WLabel(TextHelper.literal("XP: " + storedExperience + "/" + MAX_EXPERIENCE));
        panel.add(xpLabel, 0, 12);
        
        panel.setSize(120, 30);
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getStoredExperience() {
        return storedExperience;
    }

    public void addExperience(int amount) {
        storedExperience = Math.min(storedExperience + amount, MAX_EXPERIENCE);
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        super.writeToNbt(nbt);
        nbt.putBoolean("Active", active);
        nbt.putInt("StoredXP", storedExperience);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        active = nbt.getBoolean("Active");
        storedExperience = nbt.getInt("StoredXP");
    }
}