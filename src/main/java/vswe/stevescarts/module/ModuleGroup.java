package vswe.stevescarts.module;

import vswe.stevescarts.module.hull.HullModule;
import vswe.stevescarts.util.TextHelper;

import net.minecraft.text.Text;

public enum ModuleGroup {
	HULL(1, 0xFFFFFF, TextHelper.translatable("module.stevescarts.category.hull.title")),
	ENGINE(5, 0xFFA500, TextHelper.translatable("module.stevescarts.category.engines.title")) {
		@Override
		public int getMax(ModuleType<? extends HullModule> type) {
			return type.asHull().getHullData().maxEngines();
		}
	},
	TOOL(1, 0x3A243B, TextHelper.translatable("module.stevescarts.category.tool.title")),
	ATTACHMENT(6, 0x0A3DC9, TextHelper.translatable("module.stevescarts.category.attachments.title")),
	STORAGE(4, 0x7A0800, TextHelper.translatable("module.stevescarts.category.storage.title")),
	ADDON(12, 0x056608, TextHelper.translatable("module.stevescarts.category.addons.title")) {
		@Override
		public int getMax(ModuleType<? extends HullModule> type) {
			return type.asHull().getHullData().maxAddons();
		}
	};

	private final int maxMax;
	private final int textColor;
	private final Text translation;

	ModuleGroup(int maxMax, int textColor, Text translation) {
		this.maxMax = maxMax;
		this.textColor = textColor;
		this.translation = translation;
	}

	public int getMax(ModuleType<? extends HullModule> type) {
		return this.getMaxMax();
	}

	public int getMaxMax() {
		return this.maxMax;
	}

	public int getTextColor() {
		return textColor;
	}

	public Text getTranslation() {
		return translation;
	}
}
