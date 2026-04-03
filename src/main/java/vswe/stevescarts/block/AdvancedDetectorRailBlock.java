package vswe.stevescarts.block;

import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

public class AdvancedDetectorRailBlock extends Block {
    public AdvancedDetectorRailBlock() {
        super(FabricBlockSettings.of(Material.METAL).strength(5.0F, 6.0F));
    }
}
