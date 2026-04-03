package vswe.stevescarts.module.processor;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.Configurable;
import vswe.stevescarts.module.ModuleType;
import vswe.stevescarts.module.Worker;
import vswe.stevescarts.screen.CartHandler;
import vswe.stevescarts.util.TextHelper;

/**
 * Advanced Crafter Module - Crafts complex recipes
 * Handles crafting of recipes with up to 9 ingredients
 */
public class AdvancedCrafterModule extends CartModule implements Configurable, Worker {
    private int recipesCompleted = 0;
    private SimpleInventory craftingGrid = new SimpleInventory(9);
    private int workTicks = 0;
    private static final int CRAFT_INTERVAL = 40;  // Every 2 seconds

    public AdvancedCrafterModule(CartEntity minecart, ModuleType<?> type) {
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
        // This would process recipes from cart inventory
        // and output crafted items
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel title = new WLabel(TextHelper.literal("Advanced Crafter"));
        panel.add(title, 0, 0);
        WLabel countLabel = new WLabel(TextHelper.literal("Recipes: " + recipesCompleted));
        panel.add(countLabel, 0, 12);
        panel.setSize(140, 30);
    }

    @Override
    public int getPriority() {
        return Worker.NORMAL_PRIORITY;
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