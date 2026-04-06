package vswe.stevescarts.block.entity;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.Identifier;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.block.StevesCartsBlocks;

public class StevesCartsBlockEntities {
    public static BlockEntityType<CartAssemblerBlockEntity> CART_ASSEMBLER;
    public static BlockEntityType<CargoManagerBlockEntity> CARGO_MANAGER;
    public static BlockEntityType<LiquidManagerBlockEntity> LIQUID_MANAGER;
    public static BlockEntityType<DistributorBlockEntity> DISTRIBUTOR;
    public static BlockEntityType<ActivatorBlockEntity> ACTIVATOR;
    public static BlockEntityType<DetectorBlockEntity> DETECTOR;
    public static BlockEntityType<UpgradeBlockEntity> UPGRADE;

    public static void init() {
        registerBlockEntities();
    }

    public static void registerBlockEntities() {
        CART_ASSEMBLER = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            new Identifier(StevesCarts.MOD_ID, "cart_assembler"),
            FabricBlockEntityTypeBuilder.create(CartAssemblerBlockEntity::new, StevesCartsBlocks.CART_ASSEMBLER).build()
        );
        CARGO_MANAGER = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            new Identifier(StevesCarts.MOD_ID, "cargo_manager"),
            FabricBlockEntityTypeBuilder.create(CargoManagerBlockEntity::new, StevesCartsBlocks.CARGO_MANAGER).build()
        );
        LIQUID_MANAGER = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            new Identifier(StevesCarts.MOD_ID, "liquid_manager"),
            FabricBlockEntityTypeBuilder.create(LiquidManagerBlockEntity::new, StevesCartsBlocks.FLUID_MANAGER).build()
        );
        DISTRIBUTOR = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            new Identifier(StevesCarts.MOD_ID, "distributor"),
            FabricBlockEntityTypeBuilder.create(DistributorBlockEntity::new, StevesCartsBlocks.EXTERNAL_DISTRIBUTOR).build()
        );
        ACTIVATOR = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            new Identifier(StevesCarts.MOD_ID, "activator"),
            FabricBlockEntityTypeBuilder.create(ActivatorBlockEntity::new, StevesCartsBlocks.MODULE_TOGGLER_BLOCK).build()
        );
        DETECTOR = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            new Identifier(StevesCarts.MOD_ID, "detector"),
            FabricBlockEntityTypeBuilder.create(DetectorBlockEntity::new, StevesCartsBlocks.MODULE_TOGGLER_BLOCK).build()
        );
        UPGRADE = Registry.register(
            Registry.BLOCK_ENTITY_TYPE,
            new Identifier(StevesCarts.MOD_ID, "upgrade"),
            FabricBlockEntityTypeBuilder.create(UpgradeBlockEntity::new, StevesCartsBlocks.UPGRADE).build()
        );
    }
}
