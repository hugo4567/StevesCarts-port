package vswe.stevescarts.module.weapon;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
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
 * Projectile Cake Module - Launches cake as projectiles
 * Fun projectile weapon using cake
 */
public class ProjectileCakeModule extends CartModule implements Configurable, Worker {
    private int cakesFired = 0;
    private int cakeCount = 0;
    private static final int MAX_CAKES = 32;
    private int fireTicks = 0;
    private static final int FIRE_INTERVAL = 30;

    public ProjectileCakeModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        if (getEntity() == null) return;
        
        fireTicks++;
        if (fireTicks >= FIRE_INTERVAL && cakeCount > 0) {
            fireTicks = 0;
            work();
        }
    }

    @Override
    public void work() {
        // Fire cake projectile
        if (cakeCount > 0) {
            cakesFired++;
            cakeCount--;
        }
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel title = new WLabel(TextHelper.literal("Projectile Cake"));
        panel.add(title, 0, 0);
        WLabel firedLabel = new WLabel(TextHelper.literal("Fired: " + cakesFired));
        panel.add(firedLabel, 0, 12);
        WLabel cakesLabel = new WLabel(TextHelper.literal("Cake: " + cakeCount + "/" + MAX_CAKES));
        panel.add(cakesLabel, 100, 12);
        panel.setSize(160, 30);
    }

    public void addCakes(int amount) {
        cakeCount = Math.min(cakeCount + amount, MAX_CAKES);
    }

    @Override
    public int getPriority() {
        return Worker.NORMAL_PRIORITY;
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        super.writeToNbt(nbt);
        nbt.putInt("CakesFired", cakesFired);
        nbt.putInt("CakeCount", cakeCount);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        cakesFired = nbt.getInt("CakesFired");
        cakeCount = nbt.getInt("CakeCount");
    }
}