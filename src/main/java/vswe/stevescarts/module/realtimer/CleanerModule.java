package vswe.stevescarts.module.realtimer;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.ModuleType;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import java.util.List;

/**
 * Cleaning Machine Module - Sucks in nearby items and arrows
 * 
 * This module attracts nearby items toward the cart (vacuum effect)
 * and picks up items and arrows that touch the cart.
 */
public class CleanerModule extends CartModule {

	public CleanerModule(CartEntity minecart, ModuleType<?> type) {
		super(minecart, type);
	}

	@Override
	public void tick() {
		if (getEntity() == null || getEntity().world == null || getEntity().world.isClient) {
			return;
		}

		if (getEntity().getModules().stream().anyMatch(m -> m.canPropel())) {
			suck();
		}
		clean();
	}

	/**
	 * Attracts nearby items toward the cart (vacuum effect)
	 */
	private void suck() {
		List<ItemEntity> items = getEntity().world.getEntitiesByClass(
			ItemEntity.class,
			getEntity().getBoundingBox().expand(3.0, 1.0, 3.0),
			entity -> true
		);

		for (ItemEntity item : items) {
			double difX = getEntity().getX() - item.getX();
			double difY = getEntity().getY() - item.getY();
			double difZ = getEntity().getZ() - item.getZ();

			if (Math.abs(difX) > 0.5) {
				item.addVelocity(1.0 / (difX * 2), 0, 0);
			}
			if (Math.abs(difY) > 0.5) {
				item.addVelocity(0, 1.0 / (difY * 2), 0);
			}
			if (Math.abs(difZ) > 0.5) {
				item.addVelocity(0, 0, 1.0 / (difZ * 2));
			}
		}
	}

	/**
	 * Picks up items and arrows that are close to the cart
	 */
	private void clean() {
		List<Entity> entities = getEntity().world.getOtherEntities(
			getEntity(),
			getEntity().getBoundingBox().expand(1.0, 0.5, 1.0)
		);

		for (Entity entity : entities) {
			if (entity instanceof ItemEntity itemEntity) {
				if (!itemEntity.isRemoved()) {
					// Play pickup sound
					getEntity().world.playSound(
						null, getEntity().getBlockPos(),
						SoundEvents.ENTITY_ITEM_PICKUP,
						SoundCategory.NEUTRAL,
						0.2f, ((getEntity().world.random.nextFloat() - getEntity().world.random.nextFloat()) * 0.7f + 1.0f) * 2.0f
					);
					itemEntity.discard();
				}
			} else if (entity instanceof ArrowEntity arrowEntity) {
				if (!arrowEntity.isRemoved()) {
					getEntity().world.playSound(
						null, getEntity().getBlockPos(),
						SoundEvents.ENTITY_ITEM_PICKUP,
						SoundCategory.NEUTRAL,
						0.2f, ((getEntity().world.random.nextFloat() - getEntity().world.random.nextFloat()) * 0.7f + 1.0f) * 2.0f
					);
					arrowEntity.discard();
				}
			}
		}
	}
}
