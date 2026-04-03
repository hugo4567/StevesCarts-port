package vswe.stevescarts.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class DistributorBlockEntity extends BlockEntity {
    public DistributorBlockEntity(BlockPos pos, BlockState state) {
        super(StevesCartsBlockEntities.DISTRIBUTOR, pos, state);
    }
}