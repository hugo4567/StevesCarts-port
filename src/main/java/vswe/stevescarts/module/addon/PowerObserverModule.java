package vswe.stevescarts.module.addon;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.ModuleType;

import net.minecraft.nbt.NbtCompound;

/**
 * Power Observer Module - Monitors engine power levels
 *
 * This addon monitors the power levels of engines attached to the cart.
 * It provides 4 configurable areas that can be assigned to specific engines.
 * Each area has a power threshold level (in kJ) that triggers redstone output.
 *
 * Useful for creating automated fuel management systems — the observer
 * can signal when engines are running low on power.
 */
public class PowerObserverModule extends CartModule {
	private short[] areaData;
	private short[] powerLevel;

	public PowerObserverModule(CartEntity minecart, ModuleType<?> type) {
		super(minecart, type);
		areaData = new short[4];
		powerLevel = new short[4];
		// Default thresholds
		for (int i = 0; i < 4; i++) {
			powerLevel[i] = 100;
		}
	}

	/**
	 * Checks if a particular area is active (has engines assigned).
	 */
	public boolean isAreaActive(int area) {
		return area >= 0 && area < 4 && areaData[area] != 0;
	}

	/**
	 * Gets the engine bitmask for an area.
	 */
	public short getAreaData(int area) {
		return area >= 0 && area < 4 ? areaData[area] : 0;
	}

	/**
	 * Assigns an engine to an area.
	 */
	public void addEngineToArea(int area, int engineIndex) {
		if (area >= 0 && area < 4) {
			areaData[area] |= (short) (1 << engineIndex);
		}
	}

	/**
	 * Removes an engine from an area.
	 */
	public void removeEngineFromArea(int area, int engineIndex) {
		if (area >= 0 && area < 4) {
			areaData[area] &= (short) ~(1 << engineIndex);
		}
	}

	/**
	 * Gets the power threshold for an area.
	 */
	public short getPowerLevel(int area) {
		return area >= 0 && area < 4 ? powerLevel[area] : 0;
	}

	/**
	 * Sets the power threshold for an area.
	 */
	public void setPowerLevel(int area, short level) {
		if (area >= 0 && area < 4) {
			powerLevel[area] = (short) Math.max(0, Math.min(10000, level));
		}
	}

	/**
	 * Adjusts the power level threshold by a delta.
	 */
	public void adjustPowerLevel(int area, int delta) {
		if (area >= 0 && area < 4) {
			setPowerLevel(area, (short) (powerLevel[area] + delta));
		}
	}

	@Override
	public void writeToNbt(NbtCompound nbt) {
		for (int i = 0; i < 4; i++) {
			nbt.putShort("AreaData" + i, areaData[i]);
			nbt.putShort("PowerLevel" + i, powerLevel[i]);
		}
		super.writeToNbt(nbt);
	}

	@Override
	public void readFromNbt(NbtCompound nbt) {
		for (int i = 0; i < 4; i++) {
			areaData[i] = nbt.getShort("AreaData" + i);
			powerLevel[i] = nbt.getShort("PowerLevel" + i);
		}
		super.readFromNbt(nbt);
	}
}
