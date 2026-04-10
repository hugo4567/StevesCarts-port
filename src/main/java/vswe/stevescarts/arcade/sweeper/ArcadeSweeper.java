package vswe.stevescarts.arcade.sweeper;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import vswe.stevescarts.arcade.ArcadeGame;
import vswe.stevescarts.module.addon.StevesArcadeModule;

import java.util.Random;

/**
 * Creeper Sweeper - A Minesweeper clone with Creepers.
 */
public class ArcadeSweeper extends ArcadeGame {
    private static final int GRID_COLS = 20;
    private static final int GRID_ROWS = 10;
    private static final int CELL_SIZE = 16;
    private static final int GRID_OFFSET_X = 50;
    private static final int GRID_OFFSET_Y = 20;
    private static final int CREEPER_COUNT = 30;
    
    private Cell[][] grid;
    private boolean gameOver;
    private boolean gameWon;
    private int revealedCount;
    private int flaggedCount;
    private int highscore;
    private int currentTime;
    private int tickCounter;
    private boolean firstClick;
    
    public ArcadeSweeper(StevesArcadeModule module, String name) {
        super(module, name);
        initGame();
    }
    
    private void initGame() {
        grid = new Cell[GRID_ROWS][GRID_COLS];
        for (int row = 0; row < GRID_ROWS; row++) {
            for (int col = 0; col < GRID_COLS; col++) {
                grid[row][col] = new Cell();
            }
        }
        gameOver = false;
        gameWon = false;
        revealedCount = 0;
        flaggedCount = 0;
        currentTime = 0;
        tickCounter = 0;
        firstClick = true;
    }
    
    private void placeCreepers(int excludeRow, int excludeCol) {
        Random random = new Random();
        int placed = 0;
        while (placed < CREEPER_COUNT) {
            int row = random.nextInt(GRID_ROWS);
            int col = random.nextInt(GRID_COLS);
            // Don't place on first click or adjacent cells
            if (Math.abs(row - excludeRow) <= 1 && Math.abs(col - excludeCol) <= 1) {
                continue;
            }
            if (!grid[row][col].isCreeper) {
                grid[row][col].isCreeper = true;
                placed++;
            }
        }
        // Calculate adjacent creeper counts
        for (int row = 0; row < GRID_ROWS; row++) {
            for (int col = 0; col < GRID_COLS; col++) {
                if (!grid[row][col].isCreeper) {
                    grid[row][col].adjacentCreepers = countAdjacentCreepers(row, col);
                }
            }
        }
    }
    
