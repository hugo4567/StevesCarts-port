package vswe.stevescarts.module.realtimer;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.ModuleType;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

/**
 * Dynamite Carrier Module - Carries explosives that can be detonated
 *
 * This module allows the cart to carry dynamite and detonate it.
 * The fuse can be set to different lengths (1-150 ticks).
 * Once primed, the fuse counts down each tick until detonation.
 *
 * Explosion power scales with internal dynamite count:
 * base power 8, plus 2 per dynamite unit stored.
 * Actual explosion size = total power / 2.5.
 *
 * Can be triggered by powered rails or manually.
 */
public class DynamiteModule extends CartModule {
	private int fuse = 0;
	private int fuseLength = 75;
	private int explosionPower = 8;
	private boolean primed = false;

	private static final int MAX_FUSE_LENGTH = 150;

	public DynamiteModule(CartEntity minecart, ModuleType<?> type) {
		super(minecart, type);
	}

	@Override
	public void tick() {
		if (getEntity() == null || getEntity().world == null || getEntity().world.isClient) {
			return;
		}

		if (!primed) {
			return;
		}

		fuse++;
		if (fuse >= fuseLength) {
			explode();
		}
	}

	/**
	 * Primes the dynamite, starting the fuse countdown.
	 */
	public void prime() {
		if (!primed) {
			primed = true;
			fuse = 0;
		}
	}

	/**
	 * Triggers the explosion and destroys the cart.
	 */
	private void explode() {
		if (getEntity() == null || getEntity().world == null) {
			return;
		}

		World world = getEntity().world;
		float size = explosionPower / 2.5f;

		world.createExplosion(
			getEntity(),
			getEntity().getX(),
			getEntity().getY(),
			getEntity().getZ(),
			size,
			Explosion.DestructionType.BREAK
		);

		getEntity().discard();
	}

	/**
	 * Activated when the cart passes over a powered rail.
	 */
	@Override
	public void onActivate() {
		prime();
	}

	/**
	 * Sets the fuse length in ticks (clamped to 1-150).
	 */
	public void setFuseLength(int length) {
		this.fuseLength = Math.max(1, Math.min(MAX_FUSE_LENGTH, length));
	}

	/**
	 * Adds dynamite to increase explosion power.
	 * Each unit adds 2 to the base power of 8.
	 */
	public void addDynamite(int count) {
		this.explosionPower = 8 + (count * 2);
	}

	public int getFuse() {
		return fuse;
	}

	public int getFuseLength() {
		return fuseLength;
	}

	public int getExplosionPower() {
		return explosionPower;
	}

	public boolean isPrimed() {
		return primed;
	}

	public int getMaxFuseLength() {
		return MAX_FUSE_LENGTH;
	}

	@Override
	public void writeToNbt(NbtCompound nbt) {
		nbt.putInt("Fuse", fuse);
		nbt.putInt("FuseLength", fuseLength);
		nbt.putInt("ExplosionPower", explosionPower);
		nbt.putBoolean("Primed", primed);
		super.writeToNbt(nbt);
	}

	@Override
	public void readFromNbt(NbtCompound nbt) {
		fuse = nbt.getInt("Fuse");
		fuseLength = nbt.getInt("FuseLength");
		if (fuseLength == 0) fuseLength = 75;
		explosionPower = nbt.getInt("ExplosionPower");
		if (explosionPower == 0) explosionPower = 8;
		primed = nbt.getBoolean("Primed");
		super.readFromNbt(nbt);
	}
}
