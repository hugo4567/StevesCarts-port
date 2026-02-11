package vswe.stevescarts.module.realtimer;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.ModuleType;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.GiantEntity;
import net.minecraft.entity.mob.MagmaCubeEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.entity.mob.CaveSpiderEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

import java.util.Comparator;
import java.util.List;

/**
 * Cage Module - Captures and releases living entities
 *
 * This module can automatically or manually capture nearby living entities
 * and mount them on the cart. Certain dangerous or special entities are
 * excluded from capture (players, bosses, golems, large mobs, etc).
 *
 * Features:
 * - Auto-pickup mode (every 20 ticks)
 * - Manual pickup/drop via controls
 * - Entity blacklist for balance
 * - Entities sorted by distance (closest first)
 */
public class CageModule extends CartModule {
	private int cooldown = 0;
	private boolean autoPickup = true;
	private static final int PICKUP_COOLDOWN = 20;
	private static final int SEARCH_DISTANCE = 2;

	public CageModule(CartEntity minecart, ModuleType<?> type) {
		super(minecart, type);
	}

	@Override
	public void tick() {
		if (getEntity() == null || getEntity().world == null || getEntity().world.isClient) {
			return;
		}

		if (cooldown > 0) {
			--cooldown;
			return;
		}

		if (autoPickup && !hasPassenger()) {
			pickUpCreature(SEARCH_DISTANCE);
			cooldown = PICKUP_COOLDOWN;
		}
	}

	/**
	 * Searches for and picks up the nearest valid entity.
	 */
	public void pickUpCreature(int searchDistance) {
		if (getEntity() == null || hasPassenger()) {
			return;
		}

		List<LivingEntity> entities = getEntity().world.getEntitiesByClass(
			LivingEntity.class,
			getEntity().getBoundingBox().expand(searchDistance, searchDistance, searchDistance),
			this::isValidTarget
		);

		// Sort by distance (closest first)
		entities.sort(Comparator.comparingDouble(e -> e.squaredDistanceTo(getEntity())));

		if (!entities.isEmpty()) {
			LivingEntity target = entities.get(0);
			target.startRiding(getEntity(), true);
			cooldown = PICKUP_COOLDOWN;
		}
	}

	/**
	 * Drops the currently carried entity.
	 */
	public void manualDrop() {
		if (getEntity() != null) {
			getEntity().removeAllPassengers();
			cooldown = PICKUP_COOLDOWN;
		}
	}

	/**
	 * Checks if the cart already has a passenger.
	 */
	private boolean hasPassenger() {
		return getEntity() != null && !getEntity().getPassengerList().isEmpty();
	}

	/**
	 * Determines whether an entity can be captured by the cage.
	 * Certain entities are blacklisted for balance/gameplay reasons.
	 */
	private boolean isValidTarget(LivingEntity entity) {
		if (entity instanceof PlayerEntity) return false;
		if (entity instanceof IronGolemEntity) return false;
		if (entity instanceof EnderDragonEntity) return false;
		if (entity instanceof WitherEntity) return false;
		if (entity instanceof EndermanEntity) return false;
		if (entity instanceof GiantEntity) return false;
		if (entity instanceof SlimeEntity && !(entity instanceof MagmaCubeEntity)) return false;
		// Spiders excluded except cave spiders
		if (entity instanceof SpiderEntity && !(entity instanceof CaveSpiderEntity)) return false;
		// Don't capture entities already riding something
		if (entity.hasVehicle()) return false;
		return true;
	}

	public boolean isAutoPickup() {
		return autoPickup;
	}

	public void setAutoPickup(boolean autoPickup) {
		this.autoPickup = autoPickup;
	}

	@Override
	public void writeToNbt(NbtCompound nbt) {
		nbt.putBoolean("AutoPickup", autoPickup);
		super.writeToNbt(nbt);
	}

	@Override
	public void readFromNbt(NbtCompound nbt) {
		if (nbt.contains("AutoPickup")) {
			autoPickup = nbt.getBoolean("AutoPickup");
		}
		super.readFromNbt(nbt);
	}
}
