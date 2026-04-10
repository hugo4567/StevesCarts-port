package vswe.stevescarts.arcade.tracks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import vswe.stevescarts.arcade.ArcadeGame;
import vswe.stevescarts.module.addon.StevesArcadeModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Track Operator - A train track switching/routing puzzle game.
 * Guide minecarts to their correct destinations by clicking to switch track junctions.
 */
public class ArcadeTracks extends ArcadeGame {
    private static final int GRID_WIDTH = 18;
    private static final int GRID_HEIGHT = 8;
    private static final int CELL_SIZE = 20;
    private static final int GRID_OFFSET_X = 30;
    private static final int GRID_OFFSET_Y = 30;
    
    // Track types
    private static final int EMPTY = 0;
    private static final int TRACK_H = 1;      // Horizontal track
    private static final int TRACK_V = 2;      // Vertical track
    private static final int TRACK_CORNER_NE = 3; // ┘
    private static final int TRACK_CORNER_NW = 4; // └
    private static final int TRACK_CORNER_SE = 5; // ┐
    private static final int TRACK_CORNER_SW = 6; // ┌
    private static final int JUNCTION_H = 7;   // Horizontal junction (clickable)
    private static final int JUNCTION_V = 8;   // Vertical junction (clickable)
    private static final int STATION_A = 10;   // Red station
    private static final int STATION_B = 11;   // Blue station
    private static final int STATION_C = 12;   // Green station
    private static final int SPAWN = 15;       // Cart spawn point
    
    private int[][] grid;
    private boolean[] junctionStates; // true = alternate path
    private List<Cart> carts;
    private int score;
    private int highscore;
    private int lives;
    private int level;
    private int spawnTimer;
    private int spawnDelay;
    private boolean gameOver;
    private Random random;
    
    public ArcadeTracks(StevesArcadeModule module, String name) {
        super(module, name);
        random = new Random();
        initGame();
    }
    
    private void initGame() {
        grid = new int[GRID_HEIGHT][GRID_WIDTH];
        junctionStates = new boolean[10]; // Max 10 junctions
        carts = new ArrayList<>();
        score = 0;
        lives = 3;
        level = 1;
        spawnTimer = 0;
        spawnDelay = 60;
        gameOver = false;
        
        createTrackLayout();
    }
    
    private void createTrackLayout() {
        // Clear grid
        for (int y = 0; y < GRID_HEIGHT; y++) {
            for (int x = 0; x < GRID_WIDTH; x++) {
                grid[y][x] = EMPTY;
            }
        }
        
        // Create a track layout with junctions
        // Spawn point on the left
        grid[3][0] = SPAWN;
        
        // Main horizontal track
        for (int x = 1; x < 5; x++) grid[3][x] = TRACK_H;
        
        // First junction (vertical split)
        grid[3][5] = JUNCTION_V; // Junction 0
        
        // Upper branch
        grid[2][5] = TRACK_V;
        grid[1][5] = TRACK_CORNER_NE;
        for (int x = 6; x < 10; x++) grid[1][x] = TRACK_H;
        grid[1][10] = STATION_A; // Red station
        
        // Lower branch from first junction
        grid[4][5] = TRACK_V;
        grid[5][5] = TRACK_CORNER_NW;
        for (int x = 6; x < 8; x++) grid[5][x] = TRACK_H;
        
        // Second junction
        grid[5][8] = JUNCTION_V; // Junction 1
        
        // Second junction upper path
        grid[4][8] = TRACK_V;
        grid[3][8] = TRACK_CORNER_NE;
        for (int x = 9; x < 14; x++) grid[3][x] = TRACK_H;
        grid[3][14] = STATION_B; // Blue station
        
        // Second junction lower path
        grid[6][8] = TRACK_V;
        grid[7][8] = TRACK_CORNER_NW;
        for (int x = 9; x < 16; x++) grid[7][x] = TRACK_H;
        grid[7][16] = STATION_C; // Green station
        
        // Straight path continuation from first junction
        for (int x = 6; x < 8; x++) grid[3][x] = TRACK_H;
        grid[3][8] = JUNCTION_H; // Junction 2
        for (int x = 9; x < 12; x++) grid[3][x] = TRACK_H;
        
        // Third junction
        grid[3][12] = JUNCTION_V; // Junction 3
        
        // Third junction paths
        grid[2][12] = TRACK_V;
        grid[1][12] = TRACK_CORNER_NE;
        for (int x = 13; x < 17; x++) grid[1][x] = TRACK_H;
        grid[1][17] = STATION_A;
        
        grid[4][12] = TRACK_V;
        grid[5][12] = TRACK_CORNER_NW;
        for (int x = 13; x < 15; x++) grid[5][x] = TRACK_H;
        grid[5][15] = STATION_B;
    }
    
