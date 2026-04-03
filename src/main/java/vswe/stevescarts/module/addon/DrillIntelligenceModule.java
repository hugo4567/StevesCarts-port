package vswe.stevescarts.module.addon;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.ModuleType;

import net.minecraft.nbt.NbtCompound;

/**
 * Drill Intelligence Module - Enhances drill accuracy and efficiency
 * Increases mining speed and efficiency of connected drill modules
 */
public class DrillIntelligenceModule extends CartModule {
	protected float efficiencyBoost = 1.25f; // 25% speed boost
	protected boolean opportunisticMining = true; // Mine nearby ores

	public DrillIntelligenceModule(CartEntity minecart, ModuleType<?> type) {
		super(minecart, type);
	}

	@Override
	public void writeToNbt(NbtCompound nbt) {
		nbt.putFloat("EfficiencyBoost", this.efficiencyBoost);
		nbt.putBoolean("OpportunisticMining", this.opportunisticMining);
		super.writeToNbt(nbt);
	}

	@Override
	public void readFromNbt(NbtCompound nbt) {
		this.efficiencyBoost = nbt.getFloat("EfficiencyBoost");
		this.opportunisticMining = nbt.getBoolean("OpportunisticMining");
		super.readFromNbt(nbt);
	}

	public float getEfficiencyBoost() {
		return this.efficiencyBoost;
	}

	public boolean shouldMineOpportunistically() {
		return this.opportunisticMining;
	}
}