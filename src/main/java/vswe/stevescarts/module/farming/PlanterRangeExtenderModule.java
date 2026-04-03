package vswe.stevescarts.module.farming;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.Configurable;
import vswe.stevescarts.module.ModuleType;
import vswe.stevescarts.screen.CartHandler;
import vswe.stevescarts.util.TextHelper;

/**
 * Planter Range Extender Module - Extends planting range
 * Increases the range and efficiency of planting modules
 */
public class PlanterRangeExtenderModule extends CartModule implements Configurable {
    private int rangeBoost = 1;  // Default 1 block extra range
    private boolean active = true;
    private static final int MAX_BOOST = 4;

    public PlanterRangeExtenderModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        // Range boost is applied by other modules through getter
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel label = new WLabel(TextHelper.literal("Planter Range Extender"));
        panel.add(label, 0, 0);
        
        String boostText = active ? "+" + rangeBoost + " blocks" : "Inactive";
        WLabel boostLabel = new WLabel(TextHelper.literal("Boost: " + boostText));
        panel.add(boostLabel, 0, 12);
        
        panel.setSize(140, 30);
    }

    public int getRangeBoost() {
        return active ? rangeBoost : 0;
    }

    public void setRangeBoost(int boost) {
        rangeBoost = Math.min(Math.max(boost, 1), MAX_BOOST);
    }

    public void toggleActive() {
        active = !active;
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        super.writeToNbt(nbt);
        nbt.putInt("RangeBoost", rangeBoost);
        nbt.putBoolean("Active", active);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        rangeBoost = nbt.getInt("RangeBoost");
        active = nbt.getBoolean("Active");
    }
}
