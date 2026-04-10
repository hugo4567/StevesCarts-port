package vswe.stevescarts.arcade.tetris;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import org.lwjgl.glfw.GLFW;
import vswe.stevescarts.arcade.ArcadeGame;
import vswe.stevescarts.module.addon.StevesArcadeModule;

import java.util.Random;

/**
 * Block Stacker - A Tetris clone.
 */
public class ArcadeTetris extends ArcadeGame {
    private static final int BOARD_WIDTH = 10;
    private static final int BOARD_HEIGHT = 20;
    private static final int CELL_SIZE = 8;
    private static final int BOARD_OFFSET_X = 180;
    private static final int BOARD_OFFSET_Y = 10;
    
    // Tetromino shapes (each piece has 4 rotations)
    private static final int[][][] TETROMINOES = {
        // I
        {{0,0}, {1,0}, {2,0}, {3,0}},
        // O
        {{0,0}, {1,0}, {0,1}, {1,1}},
        // T
        {{1,0}, {0,1}, {1,1}, {2,1}},
        // S
        {{1,0}, {2,0}, {0,1}, {1,1}},
        // Z
        {{0,0}, {1,0}, {1,1}, {2,1}},
        // J
        {{0,0}, {0,1}, {1,1}, {2,1}},
        // L
        {{2,0}, {0,1}, {1,1}, {2,1}}
    };
    
    private static final int[] COLORS = {
        0xFF00FFFF, // I - Cyan
        0xFFFFFF00, // O - Yellow
        0xFFAA00AA, // T - Purple
        0xFF00FF00, // S - Green
        0xFFFF0000, // Z - Red
        0xFF0000FF, // J - Blue
        0xFFFFAA00  // L - Orange
    };
    
    private int[][] board;
    private int[][] currentPiece;
    private int currentType;
    private int currentX;
    private int currentY;
    private int currentRotation;
    private int nextType;
    private int score;
    private int highscore;
    private int level;
    private int linesCleared;
    private int fallDelay;
    private int fallCounter;
    private boolean gameOver;
    private Random random;
    
    private boolean keyLeft;
    private boolean keyRight;
    private boolean keyDown;
    private int keyRepeatCounter;
    
    public ArcadeTetris(StevesArcadeModule module, String name) {
        super(module, name);
        random = new Random();
        initGame();
    }
    
    private void initGame() {
        board = new int[BOARD_HEIGHT][BOARD_WIDTH];
        for (int y = 0; y < BOARD_HEIGHT; y++) {
            for (int x = 0; x < BOARD_WIDTH; x++) {
                board[y][x] = -1;
            }
        }
        score = 0;
        level = 1;
        linesCleared = 0;
        fallDelay = 20;
        fallCounter = 0;
        gameOver = false;
        keyLeft = false;
        keyRight = false;
        keyDown = false;
        keyRepeatCounter = 0;
        nextType = random.nextInt(7);
        spawnPiece();
    }
    
    private void spawnPiece() {
        currentType = nextType;
        nextType = random.nextInt(7);
        currentPiece = copyPiece(TETROMINOES[currentType]);
        currentRotation = 0;
        currentX = BOARD_WIDTH / 2 - 2;
        currentY = 0;
        
        if (!isValidPosition(currentX, currentY)) {
            gameOver = true;
            if (score > highscore) {
                highscore = score;
            }
        }
    }
    
    private int[][] copyPiece(int[][] piece) {
        int[][] copy = new int[piece.length][2];
        for (int i = 0; i < piece.length; i++) {
            copy[i][0] = piece[i][0];
            copy[i][1] = piece[i][1];
        }
        return copy;
    }
    
    private boolean isValidPosition(int x, int y) {
        for (int[] block : currentPiece) {
            int bx = x + block[0];
            int by = y + block[1];
            if (bx < 0 || bx >= BOARD_WIDTH || by >= BOARD_HEIGHT) {
                return false;
            }
            if (by >= 0 && board[by][bx] != -1) {
                return false;
            }
        }
        return true;
    }
    
    private void lockPiece() {
        for (int[] block : currentPiece) {
            int bx = currentX + block[0];
            int by = currentY + block[1];
            if (by >= 0 && by < BOARD_HEIGHT && bx >= 0 && bx < BOARD_WIDTH) {
                board[by][bx] = currentType;
            }
        }
        clearLines();
        spawnPiece();
    }
    