    private int countAdjacentCreepers(int row, int col) {
        int count = 0;
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;
                int nr = row + dr;
                int nc = col + dc;
                if (nr >= 0 && nr < GRID_ROWS && nc >= 0 && nc < GRID_COLS) {
                    if (grid[nr][nc].isCreeper) {
                        count++;
                    }
                }
            }
        }
        return count;
    }
    
    private void revealCell(int row, int col) {
        if (row < 0 || row >= GRID_ROWS || col < 0 || col >= GRID_COLS) return;
        Cell cell = grid[row][col];
        if (cell.revealed || cell.flagged) return;
        
        cell.revealed = true;
        revealedCount++;
        
        if (cell.isCreeper) {
            gameOver = true;
            revealAllCreepers();
            ArcadeGame.playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0f, 1.0f);
            return;
        }
        
        if (cell.adjacentCreepers == 0) {
            // Flood fill reveal
            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    if (dr != 0 || dc != 0) {
                        revealCell(row + dr, col + dc);
                    }
                }
            }
        }
        
        checkWin();
    }
    
    private void revealAllCreepers() {
        for (int row = 0; row < GRID_ROWS; row++) {
            for (int col = 0; col < GRID_COLS; col++) {
                if (grid[row][col].isCreeper) {
                    grid[row][col].revealed = true;
                }
            }
        }
    }
    
    private void checkWin() {
        int totalSafe = GRID_ROWS * GRID_COLS - CREEPER_COUNT;
        if (revealedCount >= totalSafe) {
            gameWon = true;
            gameOver = true;
            if (highscore == 0 || currentTime < highscore) {
                highscore = currentTime;
            }
            ArcadeGame.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        }
    }
    
    @Override
    @Environment(EnvType.CLIENT)
    public void update() {
        if (!gameOver && !firstClick) {
            tickCounter++;
            if (tickCounter >= 20) {
                tickCounter = 0;
                currentTime++;
            }
        }
    }
    
    @Override
    @Environment(EnvType.CLIENT)
    public void drawBackground(MatrixStack matrices, int mouseX, int mouseY) {
        // Draw grid
        for (int row = 0; row < GRID_ROWS; row++) {
            for (int col = 0; col < GRID_COLS; col++) {
                int x = GRID_OFFSET_X + col * CELL_SIZE;
                int y = GRID_OFFSET_Y + row * CELL_SIZE;
                Cell cell = grid[row][col];
                
                if (cell.revealed) {
                    if (cell.isCreeper) {
                        // Creeper face (red background)
                        fill(matrices, x, y, x + CELL_SIZE, y + CELL_SIZE, 0xFFFF4444);
                        fill(matrices, x + 2, y + 2, x + CELL_SIZE - 2, y + CELL_SIZE - 2, 0xFF44AA44);
                    } else {
                        // Revealed cell
                        fill(matrices, x, y, x + CELL_SIZE, y + CELL_SIZE, 0xFFCCCCCC);
                        fill(matrices, x + 1, y + 1, x + CELL_SIZE - 1, y + CELL_SIZE - 1, 0xFFAAAAAA);
                    }
                } else {
                    // Hidden cell
                    fill(matrices, x, y, x + CELL_SIZE, y + CELL_SIZE, 0xFF888888);
                    fill(matrices, x + 1, y + 1, x + CELL_SIZE - 1, y + CELL_SIZE - 1, 0xFF666666);
                    if (cell.flagged) {
                        // Flag indicator (red dot)
                        fill(matrices, x + 4, y + 4, x + 12, y + 12, 0xFFFF0000);
                    }
                }
            }
        }
    }
    
    @Override
    @Environment(EnvType.CLIENT)
    public void drawForeground(MatrixStack matrices, TextRenderer textRenderer) {
        // Draw numbers on revealed cells
        for (int row = 0; row < GRID_ROWS; row++) {
            for (int col = 0; col < GRID_COLS; col++) {
                Cell cell = grid[row][col];
                if (cell.revealed && !cell.isCreeper && cell.adjacentCreepers > 0) {
                    int x = GRID_OFFSET_X + col * CELL_SIZE + 5;
                    int y = GRID_OFFSET_Y + row * CELL_SIZE + 4;
                    int color = getNumberColor(cell.adjacentCreepers);
                    drawString(matrices, textRenderer, String.valueOf(cell.adjacentCreepers), x, y, color);
                }
            }
        }
        
        // Game info
        drawString(matrices, textRenderer, "Creeper Sweeper", GRID_OFFSET_X, 5, 0x404040);
        drawString(matrices, textRenderer, "Creepers: " + (CREEPER_COUNT - flaggedCount), 380, 30, 0x404040);
        drawString(matrices, textRenderer, "Time: " + currentTime + "s", 380, 50, 0x404040);
        drawString(matrices, textRenderer, "Best: " + (highscore > 0 ? highscore + "s" : "---"), 380, 70, 0x404040);
        drawString(matrices, textRenderer, "Left Click: Reveal", 380, 100, 0x404040);
        drawString(matrices, textRenderer, "Right Click: Flag", 380, 112, 0x404040);
        drawString(matrices, textRenderer, "R: Restart", 380, 124, 0x404040);
        
        if (gameOver) {
            String message = gameWon ? "YOU WIN!" : "GAME OVER!";
            int color = gameWon ? 0x00AA00 : 0xAA0000;
            drawCenteredString(matrices, textRenderer, message, 200, 180, color);
        }
    }
    
    private int getNumberColor(int number) {
        return switch (number) {
            case 1 -> 0x0000FF;
            case 2 -> 0x00AA00;
            case 3 -> 0xFF0000;
            case 4 -> 0x000088;
            case 5 -> 0x880000;
            case 6 -> 0x008888;
            case 7 -> 0x000000;
            case 8 -> 0x888888;
            default -> 0x000000;
        };
    }
    
    @Override
    @Environment(EnvType.CLIENT)
    public void mouseClicked(int x, int y, int button) {
        if (gameOver) {
            return;
        }
        
        int col = (x - GRID_OFFSET_X) / CELL_SIZE;
        int row = (y - GRID_OFFSET_Y) / CELL_SIZE;
        
        if (row >= 0 && row < GRID_ROWS && col >= 0 && col < GRID_COLS) {
            Cell cell = grid[row][col];
            
            if (button == 0) { // Left click - reveal
                if (!cell.flagged) {
                    if (firstClick) {
                        firstClick = false;
                        placeCreepers(row, col);
                    }
                    revealCell(row, col);
                    if (!gameOver) {
                        ArcadeGame.playSound(SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON, 0.5f, 1.0f);
                    }
                }
            } else if (button == 1) { // Right click - flag
                if (!cell.revealed) {
                    cell.flagged = !cell.flagged;
                    flaggedCount += cell.flagged ? 1 : -1;
                    ArcadeGame.playSound(SoundEvents.BLOCK_NOTE_BLOCK_HAT, 0.5f, cell.flagged ? 1.2f : 0.8f);
                }
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
        nbt.putInt(generateNBTName("HighscoreSweeper", id), highscore);
    }
    
    @Override
    public void load(NbtCompound nbt, int id) {
        highscore = nbt.getInt(generateNBTName("HighscoreSweeper", id));
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
     * Represents a single cell in the minesweeper grid.
     */
    private static class Cell {
        boolean isCreeper = false;
        boolean revealed = false;
        boolean flagged = false;
        int adjacentCreepers = 0;
    }
}
