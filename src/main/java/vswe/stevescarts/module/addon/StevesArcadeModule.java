package vswe.stevescarts.module.addon;

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
 * Steve's Arcade Module - Entertainment module
 * Provides various arcade-like minigames and entertainment
 */
public class StevesArcadeModule extends CartModule implements Configurable, Toggleable {
    private boolean active = true;
    private int gameMode = 0;  // 0=off, 1=game1, 2=game2, etc
    private int gameScore = 0;
    private int gameTicks = 0;

    public StevesArcadeModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        if (!active || gameMode == 0) return;
        
        gameTicks++;
        if (gameTicks >= 20) {
            gameTicks = 0;
            updateGameState();
        }
    }

    private void updateGameState() {
        switch(gameMode) {
            case 1:
                // Game 1 logic
                gameScore += 10;
                break;
            case 2:
                // Game 2 logic
                gameScore += 5;
                break;
        }
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel title = new WLabel(TextHelper.literal("Steve's Arcade"));
        panel.add(title, 0, 0);
        
        String modeText = switch(gameMode) {
            case 0 -> "Offline";
            case 1 -> "Game 1";
            case 2 -> "Game 2";
            default -> "Unknown";
        };
        
        WLabel modeLabel = new WLabel(TextHelper.literal("Mode: " + modeText));
        panel.add(modeLabel, 0, 12);
        
        WLabel scoreLabel = new WLabel(TextHelper.literal("Score: " + gameScore));
        panel.add(scoreLabel, 60, 12);
        
        panel.setSize(140, 30);
    }

    public void setGameMode(int mode) {
        gameMode = mode;
        gameScore = 0;
        gameTicks = 0;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        super.writeToNbt(nbt);
        nbt.putBoolean("Active", active);
        nbt.putInt("GameMode", gameMode);
        nbt.putInt("GameScore", gameScore);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        active = nbt.getBoolean("Active");
        gameMode = nbt.getInt("GameMode");
        gameScore = nbt.getInt("GameScore");
    }
}