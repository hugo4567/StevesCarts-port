package vswe.stevescarts.module.engine;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.ModuleType;
import vswe.stevescarts.module.StevesCartsModules;
import vswe.stevescarts.screen.CartHandler;
import vswe.stevescarts.screen.widget.WLongPropertyLabel;
import vswe.stevescarts.util.LongProperty;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

/**
 * Creative Engine Module - Provides infinite power
 * Equivalent to ModuleCheatEngine from v1.12.2
 * 
 * This engine provides unlimited power and can never be refueled.
 * Perfect for creative/admin builds!
 */
public class CreativeEngineModule extends EngineModule {
	public static final long INFINITE_POWER = 9001L;
	private final LongProperty power = LongProperty.create();

	public CreativeEngineModule(CartEntity minecart, ModuleType<?> type) {
		super(minecart, type);
		power.accept(INFINITE_POWER);
	}

	@Override
	public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
		WLabel label = new WLabel(StevesCartsModules.CREATIVE_ENGINE.getTranslationText());
		panel.add(label, 0, 0);
		super.addPriorityButton(handler, panel, 0, 11);
		
		// Add text showing infinite power
		WLongPropertyLabel infiniteLabel = new WLongPropertyLabel("screen.stevescarts.cart.power", this.power);
		panel.add(infiniteLabel, 17, 12);
		
		panel.setSize(72, 30);
	}

	@Override
	public void writeToNbt(NbtCompound nbt) {
		// Don't persist power, always infinite
		super.writeToNbt(nbt);
	}

	@Override
	public void readFromNbt(NbtCompound nbt) {
		// Always reset to infinite power
		power.accept(INFINITE_POWER);
		super.readFromNbt(nbt);
	}

	@Override
	public boolean canPropel() {
		// Always has power
		return true;
	}

	@Override
	public void onPropel() {
		// Do nothing - infinite power never depletes
	}

	@Override
	protected String getDiscriminator() {
		return "creative";
	}
}

