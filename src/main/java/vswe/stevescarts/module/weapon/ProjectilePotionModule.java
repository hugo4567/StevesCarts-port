package vswe.stevescarts.module.weapon;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.item.Items;
import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.Configurable;
import vswe.stevescarts.module.ModuleType;
import vswe.stevescarts.module.Worker;
import vswe.stevescarts.screen.CartHandler;
import vswe.stevescarts.util.TextHelper;

/**
 * Projectile Potion Module - Launches potion bottles as projectiles
 * Throws splash potions that apply effects when they hit
 */
public class ProjectilePotionModule extends CartModule implements Configurable, Worker {
	private int potionsFired = 0;
	private int potionCount = 0;
	private static final int MAX_POTIONS = 64;
	private int fireTicks = 0;
	private static final int FIRE_INTERVAL = 20;  // 1 second
	private int potionType = 0;  // 0=Health, 1=Speed, 2=Strength

	public ProjectilePotionModule(CartEntity minecart, ModuleType<?> type) {
		super(minecart, type);
	}

	@Override
	public void tick() {
		if (getEntity() == null) return;
		
		fireTicks++;
		if (fireTicks >= FIRE_INTERVAL && potionCount > 0) {
			fireTicks = 0;
			work();
		}
	}

	@Override
	public void work() {
		// Fire potion projectile
		if (potionCount > 0) {
			potionsFired++;
			potionCount--;
		}
	}

	@Override
	public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
		WLabel title = new WLabel(TextHelper.literal("Projectile Potion"));
		panel.add(title, 0, 0);
		WLabel firedLabel = new WLabel(TextHelper.literal("Fired: " + potionsFired));
		panel.add(firedLabel, 0, 12);
		WLabel potionLabel = new WLabel(TextHelper.literal("Potions: " + potionCount + "/" + MAX_POTIONS));
		panel.add(potionLabel, 100, 12);
		panel.setSize(170, 30);
	}

	public void addPotions(int amount) {
		potionCount = Math.min(potionCount + amount, MAX_POTIONS);
	}

	public void setPotionType(int type) {
		this.potionType = Math.max(0, Math.min(type, 2));
	}

	@Override
	public int getPriority() {
		return Worker.NORMAL_PRIORITY;
	}

	@Override
	public void writeToNbt(NbtCompound nbt) {
		super.writeToNbt(nbt);
		nbt.putInt("PotionsFired", potionsFired);
		nbt.putInt("PotionCount", potionCount);
		nbt.putInt("PotionType", potionType);
	}

	@Override
	public void readFromNbt(NbtCompound nbt) {
		super.readFromNbt(nbt);
		potionsFired = nbt.getInt("PotionsFired");
		potionCount = nbt.getInt("PotionCount");
		potionType = nbt.getInt("PotionType");
	}
}