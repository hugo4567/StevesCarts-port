package vswe.stevescarts.block;

import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

public class LiquidManagerBlock extends Block {
    public LiquidManagerBlock() {
        super(FabricBlockSettings.of(Material.STONE).strength(1.5F));
    }
}