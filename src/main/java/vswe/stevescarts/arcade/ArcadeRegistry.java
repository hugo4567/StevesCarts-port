package vswe.stevescarts.arcade;

import vswe.stevescarts.arcade.invaders.ArcadeInvaders;
import vswe.stevescarts.arcade.monopoly.ArcadeMonopoly;
import vswe.stevescarts.arcade.sweeper.ArcadeSweeper;
import vswe.stevescarts.arcade.tetris.ArcadeTetris;
import vswe.stevescarts.arcade.tracks.ArcadeTracks;
import vswe.stevescarts.module.addon.StevesArcadeModule;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

/**
 * Registry for all available arcade games.
 */
public class ArcadeRegistry {
    
    private static final List<GameEntry> GAMES = new ArrayList<>();
    
    static {
        // Register all games in order
        register("ghast_invaders", "Ghast Invaders", ArcadeInvaders::new);
        register("block_stacker", "Block Stacker", ArcadeTetris::new);
        register("creeper_sweeper", "Creeper Sweeper", ArcadeSweeper::new);
        register("mob_monopoly", "Mob Monopoly", ArcadeMonopoly::new);
        register("track_operator", "Track Operator", ArcadeTracks::new);
    }
    
    private static void register(String id, String name, BiFunction<StevesArcadeModule, String, ArcadeGame> factory) {
        GAMES.add(new GameEntry(id, name, factory));
    }
    
    /**
     * Get the number of registered games.
     */
    public static int getGameCount() {
        return GAMES.size();
    }
    
    /**
     * Get game entry by index.
     */
    public static GameEntry getGame(int index) {
        if (index >= 0 && index < GAMES.size()) {
            return GAMES.get(index);
        }
        return null;
    }
    
    /**
     * Get all registered games.
     */
    public static List<GameEntry> getAllGames() {
        return GAMES;
    }
    
    /**
     * Create a new game instance.
     */
    public static ArcadeGame createGame(int index, StevesArcadeModule module) {
        GameEntry entry = getGame(index);
        if (entry != null) {
            return entry.factory.apply(module, entry.name);
        }
        return null;
    }
    
    /**
     * Entry for a registered game.
     */
    public static class GameEntry {
        public final String id;
        public final String name;
        public final BiFunction<StevesArcadeModule, String, ArcadeGame> factory;
        
        public GameEntry(String id, String name, BiFunction<StevesArcadeModule, String, ArcadeGame> factory) {
            this.id = id;
            this.name = name;
            this.factory = factory;
        }
    }
}
