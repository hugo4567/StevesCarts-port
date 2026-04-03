package vswe.stevescarts.module.addon;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Box;
import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.Configurable;
import vswe.stevescarts.module.ModuleType;
import vswe.stevescarts.screen.CartHandler;
import vswe.stevescarts.util.TextHelper;

import java.util.List;

/**
 * Cage Module - Captures nearby mobs
 * Creates a cage effect that prevents mobs from escaping
 */
public class CageModule extends CartModule implements Configurable, Toggleable {
    private boolean active = true;
    private static final int CAGE_RANGE = 8;
    private int tickCounter = 0;

    public CageModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        if (!active || getEntity() == null) return;
        
        tickCounter++;
        if (tickCounter >= 10) {
            tickCounter = 0;
            containNearbyMobs();
        }
    }

    private void containNearbyMobs() {
        if (getEntity() == null || getEntity().world == null) return;
        
        Box searchBox = getEntity().getBoundingBox().expand(CAGE_RANGE);
        List<Entity> entities = getEntity().world.getOtherEntities(getEntity(), searchBox);
        
        for (Entity entity : entities) {
            if (entity instanceof net.minecraft.entity.mob.MobEntity mob) {
                // Prevent mob from moving too far away
                if (entity.getPos().distanceTo(getEntity().getPos()) > CAGE_RANGE) {
                    entity.setVelocity(0, 0, 0);
                }
            }
        }
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel label = new WLabel(TextHelper.literal("Cage Module"));
        panel.add(label, 0, 0);
        panel.setSize(80, 30);
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