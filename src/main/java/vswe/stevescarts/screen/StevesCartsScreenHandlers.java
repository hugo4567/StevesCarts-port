package vswe.stevescarts.screen;

import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.block.entity.ActivatorBlockEntity;
import vswe.stevescarts.block.entity.CargoManagerBlockEntity;
import vswe.stevescarts.block.entity.CartAssemblerBlockEntity;
import vswe.stevescarts.block.entity.DetectorBlockEntity;
import vswe.stevescarts.block.entity.DistributorBlockEntity;
import vswe.stevescarts.block.entity.LiquidManagerBlockEntity;
import vswe.stevescarts.block.entity.UpgradeBlockEntity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;

public class StevesCartsScreenHandlers {
	// Existing handlers
	public static final ScreenHandlerType<CartAssemblerHandler> CART_ASSEMBLER = ScreenHandlerRegistry.registerExtended(
		StevesCarts.id("cart_assembler"), 
		(syncId, playerInventory, buf) -> new CartAssemblerHandler(syncId, playerInventory, ScreenHandlerContext.EMPTY, getCartAssemblerBlockEntity(buf.readBlockPos()))
	);
	
	public static final ScreenHandlerType<CartHandler> CART = ScreenHandlerRegistry.registerExtended(
		StevesCarts.id("cart"), 
		CartHandler::new
	);

	// New GUI handlers
	public static final ScreenHandlerType<UpgradeHandler> UPGRADE = ScreenHandlerRegistry.registerExtended(
		StevesCarts.id("upgrade"),
		(syncId, playerInventory, buf) -> new UpgradeHandler(syncId, playerInventory, ScreenHandlerContext.EMPTY, getUpgradeBlockEntity(buf.readBlockPos()))
	);

	public static final ScreenHandlerType<ActivatorHandler> ACTIVATOR = ScreenHandlerRegistry.registerExtended(
		StevesCarts.id("activator"),
		(syncId, playerInventory, buf) -> new ActivatorHandler(syncId, playerInventory, ScreenHandlerContext.EMPTY, getActivatorBlockEntity(buf.readBlockPos()))
	);

	public static final ScreenHandlerType<CargoHandler> CARGO = ScreenHandlerRegistry.registerExtended(
		StevesCarts.id("cargo"),
		(syncId, playerInventory, buf) -> new CargoHandler(syncId, playerInventory, ScreenHandlerContext.EMPTY, getCargoManagerBlockEntity(buf.readBlockPos()))
	);

	public static final ScreenHandlerType<LiquidHandler> LIQUID = ScreenHandlerRegistry.registerExtended(
		StevesCarts.id("liquid"),
		(syncId, playerInventory, buf) -> new LiquidHandler(syncId, playerInventory, ScreenHandlerContext.EMPTY, getLiquidManagerBlockEntity(buf.readBlockPos()))
	);

	public static final ScreenHandlerType<DistributorHandler> DISTRIBUTOR = ScreenHandlerRegistry.registerExtended(
		StevesCarts.id("distributor"),
		(syncId, playerInventory, buf) -> new DistributorHandler(syncId, playerInventory, ScreenHandlerContext.EMPTY, getDistributorBlockEntity(buf.readBlockPos()))
	);

	public static final ScreenHandlerType<DetectorHandler> DETECTOR = ScreenHandlerRegistry.registerExtended(
		StevesCarts.id("detector"),
		(syncId, playerInventory, buf) -> new DetectorHandler(syncId, playerInventory, ScreenHandlerContext.EMPTY, getDetectorBlockEntity(buf.readBlockPos()))
	);

	public static void init() {
		// Initialisation des gestionnaires d'écran
		// All screen handlers are registered statically above
	}

	// Block entity getters for client-side screen creation
	private static CartAssemblerBlockEntity getCartAssemblerBlockEntity(BlockPos pos) {
		return (CartAssemblerBlockEntity) MinecraftClient.getInstance().world.getBlockEntity(pos);
	}

	private static UpgradeBlockEntity getUpgradeBlockEntity(BlockPos pos) {
		return (UpgradeBlockEntity) MinecraftClient.getInstance().world.getBlockEntity(pos);
	}

	private static ActivatorBlockEntity getActivatorBlockEntity(BlockPos pos) {
		return (ActivatorBlockEntity) MinecraftClient.getInstance().world.getBlockEntity(pos);
	}

	private static CargoManagerBlockEntity getCargoManagerBlockEntity(BlockPos pos) {
		return (CargoManagerBlockEntity) MinecraftClient.getInstance().world.getBlockEntity(pos);
	}

	private static LiquidManagerBlockEntity getLiquidManagerBlockEntity(BlockPos pos) {
		return (LiquidManagerBlockEntity) MinecraftClient.getInstance().world.getBlockEntity(pos);
	}

	private static DistributorBlockEntity getDistributorBlockEntity(BlockPos pos) {
		return (DistributorBlockEntity) MinecraftClient.getInstance().world.getBlockEntity(pos);
	}

	private static DetectorBlockEntity getDetectorBlockEntity(BlockPos pos) {
		return (DetectorBlockEntity) MinecraftClient.getInstance().world.getBlockEntity(pos);
	}
}
