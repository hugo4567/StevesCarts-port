package vswe.stevescarts.block;

import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

public class DistributorBlock extends Block {
    public DistributorBlock() {
        super(FabricBlockSettings.of(Material.STONE).strength(1.5F));
    }
}