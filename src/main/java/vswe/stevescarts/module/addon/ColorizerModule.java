package vswe.stevescarts.module.addon;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.ModuleType;

import net.minecraft.nbt.NbtCompound;

/**
 * Colorizer Module - Changes the cart's color using RGB sliders
 *
 * This addon allows the player to set custom RGB color values (0-255)
 * for the cart's rendering. The color is stored as three byte values
 * and synced via NBT. Other modules and renderers can read the color
 * to apply custom tinting.
 */
public class ColorizerModule extends CartModule {
	private int red = 255;
	private int green = 255;
	private int blue = 255;

	public ColorizerModule(CartEntity minecart, ModuleType<?> type) {
		super(minecart, type);
	}

	/**
	 * Gets a specific color channel value.
	 * @param channel 0=red, 1=green, 2=blue
	 */
	public int getColorVal(int channel) {
		return switch (channel) {
			case 0 -> red;
			case 1 -> green;
			case 2 -> blue;
			default -> 255;
		};
	}

	/**
	 * Sets a specific color channel value (clamped to 0-255).
	 */
	public void setColorVal(int channel, int value) {
		value = Math.max(0, Math.min(255, value));
		switch (channel) {
			case 0 -> red = value;
			case 1 -> green = value;
			case 2 -> blue = value;
		}
	}

	/**
	 * Returns the color as float array [r, g, b] in range 0.0-1.0.
	 */
	public float[] getColor() {
		return new float[] { red / 255.0f, green / 255.0f, blue / 255.0f };
	}

	/**
	 * Returns the color as a packed RGB integer.
	 */
	public int getPackedColor() {
		return (red << 16) | (green << 8) | blue;
	}

	@Override
	public void writeToNbt(NbtCompound nbt) {
		nbt.putByte("Red", (byte) red);
		nbt.putByte("Green", (byte) green);
		nbt.putByte("Blue", (byte) blue);
		super.writeToNbt(nbt);
	}

	@Override
	public void readFromNbt(NbtCompound nbt) {
		red = Byte.toUnsignedInt(nbt.getByte("Red"));
		green = Byte.toUnsignedInt(nbt.getByte("Green"));
		blue = Byte.toUnsignedInt(nbt.getByte("Blue"));
		// Default to white if not saved
		if (!nbt.contains("Red")) {
			red = green = blue = 255;
		}
		super.readFromNbt(nbt);
	}
}
