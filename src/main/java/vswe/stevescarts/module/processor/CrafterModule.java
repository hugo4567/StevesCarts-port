package vswe.stevescarts.module.processor;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.Configurable;
import vswe.stevescarts.module.ModuleType;
import vswe.stevescarts.module.Worker;
import vswe.stevescarts.screen.CartHandler;
import vswe.stevescarts.util.TextHelper;

/**
 * Crafter Module - Basic crafting
 * Crafts recipes with up to 4 ingredients
 */
public class CrafterModule extends CartModule implements Configurable, Worker {
    private int recipesCompleted = 0;
    private int workTicks = 0;
    private static final int CRAFT_INTERVAL = 45;  // Every 2.25 seconds

    public CrafterModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        if (getEntity() == null) return;
        
        workTicks++;
        if (workTicks >= CRAFT_INTERVAL) {
            workTicks = 0;
            work();
        }
    }

    @Override
    public void work() {
        // Process simple crafting recipes from inventory
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel title = new WLabel(TextHelper.literal("Crafter"));
        panel.add(title, 0, 0);
        WLabel countLabel = new WLabel(TextHelper.literal("Crafted: " + recipesCompleted));
        panel.add(countLabel, 0, 12);
        panel.setSize(140, 30);
    }

    @Override
    public int getPriority() {
        return Worker.LOW_PRIORITY;
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        super.writeToNbt(nbt);
        nbt.putInt("RecipesCompleted", recipesCompleted);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        recipesCompleted = nbt.getInt("RecipesCompleted");
    }
}