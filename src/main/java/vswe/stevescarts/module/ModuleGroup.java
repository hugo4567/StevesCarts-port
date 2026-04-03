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
	},
	DETECTOR(8, 0xFF69B4, TextHelper.translatable("module.stevescarts.category.detector.title")),
	FARMING(7, 0x228B22, TextHelper.translatable("module.stevescarts.category.farming.title")),
	MANAGER(4, 0x4B0082, TextHelper.translatable("module.stevescarts.category.manager.title")),
	PROCESSOR(10, 0xFF4500, TextHelper.translatable("module.stevescarts.category.processor.title")),
	REALTIMER(6, 0x00CED1, TextHelper.translatable("module.stevescarts.category.realtimer.title")),
	WEAPON(8, 0x8B0000, TextHelper.translatable("module.stevescarts.category.weapon.title"));

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
