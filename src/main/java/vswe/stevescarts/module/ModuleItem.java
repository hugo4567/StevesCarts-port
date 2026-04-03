package vswe.stevescarts.module;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import vswe.stevescarts.util.TextHelper;

public class ModuleItem extends Item {
	private final ModuleType<?> moduleType;

	public ModuleItem(Settings settings, ModuleType<? extends CartModule> moduleType) {
		super(settings);
		this.moduleType = moduleType;
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		// Afficher le type de module dans le tooltip
		tooltip.add(TextHelper.formatted(TextHelper.literal("Module: " + this.moduleType.getId().getPath()), Formatting.GRAY));
	}

	public ModuleType<?> getType() {
		return this.moduleType;
	}

	@Override
	protected String getOrCreateTranslationKey() {
		return this.moduleType.getTranslationKey();
	}
}