    private void clearLines() {
        int lines = 0;
        for (int y = BOARD_HEIGHT - 1; y >= 0; y--) {
            boolean full = true;
            for (int x = 0; x < BOARD_WIDTH; x++) {
                if (board[y][x] == -1) {
                    full = false;
                    break;
                }
            }
            if (full) {
                lines++;
                // Shift down
                for (int yy = y; yy > 0; yy--) {
                    System.arraycopy(board[yy - 1], 0, board[yy], 0, BOARD_WIDTH);
                }
                for (int x = 0; x < BOARD_WIDTH; x++) {
                    board[0][x] = -1;
                }
                y++; // Check same row again
            }
        }
        if (lines > 0) {
            linesCleared += lines;
            // Scoring: 100, 300, 500, 800 for 1-4 lines
            int[] scores = {0, 100, 300, 500, 800};
            score += scores[Math.min(lines, 4)] * level;
            level = 1 + linesCleared / 10;
            fallDelay = Math.max(2, 20 - level * 2);
            ArcadeGame.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 0.8f, 1.0f + lines * 0.1f);
        }
    }
    
    private void rotatePiece() {
        // Find center of piece
        int[][] rotated = new int[currentPiece.length][2];
        int cx = 1, cy = 1;
        if (currentType == 0) { // I piece
            cx = 2;
            cy = 0;
        } else if (currentType == 1) { // O piece - no rotation
            return;
        }
        
        for (int i = 0; i < currentPiece.length; i++) {
            int x = currentPiece[i][0] - cx;
            int y = currentPiece[i][1] - cy;
            rotated[i][0] = -y + cx;
            rotated[i][1] = x + cy;
        }
        
        int[][] oldPiece = currentPiece;
        currentPiece = rotated;
        
        if (!isValidPosition(currentX, currentY)) {
            // Try wall kick
            if (isValidPosition(currentX - 1, currentY)) {
                currentX--;
            } else if (isValidPosition(currentX + 1, currentY)) {
                currentX++;
            } else if (isValidPosition(currentX - 2, currentY)) {
                currentX -= 2;
            } else if (isValidPosition(currentX + 2, currentY)) {
                currentX += 2;
            } else {
                currentPiece = oldPiece; // Revert
            }
        }
    }
    
    @Override
    @Environment(EnvType.CLIENT)
    public void update() {
        if (gameOver) return;
        
        // Handle key repeat for movement
        if (keyLeft || keyRight || keyDown) {
            keyRepeatCounter++;
            if (keyRepeatCounter >= 3) {
                keyRepeatCounter = 0;
                if (keyLeft && isValidPosition(currentX - 1, currentY)) {
                    currentX--;
                }
                if (keyRight && isValidPosition(currentX + 1, currentY)) {
                    currentX++;
                }
                if (keyDown && isValidPosition(currentX, currentY + 1)) {
                    currentY++;
                    score++;
                }
            }
        }
        
        // Natural fall
        fallCounter++;
        if (fallCounter >= fallDelay) {
            fallCounter = 0;
            if (isValidPosition(currentX, currentY + 1)) {
                currentY++;
            } else {
                lockPiece();
                ArcadeGame.playSound(SoundEvents.BLOCK_STONE_PLACE, 0.5f, 1.0f);
            }
        }
    }
    
    @Override
    @Environment(EnvType.CLIENT)
    public void drawBackground(MatrixStack matrices, int mouseX, int mouseY) {
        // Draw board border
        fill(matrices, BOARD_OFFSET_X - 2, BOARD_OFFSET_Y - 2,
             BOARD_OFFSET_X + BOARD_WIDTH * CELL_SIZE + 2, BOARD_OFFSET_Y + BOARD_HEIGHT * CELL_SIZE + 2,
             0xFF333333);
        fill(matrices, BOARD_OFFSET_X, BOARD_OFFSET_Y,
             BOARD_OFFSET_X + BOARD_WIDTH * CELL_SIZE, BOARD_OFFSET_Y + BOARD_HEIGHT * CELL_SIZE,
             0xFF111111);
        
        // Draw placed blocks
        for (int y = 0; y < BOARD_HEIGHT; y++) {
            for (int x = 0; x < BOARD_WIDTH; x++) {
                if (board[y][x] != -1) {
                    drawBlock(matrices, BOARD_OFFSET_X + x * CELL_SIZE, BOARD_OFFSET_Y + y * CELL_SIZE, COLORS[board[y][x]]);
                }
            }
        }
        
        // Draw current piece
        if (!gameOver) {
            for (int[] block : currentPiece) {
                int bx = currentX + block[0];
                int by = currentY + block[1];
                if (by >= 0) {
                    drawBlock(matrices, BOARD_OFFSET_X + bx * CELL_SIZE, BOARD_OFFSET_Y + by * CELL_SIZE, COLORS[currentType]);
                }
            }
            
            // Draw ghost piece (preview where piece will land)
            int ghostY = currentY;
            while (isValidPosition(currentX, ghostY + 1)) {
                ghostY++;
            }
            if (ghostY != currentY) {
                for (int[] block : currentPiece) {
                    int bx = currentX + block[0];
                    int by = ghostY + block[1];
                    if (by >= 0) {
                        int ghostColor = (COLORS[currentType] & 0xFFFFFF) | 0x44000000;
                        drawBlock(matrices, BOARD_OFFSET_X + bx * CELL_SIZE, BOARD_OFFSET_Y + by * CELL_SIZE, ghostColor);
                    }
                }
            }
        }
        
        // Draw next piece preview
        fill(matrices, 300, 30, 360, 90, 0xFF333333);
        fill(matrices, 302, 32, 358, 88, 0xFF111111);
        int[][] nextPiece = TETROMINOES[nextType];
        for (int[] block : nextPiece) {
            drawBlock(matrices, 310 + block[0] * CELL_SIZE, 40 + block[1] * CELL_SIZE, COLORS[nextType]);
        }
    }
    
    private void drawBlock(MatrixStack matrices, int x, int y, int color) {
        fill(matrices, x, y, x + CELL_SIZE, y + CELL_SIZE, color);
        // Highlight edge
        int light = brighten(color);
        fill(matrices, x, y, x + CELL_SIZE, y + 1, light);
        fill(matrices, x, y, x + 1, y + CELL_SIZE, light);
    }
    
    private int brighten(int color) {
        int a = (color >> 24) & 0xFF;
        int r = Math.min(255, ((color >> 16) & 0xFF) + 50);
        int g = Math.min(255, ((color >> 8) & 0xFF) + 50);
        int b = Math.min(255, (color & 0xFF) + 50);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }
    
    @Override
    @Environment(EnvType.CLIENT)
    public void drawForeground(MatrixStack matrices, TextRenderer textRenderer) {
        drawString(matrices, textRenderer, "Block Stacker", 50, 20, 0x404040);
        drawString(matrices, textRenderer, "Score: " + score, 50, 50, 0x404040);
        drawString(matrices, textRenderer, "High: " + highscore, 50, 62, 0x404040);
        drawString(matrices, textRenderer, "Level: " + level, 50, 80, 0x404040);
        drawString(matrices, textRenderer, "Lines: " + linesCleared, 50, 92, 0x404040);
        
        drawString(matrices, textRenderer, "Next:", 300, 20, 0x404040);
        
        drawString(matrices, textRenderer, "Controls:", 300, 100, 0x404040);
        drawString(matrices, textRenderer, "A/D - Move", 300, 114, 0x404040);
        drawString(matrices, textRenderer, "W - Rotate", 300, 126, 0x404040);
        drawString(matrices, textRenderer, "S - Soft drop", 300, 138, 0x404040);
        drawString(matrices, textRenderer, "Space - Hard drop", 300, 150, 0x404040);
        drawString(matrices, textRenderer, "R - Restart", 300, 168, 0x404040);
        
        if (gameOver) {
            drawCenteredString(matrices, textRenderer, "GAME OVER!", 220, 90, 0xAA0000);
            drawCenteredString(matrices, textRenderer, "Press R to restart", 220, 105, 0x404040);
        }
    }
    
    @Override
    @Environment(EnvType.CLIENT)
    public void keyPress(char character, int keyCode) {
        char lower = Character.toLowerCase(character);
        
        if (lower == 'r') {
            initGame();
            return;
        }
        
        if (gameOver) return;
        
        if (lower == 'a' || keyCode == GLFW.GLFW_KEY_A) {
            keyLeft = true;
            if (isValidPosition(currentX - 1, currentY)) {
                currentX--;
            }
        }
        if (lower == 'd' || keyCode == GLFW.GLFW_KEY_D) {
            keyRight = true;
            if (isValidPosition(currentX + 1, currentY)) {
                currentX++;
            }
        }
        if (lower == 's' || keyCode == GLFW.GLFW_KEY_S) {
            keyDown = true;
        }
        if (lower == 'w' || keyCode == GLFW.GLFW_KEY_W) {
            rotatePiece();
            ArcadeGame.playSound(SoundEvents.BLOCK_LEVER_CLICK, 0.5f, 1.2f);
        }
        if (keyCode == GLFW.GLFW_KEY_SPACE) {
            // Hard drop
            while (isValidPosition(currentX, currentY + 1)) {
                currentY++;
                score += 2;
            }
            lockPiece();
            ArcadeGame.playSound(SoundEvents.BLOCK_STONE_PLACE, 0.8f, 0.8f);
        }
    }
    
    @Environment(EnvType.CLIENT)
    public void keyRelease(int keyCode) {
        if (keyCode == GLFW.GLFW_KEY_A) {
            keyLeft = false;
        }
        if (keyCode == GLFW.GLFW_KEY_D) {
            keyRight = false;
        }
        if (keyCode == GLFW.GLFW_KEY_S) {
            keyDown = false;
        }
    }
    
    @Override
    public boolean disableStandardKeyFunctionality() {
        return true;
    }
    
    @Override
    public void save(NbtCompound nbt, int id) {
        nbt.putInt(generateNBTName("HighscoreTetris", id), highscore);
    }
    
    @Override
    public void load(NbtCompound nbt, int id) {
        highscore = nbt.getInt(generateNBTName("HighscoreTetris", id));
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
}
