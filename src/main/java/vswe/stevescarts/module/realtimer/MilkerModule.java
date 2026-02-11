package vswe.stevescarts.module.realtimer;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.ModuleType;

import net.minecraft.entity.passive.CowEntity;
import net.minecraft.nbt.NbtCompound;

import java.util.List;

/**
 * Milker Module - Milks cows near the cart
 * 
 * Generates milk from nearby cows every 20 ticks.
 * Milk is stored in a buffer and can be deposited into tanks.
 */
public class MilkerModule extends CartModule {
	private int cooldown = 0;
	private int milkBuffer = 0;
	private static final int MAX_MILK = 1000;

	public MilkerModule(CartEntity minecart, ModuleType<?> type) {
		super(minecart, type);
	}

	@Override
	public void tick() {
		if (getEntity() == null || getEntity().world == null || getEntity().world.isClient) {
			return;
		}

		if (cooldown <= 0) {
			if (getEntity().getModules().stream().anyMatch(m -> m.canPropel())) {
				generateMilk();
			}
			cooldown = 20;
		} else {
			--cooldown;
		}
	}

	private void generateMilk() {
		if (milkBuffer >= MAX_MILK) {
			return;
		}

		// Check for cows nearby
		List<CowEntity> cows = getEntity().world.getEntitiesByClass(
			CowEntity.class,
			getEntity().getBoundingBox().expand(4.0, 2.0, 4.0),
			cow -> !cow.isBaby()
		);

		if (!cows.isEmpty()) {
			milkBuffer = Math.min(milkBuffer + 75, MAX_MILK);
		}
	}

	public int getMilkBuffer() {
		return milkBuffer;
	}

	public int getMaxMilk() {
		return MAX_MILK;
	}

	@Override
	public void writeToNbt(NbtCompound nbt) {
		nbt.putInt("MilkBuffer", milkBuffer);
		super.writeToNbt(nbt);
	}

	@Override
	public void readFromNbt(NbtCompound nbt) {
		milkBuffer = nbt.getInt("MilkBuffer");
		super.readFromNbt(nbt);
	}
}
