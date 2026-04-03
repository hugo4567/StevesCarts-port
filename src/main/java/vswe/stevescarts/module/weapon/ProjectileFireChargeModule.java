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
 * Projectile Fire Charge Module - Launches fire charges
 * Shoots fireballs at enemies with limited ammo
 */
public class ProjectileFireChargeModule extends CartModule implements Configurable, Worker {
    private int chargesFired = 0;
    private int chargeCount = 0;
    private static final int MAX_CHARGES = 48;
    private int fireTicks = 0;
    private static final int FIRE_INTERVAL = 20;

    public ProjectileFireChargeModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        if (getEntity() == null) return;
        
        fireTicks++;
        if (fireTicks >= FIRE_INTERVAL && chargeCount > 0) {
            fireTicks = 0;
            work();
        }
    }

    @Override
    public void work() {
        // Fire charge projectile
        if (chargeCount > 0) {
            chargesFired++;
            chargeCount--;
        }
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel title = new WLabel(TextHelper.literal("Fire Charge Launcher"));
        panel.add(title, 0, 0);
        WLabel firedLabel = new WLabel(TextHelper.literal("Fired: " + chargesFired));
        panel.add(firedLabel, 0, 12);
        WLabel chargesLabel = new WLabel(TextHelper.literal("Charges: " + chargeCount + "/" + MAX_CHARGES));
        panel.add(chargesLabel, 110, 12);
        panel.setSize(180, 30);
    }

    public void addCharges(int amount) {
        chargeCount = Math.min(chargeCount + amount, MAX_CHARGES);
    }

    @Override
    public int getPriority() {
        return Worker.NORMAL_PRIORITY;
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        super.writeToNbt(nbt);
        nbt.putInt("ChargesFired", chargesFired);
        nbt.putInt("ChargeCount", chargeCount);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        chargesFired = nbt.getInt("ChargesFired");
        chargeCount = nbt.getInt("ChargeCount");
    }
}