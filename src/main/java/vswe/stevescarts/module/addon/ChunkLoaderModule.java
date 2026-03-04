package vswe.stevescarts.module.addon;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.ModuleType;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;

/**
 * Chunk Loader Module - Keeps chunks around the cart loaded
 *
 * When active, this addon forces the chunk the cart is in to remain
 * loaded, allowing the cart to operate even when no players are nearby.
 * Consumes extra fuel (5x base consumption) while active.
 *
 * Can be toggled on/off manually or via activator rails.
 * Uses Fabric's chunk ticket system for forced chunk loading.
 */
public class ChunkLoaderModule extends CartModule {
	private boolean loadingChunk = false;
	private ChunkPos lastChunkPos = null;

	public ChunkLoaderModule(CartEntity minecart, ModuleType<?> type) {
		super(minecart, type);
	}

	@Override
	public void tick() {
		if (getEntity() == null || getEntity().world == null || getEntity().world.isClient) {
			return;
		}

		if (loadingChunk) {
			// Check fuel
			if (!getEntity().getModules().stream().anyMatch(CartModule::canPropel)) {
				setChunkLoading(false);
				return;
			}

			// Update chunk ticket when cart moves to new chunk
			if (getEntity().world instanceof ServerWorld serverWorld) {
				ChunkPos currentChunk = new ChunkPos(getEntity().getBlockPos());
				if (lastChunkPos == null || !lastChunkPos.equals(currentChunk)) {
					// Remove old ticket
					if (lastChunkPos != null) {
						serverWorld.setChunkForced(lastChunkPos.x, lastChunkPos.z, false);
					}
					// Add new ticket
					serverWorld.setChunkForced(currentChunk.x, currentChunk.z, true);
					lastChunkPos = currentChunk;
				}
			}
		}
	}

	/**
	 * Toggles chunk loading on or off.
	 */
	public void setChunkLoading(boolean loading) {
		if (this.loadingChunk == loading) return;

		this.loadingChunk = loading;

		if (!loading && lastChunkPos != null) {
			// Release chunk ticket
			if (getEntity() != null && getEntity().world instanceof ServerWorld serverWorld) {
				serverWorld.setChunkForced(lastChunkPos.x, lastChunkPos.z, false);
			}
			lastChunkPos = null;
		}
	}

	public boolean isLoadingChunk() {
		return loadingChunk;
	}

	@Override
	public void onActivate() {
		setChunkLoading(!loadingChunk);
	}

	@Override
	public void writeToNbt(NbtCompound nbt) {
		nbt.putBoolean("ChunkLoading", loadingChunk);
		super.writeToNbt(nbt);
	}

	@Override
	public void readFromNbt(NbtCompound nbt) {
		loadingChunk = nbt.getBoolean("ChunkLoading");
		super.readFromNbt(nbt);
	}
}
