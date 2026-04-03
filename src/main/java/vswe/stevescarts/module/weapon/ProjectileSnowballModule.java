package vswe.stevescarts.module.weapon;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.nbt.NbtCompound;
import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.Configurable;
import vswe.stevescarts.module.ModuleType;
import vswe.stevescarts.module.Worker;
import vswe.stevescarts.screen.CartHandler;
import vswe.stevescarts.util.TextHelper;

/**
 * Projectile Snowball Module - Launches snowballs
 * Throws snowballs at enemies; damage and knockback effectiveness
 */
public class ProjectileSnowballModule extends CartModule implements Configurable, Worker {
    private int snowballsFired = 0;
    private int snowballCount = 0;
    private static final int MAX_SNOWBALLS = 96;
    private int fireTicks = 0;
    private static final int FIRE_INTERVAL = 12;  // Faster than eggs

    public ProjectileSnowballModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        if (getEntity() == null) return;
        
        fireTicks++;
        if (fireTicks >= FIRE_INTERVAL && snowballCount > 0) {
            fireTicks = 0;
            work();
        }
    }

    @Override
    public void work() {
        // Fire snowball projectile
        if (snowballCount > 0 && getEntity() != null && getEntity().world != null) {
            SnowballEntity snowball = new SnowballEntity(getEntity().world, 
                getEntity().getX(), getEntity().getY() + 1, getEntity().getZ());
            getEntity().world.spawnEntity(snowball);
            snowballsFired++;
            snowballCount--;
        }
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel title = new WLabel(TextHelper.literal("Projectile Snowball"));
        panel.add(title, 0, 0);
        WLabel firedLabel = new WLabel(TextHelper.literal("Fired: " + snowballsFired));
        panel.add(firedLabel, 0, 12);
        WLabel snowballsLabel = new WLabel(TextHelper.literal("Snowballs: " + snowballCount + "/" + MAX_SNOWBALLS));
        panel.add(snowballsLabel, 110, 12);
        panel.setSize(180, 30);
    }

    public void addSnowballs(int amount) {
        snowballCount = Math.min(snowballCount + amount, MAX_SNOWBALLS);
    }

    @Override
    public int getPriority() {
        return Worker.NORMAL_PRIORITY;
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        super.writeToNbt(nbt);
        nbt.putInt("SnowballsFired", snowballsFired);
        nbt.putInt("SnowballCount", snowballCount);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        snowballsFired = nbt.getInt("SnowballsFired");
        snowballCount = nbt.getInt("SnowballCount");
    }
}