package vswe.stevescarts.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class LiquidManagerBlockEntity extends BlockEntity {
    public LiquidManagerBlockEntity(BlockPos pos, BlockState state) {
        super(StevesCartsBlockEntities.LIQUID_MANAGER, pos, state);
    }
}