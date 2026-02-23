package vswe.stevescarts.module.addon;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.ModuleType;

import net.minecraft.nbt.NbtCompound;

import java.util.Random;

/**
 * Color Randomizer Module - Randomizes the cart's color
 *
 * Similar to the Colorizer but instead of manual RGB control,
 * this addon randomizes the cart color on button press or
 * when activated by a powered rail. Has a cooldown of 5 ticks
 * between randomizations.
 */
public class ColorRandomizerModule extends CartModule {
	private int red = 255;
	private int green = 255;
	private int blue = 255;
	private int cooldown = 0;
	private final Random random = new Random();

	public ColorRandomizerModule(CartEntity minecart, ModuleType<?> type) {
		super(minecart, type);
	}

	@Override
	public void tick() {
		if (cooldown > 0) {
			--cooldown;
		}
	}

	@Override
	public void onActivate() {
		if (cooldown == 0) {
			randomizeColor();
			cooldown = 5;
		}
	}

	/**
	 * Randomizes all three color channels.
	 */
	public void randomizeColor() {
		red = random.nextInt(256);
		green = random.nextInt(256);
		blue = random.nextInt(256);
	}

	public int getColorVal(int channel) {
		return switch (channel) {
			case 0 -> red;
			case 1 -> green;
			case 2 -> blue;
			default -> 255;
		};
	}

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
		if (!nbt.contains("Red")) {
			red = green = blue = 255;
		}
		super.readFromNbt(nbt);
	}
}
