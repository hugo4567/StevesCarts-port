package vswe.stevescarts.module.addon;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.ModuleType;

import net.minecraft.nbt.NbtCompound;

/**
 * Height Controller Module - Sets a target Y level for drill modules
 *
 * This addon allows the player to set a target altitude (Y coordinate).
 * Drill modules and drill intelligence modules read this value to
 * adjust mining depth. In 1.19, Y ranges from -64 to 320.
 *
 * GUI controls: up/down arrows to adjust target, middle button to set
 * to current cart position. Shift-click adjusts by 10 instead of 1.
 */
public class HeightControllerModule extends CartModule {
	private int yTarget = 64;
	private static final int MIN_Y = -64;
	private static final int MAX_Y = 320;

	public HeightControllerModule(CartEntity minecart, ModuleType<?> type) {
		super(minecart, type);
	}

	/**
	 * Gets the target Y altitude for drill operations.
	 */
	public int getYTarget() {
		return yTarget;
	}

	/**
	 * Sets the target Y altitude, clamped to valid world bounds.
	 */
	public void setYTarget(int target) {
		this.yTarget = Math.max(MIN_Y, Math.min(MAX_Y, target));
	}

	/**
	 * Adjusts the target by a delta amount.
	 * @param delta positive to go up, negative to go down
	 */
	public void adjustYTarget(int delta) {
		setYTarget(this.yTarget + delta);
	}

	/**
	 * Sets the Y target to the cart's current Y position.
	 */
	public void setToCurrentPosition() {
		if (getEntity() != null) {
			setYTarget((int) Math.floor(getEntity().getY()));
		}
	}

	@Override
	public void writeToNbt(NbtCompound nbt) {
		nbt.putInt("HeightTarget", yTarget);
		super.writeToNbt(nbt);
	}

	@Override
	public void readFromNbt(NbtCompound nbt) {
		yTarget = nbt.getInt("HeightTarget");
		if (yTarget == 0 && !nbt.contains("HeightTarget")) {
			yTarget = 64; // Default if not saved
		}
		super.readFromNbt(nbt);
	}
}
