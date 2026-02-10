package vswe.stevescarts.module.realtimer;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.ModuleType;

import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.nbt.NbtCompound;

/**
 * Experience Module - Collects XP orbs as the cart passes
 * 
 * This module automatically collects experience orbs within a 3-block radius,
 * storing up to 1500 experience points. The collected XP can be extracted
 * by right-clicking the cart.
 */
public class ExperienceModule extends CartModule {
	private static final int MAX_EXPERIENCE = 1500;
	private int experienceAmount = 0;

	public ExperienceModule(CartEntity minecart, ModuleType<?> type) {
		super(minecart, type);
	}

	@Override
	public void tick() {
		if (getEntity() == null || getEntity().world == null || getEntity().world.isClient) {
			return;
		}

		// Get all XP orbs within 3 blocks
		var entities = getEntity().world.getEntitiesByClass(
			ExperienceOrbEntity.class,
			getEntity().getBoundingBox().expand(3.0, 1.0, 3.0),
			entity -> true
		);

		for (ExperienceOrbEntity orb : entities) {
			if (!orb.isRemoved()) {
				experienceAmount += orb.getExperienceAmount();
				
				// Cap at max
				if (experienceAmount > MAX_EXPERIENCE) {
					experienceAmount = MAX_EXPERIENCE;
				} else {
					// Only remove if we collected it
					orb.discard();
				}
			}
		}
	}

	public int getExperienceAmount() {
		return experienceAmount;
	}

	public void setExperienceAmount(int amount) {
		experienceAmount = Math.min(amount, MAX_EXPERIENCE);
	}

	public int getMaxExperience() {
		return MAX_EXPERIENCE;
	}

	@Override
	public void writeToNbt(NbtCompound nbt) {
		nbt.putInt("Experience", experienceAmount);
		super.writeToNbt(nbt);
	}

	@Override
	public void readFromNbt(NbtCompound nbt) {
		experienceAmount = nbt.getInt("Experience");
		super.readFromNbt(nbt);
	}
}
