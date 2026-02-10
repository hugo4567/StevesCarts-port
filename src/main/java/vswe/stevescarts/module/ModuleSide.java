package vswe.stevescarts.module;

import net.minecraft.text.Text;
import vswe.stevescarts.util.TextHelper;

public enum ModuleSide {
	TOP,
	CENTER,
	BOTTOM,
	BACK,
	LEFT,
	RIGHT,
	FRONT;

	public Text asText() {
		return TextHelper.translatable("module.side.stevescarts." + this.name().toLowerCase());
	}
}