    @Override
    @Environment(EnvType.CLIENT)
    public void update() {
        if (gameOver) return;
        
        // Spawn new carts
        spawnTimer++;
        if (spawnTimer >= spawnDelay) {
            spawnTimer = 0;
            spawnCart();
            // Speed up as level increases
            spawnDelay = Math.max(30, 60 - level * 5);
        }
        
        // Update carts
        for (int i = carts.size() - 1; i >= 0; i--) {
            Cart cart = carts.get(i);
            cart.moveTimer++;
            
            if (cart.moveTimer >= 5) { // Cart speed
                cart.moveTimer = 0;
                moveCart(cart);
                
                if (cart.removed) {
                    carts.remove(i);
                }
            }
        }
    }
    
    private void spawnCart() {
        int destination = random.nextInt(3); // 0=A, 1=B, 2=C
        Cart cart = new Cart(0, 3, 1, 0, destination);
        carts.add(cart);
    }
    
    private void moveCart(Cart cart) {
        int nextX = cart.x + cart.dx;
        int nextY = cart.y + cart.dy;
        
        // Check bounds
        if (nextX < 0 || nextX >= GRID_WIDTH || nextY < 0 || nextY >= GRID_HEIGHT) {
            // Cart went off track
            loseLife();
            cart.removed = true;
            return;
        }
        
        int nextCell = grid[nextY][nextX];
        
        // Check if reached station
        if (nextCell >= STATION_A && nextCell <= STATION_C) {
            int stationDest = nextCell - STATION_A;
            if (stationDest == cart.destination) {
                // Correct station!
                score += 10 * level;
                ArcadeGame.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 0.6f, 1.0f);
                if (score > highscore) highscore = score;
                if (score > 0 && score % 100 == 0) {
                    level++;
                }
            } else {
                // Wrong station
                loseLife();
            }
            cart.removed = true;
            return;
        }
        
        // Check if track exists
        if (nextCell == EMPTY) {
            loseLife();
            cart.removed = true;
            return;
        }
        
        // Move cart
        cart.x = nextX;
        cart.y = nextY;
        
