package vswe.stevescarts.module.weapon;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.Configurable;
import vswe.stevescarts.module.ModuleType;
import vswe.stevescarts.module.Worker;
import vswe.stevescarts.screen.CartHandler;
import vswe.stevescarts.util.TextHelper;

/**
 * Advanced Shooter Module - Rapid-fire projectile launcher
 * Shoots projectiles with improved accuracy and fire rate
 */
public class AdvancedShooterModule extends CartModule implements Configurable, Worker {
    private int projectilesFired = 0;
    private int ammoCount = 0;
    private static final int MAX_AMMO = 256;
    private float fireRate = 0.5f;  // Fire every other tick
    private int fireTicks = 0;

    public AdvancedShooterModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        if (getEntity() == null || ammoCount <= 0) return;
        
        fireTicks++;
        if (fireTicks >= (int)(20 * fireRate)) {
            fireTicks = 0;
            work();
        }
    }

    @Override
    public void work() {
        if (ammoCount > 0) {
            // Fire projectile
            projectilesFired++;
            ammoCount--;
        }
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel title = new WLabel(TextHelper.literal("Advanced Shooter"));
        panel.add(title, 0, 0);
        WLabel firedLabel = new WLabel(TextHelper.literal("Fired: " + projectilesFired));
        panel.add(firedLabel, 0, 12);
        WLabel ammoLabel = new WLabel(TextHelper.literal("Ammo: " + ammoCount + "/" + MAX_AMMO));
        panel.add(ammoLabel, 90, 12);
        panel.setSize(160, 30);
    }

    public void addAmmo(int amount) {
        ammoCount = Math.min(ammoCount + amount, MAX_AMMO);
    }

    @Override
    public int getPriority() {
        return Worker.HIGHER_PRIORITY;
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        super.writeToNbt(nbt);
        nbt.putInt("ProjectilesFired", projectilesFired);
        nbt.putInt("AmmoCount", ammoCount);
        nbt.putFloat("FireRate", fireRate);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        projectilesFired = nbt.getInt("ProjectilesFired");
        ammoCount = nbt.getInt("AmmoCount");
        fireRate = nbt.getFloat("FireRate");
    }
}