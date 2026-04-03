package vswe.stevescarts.module.addon;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.Configurable;
import vswe.stevescarts.module.ModuleType;
import vswe.stevescarts.module.Worker;
import vswe.stevescarts.module.addon.Toggleable;
import vswe.stevescarts.screen.CartHandler;
import vswe.stevescarts.util.TextHelper;

/**
 * Advanced Farmer Module - Automates farming tasks
 *
 * This module interacts with crops and farmland blocks to automate
 * planting, harvesting, and tilling. It supports a larger range
 * compared to the basic farmer module.
 */
public class AdvancedFarmerModule extends CartModule implements Configurable, Toggleable, Worker {

	private int harvestedCount = 0;
	private int hydratedCount = 0;
	private boolean isActive = true;
	private static final int FARM_RANGE = 3;  // 3 blocks radius (extended)
	private static final int FARM_AHEAD = 5;  // 5 blocks ahead
	private int workTicks = 0;
	private static final int WORK_INTERVAL = 18;

	public AdvancedFarmerModule(CartEntity minecart, ModuleType<?> type) {
		super(minecart, type);
	}

	@Override
	public void tick() {
		if (getEntity() == null || !isActive) return;
		
		workTicks++;
		if (workTicks >= WORK_INTERVAL) {
			workTicks = 0;
			work();
		}
	}

	@Override
	public void work() {
		if (getEntity() == null || getEntity().world == null) return;
		if (!checkMovement()) return;
		
		BlockPos railPos = getRailPos();
		
		// Harvest mature crops in radius
		for (int x = -FARM_RANGE; x <= FARM_RANGE; x++) {
			for (int z = 0; z <= FARM_AHEAD; z++) {
				BlockPos cropPos = railPos.add(x, 0, z);
				BlockState state = getEntity().world.getBlockState(cropPos);
				
				if (state.getBlock() instanceof CropBlock) {
					CropBlock crop = (CropBlock) state.getBlock();
					if (crop.isMature(state)) {
						getEntity().world.breakBlock(cropPos, true);
						harvestedCount++;
					}
				}
			}
		}
		
		// Hydrate farmland
		for (int x = -FARM_RANGE; x <= FARM_RANGE; x++) {
			for (int z = 0; z <= FARM_AHEAD; z++) {
				BlockPos farmPos = railPos.add(x, 0, z);
				BlockState state = getEntity().world.getBlockState(farmPos);
				
				if (state.getBlock() instanceof FarmlandBlock) {
					int moisture = state.get(FarmlandBlock.MOISTURE);
					if (moisture < 7) {
						getEntity().world.setBlockState(farmPos, 
							state.with(FarmlandBlock.MOISTURE, 7), 3);
						hydratedCount++;
					}
				}
			}
		}
	}

	@Override
	public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
		WLabel title = new WLabel(TextHelper.literal("Advanced Farmer"));
		panel.add(title, 0, 0);
		WLabel harvestLabel = new WLabel(TextHelper.literal("Harvested: " + harvestedCount));
		panel.add(harvestLabel, 0, 12);
		WLabel hydrateLabel = new WLabel(TextHelper.literal("Hydrated: " + hydratedCount));
		panel.add(hydrateLabel, 120, 12);
		panel.setSize(170, 30);
	}

	@Override
	public boolean isActive() {
		return isActive;
	}

	@Override
	public int getPriority() {
		return Worker.NORMAL_PRIORITY;
	}

	public void setActive(boolean active) {
		isActive = active;
	}

	@Override
	public void writeToNbt(NbtCompound nbt) {
		super.writeToNbt(nbt);
		nbt.putInt("HarvestedCount", harvestedCount);
		nbt.putInt("HydratedCount", hydratedCount);
		nbt.putBoolean("IsActive", isActive);
	}

	@Override
	public void readFromNbt(NbtCompound nbt) {
		super.readFromNbt(nbt);
		harvestedCount = nbt.getInt("HarvestedCount");
		hydratedCount = nbt.getInt("HydratedCount");
		isActive = nbt.getBoolean("IsActive");
	}
}