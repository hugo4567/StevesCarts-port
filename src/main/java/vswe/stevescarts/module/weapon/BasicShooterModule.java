package vswe.stevescarts.module.weapon;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.Configurable;
import vswe.stevescarts.module.ModuleType;
import vswe.stevescarts.module.Worker;
import vswe.stevescarts.screen.CartHandler;
import vswe.stevescarts.util.TextHelper;

/**
 * Basic Shooter Module - Simple projectile launcher
 * Shoots projectiles at targets
 */
public class BasicShooterModule extends CartModule implements Configurable, Worker {
    private int projectilesFired = 0;
    private int ammoCount = 0;
    private static final int MAX_AMMO = 128;
    private int fireTicks = 0;
    private static final int FIRE_INTERVAL = 20;  // Fire every second

    public BasicShooterModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        if (getEntity() == null || ammoCount <= 0) return;
        
        fireTicks++;
        if (fireTicks >= FIRE_INTERVAL) {
            fireTicks = 0;
            work();
        }
    }

    @Override
    public void work() {
        // Fire projectile if we have ammo
        if (ammoCount > 0) {
            projectilesFired++;
            ammoCount--;
        }
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel title = new WLabel(TextHelper.literal("Basic Shooter"));
        panel.add(title, 0, 0);
        WLabel ammoLabel = new WLabel(TextHelper.literal("Ammo: " + ammoCount + "/" + MAX_AMMO));
        panel.add(ammoLabel, 0, 12);
        panel.setSize(140, 30);
    }

    public void addAmmo(int amount) {
        ammoCount = Math.min(ammoCount + amount, MAX_AMMO);
    }

    @Override
    public int getPriority() {
        return Worker.NORMAL_PRIORITY;
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        super.writeToNbt(nbt);
        nbt.putInt("ProjectilesFired", projectilesFired);
        nbt.putInt("AmmoCount", ammoCount);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        projectilesFired = nbt.getInt("ProjectilesFired");
        ammoCount = nbt.getInt("AmmoCount");
    }
}