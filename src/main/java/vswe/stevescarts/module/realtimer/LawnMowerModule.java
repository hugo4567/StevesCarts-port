package vswe.stevescarts.module.realtimer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.ModuleType;

import java.util.List;

/**
 * Lawn Mower Module - Removes plants and grass from around the cart
 * Equivalent to ModuleFlowerRemover from v1.12.2
 */
public class LawnMowerModule extends CartModule {
	private int tick;
	private float bladeangle;
	private float bladespeed;

	public LawnMowerModule(CartEntity cart, ModuleType<?> type) {
		super(cart, type);
		bladespeed = 0.0f;
	}

	@Override
	public void tick() {
		super.tick();
		
		CartEntity cart = getEntity();
		if (cart == null) return;
		
		World world = cart.getWorld();
		if (world.isClient) {
			bladeangle += getBladeSpinSpeed();
			// Check if cart has power by seeing if any engine modules can propel
			boolean hasPower = cart.getModules().stream().anyMatch(m -> m.canPropel());
			if (hasPower) {
				bladespeed = Math.min(1.0f, bladespeed + 0.005f);
			} else {
				bladespeed = Math.max(0.0f, bladespeed - 0.005f);
			}
			return;
		}

		// Check if cart has power
		boolean hasPower = cart.getModules().stream().anyMatch(m -> m.canPropel());
		if (hasPower) {
			if (tick >= getInterval()) {
				tick = 0;
				mownTheLawn();
				shearEntities();
			} else {
				tick++;
			}
		}
	}

	/**
	 * Interval between lawn mowing operations (in ticks)
	 */
	protected int getInterval() {
		return 70;
	}

	/**
	 * Number of blocks to the side to mow
	 */
	protected int getBlocksOnSide() {
		return 7;
	}

	/**
	 * Number of blocks vertically from center level
	 */
	protected int getBlocksFromLevel() {
		return 1;
	}

	/**
	 * Remove plantable blocks around the cart
	 */
	private void mownTheLawn() {
		CartEntity cart = getEntity();
		if (cart == null) return;
		
		BlockPos cartPos = cart.getBlockPos();
		World world = cart.getWorld();

		for (int x = -getBlocksOnSide(); x <= getBlocksOnSide(); ++x) {
			for (int z = -getBlocksOnSide(); z <= getBlocksOnSide(); ++z) {
				for (int y = -getBlocksFromLevel(); y <= getBlocksFromLevel(); ++y) {
					BlockPos pos = cartPos.add(x, y, z);
					if (isPlant(pos, world)) {
						BlockState state = world.getBlockState(pos);
						Block block = state.getBlock();
						
						// Get drops and add to cart
						if (world instanceof net.minecraft.server.world.ServerWorld serverWorld) {
							List<ItemStack> drops = Block.getDroppedStacks(state, serverWorld, pos, null);
							addStuff(drops, cart, world);
						}
						
						// Destroy the block
						world.breakBlock(pos, false);
					}
				}
			}
		}
	}

	/**
	 * Shear wool from nearby entities
	 */
	private void shearEntities() {
		CartEntity cart = getEntity();
		if (cart == null) return;
		
		World world = cart.getWorld();
		BlockPos cartPos = cart.getBlockPos();
		
		// Get all living entities in range
		Box box = cart.getBoundingBox()
			.expand(getBlocksOnSide(), getBlocksFromLevel() + 2.0f, getBlocksOnSide());
		
		var entities = world.getOtherEntities(cart, box, e -> e instanceof LivingEntity);
		
		for (var entity : entities) {
			if (entity instanceof SheepEntity sheep) {
				if (sheep.isSheared()) continue;
				
				// Shear the sheep
				sheep.setSheared(true);
			}
		}
	}

	/**
	 * Check if a block is a plant/flower
	 */
	private boolean isPlant(BlockPos pos, World world) {
		if (!world.isChunkLoaded(pos)) return false;
		
		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		
		// Check if block is a plant (crops, flowers, tall grass, etc)
		return block instanceof net.minecraft.block.PlantBlock || 
		       block instanceof net.minecraft.block.TallPlantBlock ||
		       state.getMaterial().isReplaceable();
	}

	/**
	 * Add items to the cart's inventory or drop them
	 */
	private void addStuff(List<ItemStack> items, CartEntity cart, World world) {
		for (ItemStack stack : items) {
			if (stack.isEmpty()) continue;
			
			// Drop items near the cart
			ItemEntity itemEntity = new ItemEntity(world, 
				cart.getX(), cart.getY(), cart.getZ(), stack.copy());
			itemEntity.setVelocity(0.0, 0.15, 0.0);
			world.spawnEntity(itemEntity);
		}
	}

	/**
	 * Get current blade rotation angle (for rendering)
	 */
	public float getBladeAngle() {
		return bladeangle;
	}

	/**
	 * Get blade spin speed
	 */
	public float getBladeSpinSpeed() {
		return bladespeed;
	}
}
