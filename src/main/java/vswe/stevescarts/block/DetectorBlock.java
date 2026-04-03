package vswe.stevescarts.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Material;
import vswe.stevescarts.block.entity.DetectorBlockEntity;
import vswe.stevescarts.block.entity.StevesCartsBlockEntities;

public class DetectorBlock extends Block {
    public DetectorBlock() {
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
        if (be instanceof DetectorBlockEntity detector) {
            // Rotate detector type on right-click
            DetectorBlockEntity.DetectorType[] types = DetectorBlockEntity.DetectorType.values();
            int nextIndex = (detector.getDetectorType().ordinal() + 1) % types.length;
            detector.setDetectorType(types[nextIndex]);
        }
        return ActionResult.CONSUME;
    }

}