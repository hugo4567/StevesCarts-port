package vswe.stevescarts.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class UpgradeBlockEntity extends BlockEntity {
    public UpgradeBlockEntity(BlockPos pos, BlockState state) {
        super(StevesCartsBlockEntities.UPGRADE, pos, state);
    }
}