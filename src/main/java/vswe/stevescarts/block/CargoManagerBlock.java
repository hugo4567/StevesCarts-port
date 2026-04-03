package vswe.stevescarts.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Material;
import vswe.stevescarts.block.entity.CargoManagerBlockEntity;
import vswe.stevescarts.block.entity.StevesCartsBlockEntities;

public class CargoManagerBlock extends Block implements NamedScreenHandlerFactory {
    public CargoManagerBlock() {
        super(FabricBlockSettings.of(Material.STONE).strength(1.5F).nonOpaque());
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) return ActionResult.SUCCESS;
        
        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof CargoManagerBlockEntity cargo) {
            // Ouvrir l'inventaire du gestionnaire de fret avec la factory
            player.openHandledScreen(new ExtendedScreenHandlerFactory() {
                @Override
                public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
                    buf.writeBlockPos(pos);
                }

                @Override
                public Text getDisplayName() {
                    return CargoManagerBlockEntity.NAME;
                }

                @Override
                public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                    return GenericContainerScreenHandler.createGeneric9x3(syncId, inv, cargo);
                }
            });
        }
        return ActionResult.CONSUME;
    }

    @Override
    public Text getDisplayName() {
        return CargoManagerBlockEntity.NAME;
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inventory, PlayerEntity player) {
        return null;
    }
}