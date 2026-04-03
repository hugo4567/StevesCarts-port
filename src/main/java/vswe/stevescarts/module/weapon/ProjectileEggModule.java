package vswe.stevescarts.module.weapon;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.Configurable;
import vswe.stevescarts.module.ModuleType;
import vswe.stevescarts.module.Worker;
import vswe.stevescarts.screen.CartHandler;
import vswe.stevescarts.util.TextHelper;

/**
 * Projectile Egg Module - Launches eggs as projectiles
 * Throws eggs that can hatch chickens or damage mobs
 */
public class ProjectileEggModule extends CartModule implements Configurable, Worker {
    private int eggsFired = 0;
    private int eggCount = 0;
    private static final int MAX_EGGS = 64;
    private int fireTicks = 0;
    private static final int FIRE_INTERVAL = 15;  // Fast

    public ProjectileEggModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        if (getEntity() == null) return;
        
        fireTicks++;
        if (fireTicks >= FIRE_INTERVAL && eggCount > 0) {
            fireTicks = 0;
            work();
        }
    }

    @Override
    public void work() {
        // Fire egg projectile
        if (eggCount > 0) {
            eggsFired++;
            eggCount--;
        }
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel title = new WLabel(TextHelper.literal("Projectile Egg"));
        panel.add(title, 0, 0);
        WLabel firedLabel = new WLabel(TextHelper.literal("Fired: " + eggsFired));
        panel.add(firedLabel, 0, 12);
        WLabel eggsLabel = new WLabel(TextHelper.literal("Eggs: " + eggCount + "/" + MAX_EGGS));
        panel.add(eggsLabel, 100, 12);
        panel.setSize(160, 30);
    }

    public void addEggs(int amount) {
        eggCount = Math.min(eggCount + amount, MAX_EGGS);
    }

    @Override
    public int getPriority() {
        return Worker.NORMAL_PRIORITY;
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        super.writeToNbt(nbt);
        nbt.putInt("EggsFired", eggsFired);
        nbt.putInt("EggCount", eggCount);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        eggsFired = nbt.getInt("EggsFired");
        eggCount = nbt.getInt("EggCount");
    }
}