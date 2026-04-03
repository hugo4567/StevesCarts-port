package vswe.stevescarts.module.storage;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.ModuleType;
import vswe.stevescarts.module.StevesCartsModules;
import vswe.stevescarts.screen.CartHandler;
import vswe.stevescarts.util.FluidValue;
import vswe.stevescarts.util.TextHelper;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

/**
 * Creative Tank Module - Infinite fluid storage
 * 
 * This module provides unlimited fluid storage capacity with 4 different modes:
 * 0 = Normal (accepts any fluid)
 * 1 = Water only
 * 2 = Lava only
 * 3 = Milk only
 */
public class CreativeTankModule extends TankModule {
	private static final long INFINITE_CAPACITY = 2_000_000_000L;
	private int mode = 0;  // 0 = any, 1 = water, 2 = lava, 3 = milk

	public CreativeTankModule(CartEntity minecart, ModuleType<?> type) {
		super(minecart, type, (int)(INFINITE_CAPACITY / 1000));
	}

	@Override
	public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
		WLabel label = new WLabel(StevesCartsModules.CREATIVE_TANK.getTranslationText());
		panel.add(label, 0, 0);
		
		String modeTextKey = switch (mode) {
			case 0 -> "screen.stevescarts.cart.mode.any_fluid";
			case 1 -> "screen.stevescarts.cart.mode.water";
			case 2 -> "screen.stevescarts.cart.mode.lava";
			case 3 -> "screen.stevescarts.cart.mode.milk";
			default -> "screen.stevescarts.cart.mode.any_fluid";
		};
		
		WLabel modeLabel = new WLabel(TextHelper.translatable(modeTextKey));
		panel.add(modeLabel, 0, 12);
		
		panel.setSize(100, 30);
	}

	@Override
	public void writeToNbt(NbtCompound nbt) {
		nbt.putInt("Mode", mode);
		nbt.putLong("Fluid", INFINITE_CAPACITY);
		super.writeToNbt(nbt);
	}

	@Override
	public void readFromNbt(NbtCompound nbt) {
		mode = nbt.getInt("Mode");
		// Always reset to infinite
		super.readFromNbt(nbt);
	}

	public void setMode(int newMode) {
		mode = newMode % 4;
	}

	public int getMode() {
		return mode;
	}
}
