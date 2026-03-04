package vswe.stevescarts.module.addon;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.ModuleType;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.nbt.NbtCompound;

/**
 * Divine Shield Module - Protects the cart from all damage
 *
 * When active, this addon blocks all incoming damage to the cart entity.
 * The shield consumes significant power (20x base consumption) while active.
 * Features a deploy/retract animation via shieldDistance field.
 *
 * Can be toggled on/off manually or via activator rails.
 */
public class DivineShieldModule extends CartModule {
	private boolean shieldActive = false;
	private float shieldDistance = 0.0f;
	private float shieldAngle = 0.0f;

	private static final float MAX_SHIELD_DISTANCE = 18.0f;
	private static final float DEPLOY_SPEED = 0.25f;
	private static final float ROTATE_SPEED = 0.05f;

	public DivineShieldModule(CartEntity minecart, ModuleType<?> type) {
		super(minecart, type);
	}

	@Override
	public void tick() {
		if (getEntity() == null || getEntity().world == null) {
			return;
		}

		// Animate shield deployment/retraction
		if (shieldActive) {
			if (shieldDistance < MAX_SHIELD_DISTANCE) {
				shieldDistance += DEPLOY_SPEED;
			}
			shieldAngle += ROTATE_SPEED;
		} else {
			if (shieldDistance > 0) {
				shieldDistance -= DEPLOY_SPEED;
				if (shieldDistance < 0) shieldDistance = 0;
			}
		}

		// Server-side fuel check
		if (!getEntity().world.isClient && shieldActive) {
			if (!getEntity().getModules().stream().anyMatch(CartModule::canPropel)) {
				setShieldActive(false);
			}
		}
	}

	/**
	 * Called when the cart takes damage. Returns true if damage should be blocked.
	 */
	public boolean shouldBlockDamage(DamageSource source, float amount) {
		return shieldActive;
	}

	public void setShieldActive(boolean active) {
		this.shieldActive = active;
	}

	public boolean isShieldActive() {
		return shieldActive;
	}

	public float getShieldDistance() {
		return shieldDistance;
	}

	public float getShieldAngle() {
		return shieldAngle;
	}

	@Override
	public void onActivate() {
		setShieldActive(!shieldActive);
	}

	@Override
	public void writeToNbt(NbtCompound nbt) {
		nbt.putBoolean("Shield", shieldActive);
		super.writeToNbt(nbt);
	}

	@Override
	public void readFromNbt(NbtCompound nbt) {
		shieldActive = nbt.getBoolean("Shield");
		super.readFromNbt(nbt);
	}
}