        // Update direction based on track type
        updateCartDirection(cart, nextCell);
    }
    
    private void updateCartDirection(Cart cart, int trackType) {
        switch (trackType) {
            case TRACK_H:
                // Continue horizontal
                break;
            case TRACK_V:
                // Continue vertical
                break;
            case TRACK_CORNER_NE: // ┘ - from south goes east, from west goes north
                if (cart.dy == 1) { cart.dx = 1; cart.dy = 0; }
                else if (cart.dx == -1) { cart.dx = 0; cart.dy = -1; }
                break;
            case TRACK_CORNER_NW: // └ - from south goes west, from east goes north
                if (cart.dy == 1) { cart.dx = -1; cart.dy = 0; }
                else if (cart.dx == 1) { cart.dx = 0; cart.dy = -1; }
                break;
            case TRACK_CORNER_SE: // ┐ - from north goes east, from west goes south
                if (cart.dy == -1) { cart.dx = 1; cart.dy = 0; }
                else if (cart.dx == -1) { cart.dx = 0; cart.dy = 1; }
                break;
            case TRACK_CORNER_SW: // ┌ - from north goes west, from east goes south
                if (cart.dy == -1) { cart.dx = -1; cart.dy = 0; }
                else if (cart.dx == 1) { cart.dx = 0; cart.dy = 1; }
                break;
            case JUNCTION_V: // Vertical junction
                int jIdx = getJunctionIndex(cart.x, cart.y);
                if (cart.dx != 0) {
                    // Entering horizontally, choose path
                    if (junctionStates[jIdx]) {
                        cart.dy = (cart.dx > 0) ? 1 : -1;
                        cart.dx = 0;
                    }
                    // else continue straight
                }
                break;
            case JUNCTION_H: // Horizontal junction
                int jIdx2 = getJunctionIndex(cart.x, cart.y);
                if (cart.dy != 0) {
                    // Entering vertically, choose path
                    if (junctionStates[jIdx2]) {
                        cart.dx = (cart.dy > 0) ? 1 : -1;
                        cart.dy = 0;
                    }
                    // else continue straight
                }
                break;
        }
    }
    
    private int getJunctionIndex(int x, int y) {
        // Simple hash to identify junctions
        return (y * GRID_WIDTH + x) % 10;
    }
    
    private void loseLife() {
        lives--;
        ArcadeGame.playSound(SoundEvents.ENTITY_GENERIC_HURT, 0.8f, 1.0f);
        if (lives <= 0) {
            gameOver = true;
        }
    }
    
    @Override
    @Environment(EnvType.CLIENT)
    public void drawBackground(MatrixStack matrices, int mouseX, int mouseY) {
        // Draw grid background
        fill(matrices, GRID_OFFSET_X - 2, GRID_OFFSET_Y - 2,
             GRID_OFFSET_X + GRID_WIDTH * CELL_SIZE + 2, GRID_OFFSET_Y + GRID_HEIGHT * CELL_SIZE + 2,
             0xFF333333);
        fill(matrices, GRID_OFFSET_X, GRID_OFFSET_Y,
             GRID_OFFSET_X + GRID_WIDTH * CELL_SIZE, GRID_OFFSET_Y + GRID_HEIGHT * CELL_SIZE,
             0xFF1a1a1a);
        
        // Draw tracks
        for (int y = 0; y < GRID_HEIGHT; y++) {
            for (int x = 0; x < GRID_WIDTH; x++) {
                int px = GRID_OFFSET_X + x * CELL_SIZE;
                int py = GRID_OFFSET_Y + y * CELL_SIZE;
                int cell = grid[y][x];
                
                if (cell != EMPTY) {
                    drawTrack(matrices, px, py, cell, x, y);
                }
            }
        }
        
        // Draw carts
        for (Cart cart : carts) {
            int px = GRID_OFFSET_X + cart.x * CELL_SIZE + CELL_SIZE / 4;
            int py = GRID_OFFSET_Y + cart.y * CELL_SIZE + CELL_SIZE / 4;
            int color = getDestinationColor(cart.destination);
            fill(matrices, px, py, px + CELL_SIZE / 2, py + CELL_SIZE / 2, color);
            // Border
            fill(matrices, px, py, px + CELL_SIZE / 2, py + 1, 0xFF000000);
            fill(matrices, px, py, px + 1, py + CELL_SIZE / 2, 0xFF000000);
        }
    }
    
    private void drawTrack(MatrixStack matrices, int x, int y, int type, int gridX, int gridY) {
        int trackColor = 0xFF8B4513; // Brown
        int cx = x + CELL_SIZE / 2;
        int cy = y + CELL_SIZE / 2;
        int hw = 3; // Half width of track
        
        switch (type) {
            case TRACK_H:
                fill(matrices, x, cy - hw, x + CELL_SIZE, cy + hw, trackColor);
                break;
            case TRACK_V:
                fill(matrices, cx - hw, y, cx + hw, y + CELL_SIZE, trackColor);
                break;
            case TRACK_CORNER_NE:
                fill(matrices, x, cy - hw, cx + hw, cy + hw, trackColor);
                fill(matrices, cx - hw, y, cx + hw, cy + hw, trackColor);
                break;
            case TRACK_CORNER_NW:
                fill(matrices, cx - hw, cy - hw, x + CELL_SIZE, cy + hw, trackColor);
                fill(matrices, cx - hw, y, cx + hw, cy + hw, trackColor);
                break;
            case TRACK_CORNER_SE:
                fill(matrices, x, cy - hw, cx + hw, cy + hw, trackColor);
                fill(matrices, cx - hw, cy - hw, cx + hw, y + CELL_SIZE, trackColor);
                break;
            case TRACK_CORNER_SW:
                fill(matrices, cx - hw, cy - hw, x + CELL_SIZE, cy + hw, trackColor);
                fill(matrices, cx - hw, cy - hw, cx + hw, y + CELL_SIZE, trackColor);
                break;
            case JUNCTION_V:
            case JUNCTION_H:
                int jIdx = getJunctionIndex(gridX, gridY);
                int jColor = junctionStates[jIdx] ? 0xFF00AA00 : 0xFFAA0000;
                fill(matrices, x + 2, y + 2, x + CELL_SIZE - 2, y + CELL_SIZE - 2, jColor);
                // Draw track through junction
                fill(matrices, x, cy - hw, x + CELL_SIZE, cy + hw, trackColor);
                if (type == JUNCTION_V) {
                    fill(matrices, cx - hw, y, cx + hw, y + CELL_SIZE, trackColor);
                }
                break;
            case STATION_A:
                fill(matrices, x + 2, y + 2, x + CELL_SIZE - 2, y + CELL_SIZE - 2, 0xFFAA0000);
                break;
            case STATION_B:
                fill(matrices, x + 2, y + 2, x + CELL_SIZE - 2, y + CELL_SIZE - 2, 0xFF0000AA);
                break;
            case STATION_C:
                fill(matrices, x + 2, y + 2, x + CELL_SIZE - 2, y + CELL_SIZE - 2, 0xFF00AA00);
                break;
            case SPAWN:
                fill(matrices, x, cy - hw, x + CELL_SIZE, cy + hw, trackColor);
                fill(matrices, x + 2, y + 2, x + CELL_SIZE - 2, y + CELL_SIZE - 2, 0xFFFFFF00);
                break;
        }
    }
    
    private int getDestinationColor(int dest) {
        return switch (dest) {
            case 0 -> 0xFFFF0000; // Red
            case 1 -> 0xFF0000FF; // Blue
            case 2 -> 0xFF00FF00; // Green
            default -> 0xFFFFFFFF;
        };
    }
    
    @Override
    @Environment(EnvType.CLIENT)
    public void drawForeground(MatrixStack matrices, TextRenderer textRenderer) {
        drawString(matrices, textRenderer, "Track Operator", 170, 5, 0x404040);
        
        // Info panel
        drawString(matrices, textRenderer, "Score: " + score, 380, 40, 0x404040);
        drawString(matrices, textRenderer, "High: " + highscore, 380, 55, 0x404040);
        drawString(matrices, textRenderer, "Level: " + level, 380, 70, 0x404040);
        drawString(matrices, textRenderer, "Lives: " + lives, 380, 85, 0x404040);
        
        // Legend
        drawString(matrices, textRenderer, "Stations:", 380, 110, 0x404040);
        fill(matrices, 380, 125, 390, 135, 0xFFAA0000);
        drawString(matrices, textRenderer, "A", 395, 125, 0x404040);
        fill(matrices, 410, 125, 420, 135, 0xFF0000AA);
        drawString(matrices, textRenderer, "B", 425, 125, 0x404040);
        fill(matrices, 380, 140, 390, 150, 0xFF00AA00);
        drawString(matrices, textRenderer, "C", 395, 140, 0x404040);
        
        // Controls
        drawString(matrices, textRenderer, "Click junctions", 380, 165, 0x404040);
        drawString(matrices, textRenderer, "to switch tracks!", 380, 177, 0x404040);
        drawString(matrices, textRenderer, "R - Restart", 380, 200, 0x404040);
        
        if (gameOver) {
            drawCenteredString(matrices, textRenderer, "GAME OVER!", 200, 100, 0xAA0000);
            drawCenteredString(matrices, textRenderer, "Press R to restart", 200, 115, 0x404040);
        }
    }
    
    @Override
    @Environment(EnvType.CLIENT)
    public void mouseClicked(int x, int y, int button) {
        if (gameOver || button != 0) return;
        
        // Check if clicked on a junction
        int gridX = (x - GRID_OFFSET_X) / CELL_SIZE;
        int gridY = (y - GRID_OFFSET_Y) / CELL_SIZE;
        
        if (gridX >= 0 && gridX < GRID_WIDTH && gridY >= 0 && gridY < GRID_HEIGHT) {
            int cell = grid[gridY][gridX];
            if (cell == JUNCTION_H || cell == JUNCTION_V) {
                int jIdx = getJunctionIndex(gridX, gridY);
                junctionStates[jIdx] = !junctionStates[jIdx];
                ArcadeGame.playSound(SoundEvents.BLOCK_LEVER_CLICK, 0.8f, junctionStates[jIdx] ? 1.2f : 0.8f);
            }
        }
    }
    
    @Override
    @Environment(EnvType.CLIENT)
    public void keyPress(char character, int keyCode) {
        if (Character.toLowerCase(character) == 'r') {
            initGame();
        }
    }
    
    @Override
    public void save(NbtCompound nbt, int id) {
        nbt.putInt(generateNBTName("HighscoreTracks", id), highscore);
    }
    
    @Override
    public void load(NbtCompound nbt, int id) {
        highscore = nbt.getInt(generateNBTName("HighscoreTracks", id));
    }
    
    @Override
    public void receivePacket(int id, byte[] data, PlayerEntity player) {
        if (id == 2 && data.length >= 2) {
            short data2 = data[0];
            short data3 = data[1];
            if (data2 < 0) data2 += 256;
            if (data3 < 0) data3 += 256;
            highscore = (data2 | data3 << 8);
        }
    }
    
    /**
     * Represents a minecart on the track.
     */
    private static class Cart {
        int x, y;
        int dx, dy; // Direction
        int destination; // 0=A, 1=B, 2=C
        int moveTimer;
        boolean removed;
        
        Cart(int x, int y, int dx, int dy, int destination) {
            this.x = x;
            this.y = y;
            this.dx = dx;
            this.dy = dy;
            this.destination = destination;
            this.moveTimer = 0;
            this.removed = false;
        }
    }
}
