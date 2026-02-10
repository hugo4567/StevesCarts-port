package vswe.stevescarts.module.attachment;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.ModuleType;

import net.minecraft.block.Block;
import net.minecraft.block.RailBlock;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

/**
 * Track Remover Module - Destroys rails as the cart passes over them
 * 
 * This module automatically removes rail blocks from the track,
 * allowing the cart to destroy its own path as it moves forward.
 * Useful for creating temporary tracks or harvesting rails.
 */
public class TrackRemoverModule extends CartModule {
	private boolean enabled = true;

	public TrackRemoverModule(CartEntity minecart, ModuleType<?> type) {
		super(minecart, type);
	}

	@Override
	public void tick() {
		if (!enabled || getEntity() == null || getEntity().world == null) {
			return;
		}

		// Check if we have power to remove rails
		if (!getEntity().getModules().stream().anyMatch(m -> m.canPropel())) {
			return;
		}

		// Get the cart's position
		BlockPos cartPos = getEntity().getBlockPos();

		// Check and remove rails at different heights relative to cart
		removeRailAt(cartPos.down());  // Below cart
		removeRailAt(cartPos);         // At cart level
		removeRailAt(cartPos.up());    // Above cart
	}

	private void removeRailAt(BlockPos pos) {
		if (getEntity() == null || getEntity().world == null) {
			return;
		}

		Block block = getEntity().world.getBlockState(pos).getBlock();
		
		// Check if the block is a rail block
		if (block instanceof RailBlock) {
			getEntity().world.breakBlock(pos, false);
		}
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void writeToNbt(NbtCompound nbt) {
		nbt.putBoolean("Enabled", enabled);
		super.writeToNbt(nbt);
	}

	@Override
	public void readFromNbt(NbtCompound nbt) {
		enabled = nbt.getBoolean("Enabled");
		super.readFromNbt(nbt);
	}
}
