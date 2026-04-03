package vswe.stevescarts.module.addon;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.ModuleType;
import vswe.stevescarts.module.Worker;

import net.minecraft.nbt.NbtCompound;

/**
 * Ore Extractor Module - Automatically mines valuable ores
 * Focuses on extracting ore blocks and rare minerals
 */
public class OreExtractorModule extends CartModule implements Worker {
	protected int miningTimer = 0;
	protected static final int MINE_INTERVAL = 50; // Mine every 2.5 seconds
	protected int blocksExtracted = 0;
	protected static final int EXTRACTION_RANGE = 4; // 4 block radius

	public OreExtractorModule(CartEntity minecart, ModuleType<?> type) {
		super(minecart, type);
	}

	@Override
	public void writeToNbt(NbtCompound nbt) {
		nbt.putInt("MineTimer", this.miningTimer);
		nbt.putInt("BlocksExtracted", this.blocksExtracted);
		super.writeToNbt(nbt);
	}

	@Override
	public void readFromNbt(NbtCompound nbt) {
		this.miningTimer = nbt.getInt("MineTimer");
		this.blocksExtracted = nbt.getInt("BlocksExtracted");
		super.readFromNbt(nbt);
	}

	@Override
	public void work() {
		if (++this.miningTimer >= MINE_INTERVAL) {
			this.miningTimer = 0;
			// Ore mining logic would happen here
		}
	}
}