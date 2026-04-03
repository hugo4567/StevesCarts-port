package vswe.stevescarts.module.weapon;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.item.Items;
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
 * Dynamite Carrier Module - Launches TNT explosives
 * Carries and launches TNT for area effect damage
 */
public class DynamiteCarrierModule extends CartModule implements Configurable, Worker {
    private int dynamiteFired = 0;
    private int dynamiteCount = 0;
    private static final int MAX_DYNAMITE = 64;
    private int fireTicks = 0;
    private static final int FIRE_INTERVAL = 25;  // Every 1.25 seconds

    public DynamiteCarrierModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        if (getEntity() == null) return;
        
        fireTicks++;
        if (fireTicks >= FIRE_INTERVAL && dynamiteCount > 0) {
            fireTicks = 0;
            work();
        }
    }

    @Override
    public void work() {
        // Launch TNT
        if (dynamiteCount > 0 && getEntity() != null && getEntity().world != null) {
            // Create TNT entity
            TntEntity tnt = new TntEntity(getEntity().world, 
                getEntity().getX(), getEntity().getY() + 1, getEntity().getZ(), null);
            getEntity().world.spawnEntity(tnt);
            dynamiteFired++;
            dynamiteCount--;
        }
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel title = new WLabel(TextHelper.literal("Dynamite Carrier"));
        panel.add(title, 0, 0);
        WLabel countLabel = new WLabel(TextHelper.literal("Fired: " + dynamiteFired));
        panel.add(countLabel, 0, 12);
        WLabel ammoLabel = new WLabel(TextHelper.literal("TNT: " + dynamiteCount + "/" + MAX_DYNAMITE));
        panel.add(ammoLabel, 100, 12);
        panel.setSize(160, 30);
    }

    public void addDynamite(int amount) {
        dynamiteCount = Math.min(dynamiteCount + amount, MAX_DYNAMITE);
    }

    @Override
    public int getPriority() {
        return Worker.NORMAL_PRIORITY;
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        super.writeToNbt(nbt);
        nbt.putInt("DynamiteFired", dynamiteFired);
        nbt.putInt("DynamiteCount", dynamiteCount);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        dynamiteFired = nbt.getInt("DynamiteFired");
        dynamiteCount = nbt.getInt("DynamiteCount");
    }
}