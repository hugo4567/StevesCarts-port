package vswe.stevescarts.arcade.monopoly;

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
 * Mob Monopoly - A simplified Monopoly-style board game.
 * Single player vs CPU opponent, collect resources on a Minecraft-themed board.
 */
public class ArcadeMonopoly extends ArcadeGame {
    private static final int BOARD_SIZE = 20; // Spaces on the board
    private static final int CELL_SIZE = 20;
    private static final int BOARD_OFFSET_X = 80;
    private static final int BOARD_OFFSET_Y = 30;
    
    // Property types
    private static final int TYPE_START = 0;
    private static final int TYPE_VILLAGE = 1;
    private static final int TYPE_MINE = 2;
    private static final int TYPE_FARM = 3;
    private static final int TYPE_FORTRESS = 4;
    private static final int TYPE_CHANCE = 5;
    private static final int TYPE_TAX = 6;
    private static final int TYPE_CHEST = 7;
    
    private Space[] board;
    private int playerPosition;
    private int cpuPosition;
    private int playerMoney;
    private int cpuMoney;
    private int highscore;
    private int turnCount;
    private boolean isPlayerTurn;
    private boolean gameOver;
    private boolean waitingForRoll;
    private int lastRoll;
    private String statusMessage;
    private int cpuThinkTimer;
    private Random random;
    
    // Button coordinates
    private static final int[] ROLL_BUTTON = {350, 140, 60, 20};
    private static final int[] BUY_BUTTON = {350, 165, 60, 20};
    
    private boolean canBuy;
    
    public ArcadeMonopoly(StevesArcadeModule module, String name) {
        super(module, name);
        random = new Random();
        initGame();
    }
    
    private void initGame() {
        board = new Space[BOARD_SIZE];
        
        // Define board spaces (simplified Minecraft theme)
        board[0] = new Space("START", TYPE_START, 0, 0);
        board[1] = new Space("Village", TYPE_VILLAGE, 60, 0x8B4513);
        board[2] = new Space("Chance", TYPE_CHANCE, 0, 0xFFFF00);
        board[3] = new Space("Mine", TYPE_MINE, 80, 0x808080);
        board[4] = new Space("Farm", TYPE_FARM, 80, 0x00AA00);
        board[5] = new Space("Tax", TYPE_TAX, 0, 0xFF0000);
        board[6] = new Space("Village", TYPE_VILLAGE, 100, 0xCD853F);
        board[7] = new Space("Chest", TYPE_CHEST, 0, 0xFFD700);
        board[8] = new Space("Mine", TYPE_MINE, 120, 0xA9A9A9);
        board[9] = new Space("Farm", TYPE_FARM, 120, 0x228B22);
        board[10] = new Space("Fortress", TYPE_FORTRESS, 150, 0x8B0000);
        board[11] = new Space("Chance", TYPE_CHANCE, 0, 0xFFFF00);
        board[12] = new Space("Village", TYPE_VILLAGE, 140, 0xDEB887);
        board[13] = new Space("Mine", TYPE_MINE, 160, 0xC0C0C0);
        board[14] = new Space("Tax", TYPE_TAX, 0, 0xFF0000);
        board[15] = new Space("Farm", TYPE_FARM, 160, 0x32CD32);
        board[16] = new Space("Chest", TYPE_CHEST, 0, 0xFFD700);
        board[17] = new Space("Village", TYPE_VILLAGE, 180, 0xF4A460);
        board[18] = new Space("Chance", TYPE_CHANCE, 0, 0xFFFF00);
        board[19] = new Space("Fortress", TYPE_FORTRESS, 200, 0xB22222);
        
        playerPosition = 0;
        cpuPosition = 0;
        playerMoney = 500;
        cpuMoney = 500;
        turnCount = 0;
        isPlayerTurn = true;
        gameOver = false;
        waitingForRoll = true;
        lastRoll = 0;
        canBuy = false;
        statusMessage = "Your turn! Click Roll to start.";
        cpuThinkTimer = 0;
    }
    
    @Override
    @Environment(EnvType.CLIENT)
    public void update() {
        if (gameOver) return;
        
        // CPU turn logic
        if (!isPlayerTurn) {
            cpuThinkTimer++;
            if (cpuThinkTimer >= 40) { // CPU "thinks" for 2 seconds
                cpuThinkTimer = 0;
                if (waitingForRoll) {
                    rollDice(false);
                } else if (canBuy) {
                    cpuDecideBuy();
                } else {
                    endTurn();
                }
            }
        }
        
        // Check win conditions
        if (cpuMoney <= 0) {
            gameOver = true;
            statusMessage = "YOU WIN! CPU is bankrupt!";
            if (playerMoney > highscore) {
                highscore = playerMoney;
            }
            ArcadeGame.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        } else if (playerMoney <= 0) {
            gameOver = true;
            statusMessage = "GAME OVER! You are bankrupt!";
        }
    }
    
    private void rollDice(boolean isPlayer) {
        lastRoll = random.nextInt(6) + 1 + random.nextInt(6) + 1; // 2d6
        
        if (isPlayer) {
            playerPosition = (playerPosition + lastRoll) % BOARD_SIZE;
            // Passing start bonus
            if (playerPosition < lastRoll && playerPosition != 0) {
                playerMoney += 100;
                statusMessage = "Passed START! +100 coins. Rolled " + lastRoll;
            } else {
                statusMessage = "Rolled " + lastRoll + "!";
            }
            handleLanding(true);
        } else {
            cpuPosition = (cpuPosition + lastRoll) % BOARD_SIZE;
            if (cpuPosition < lastRoll && cpuPosition != 0) {
                cpuMoney += 100;
            }
            statusMessage = "CPU rolled " + lastRoll;
            handleLanding(false);
        }
        
        waitingForRoll = false;
        ArcadeGame.playSound(SoundEvents.BLOCK_NOTE_BLOCK_HAT, 0.8f, 1.0f);
    }
    
    private void handleLanding(boolean isPlayer) {
        int pos = isPlayer ? playerPosition : cpuPosition;
        Space space = board[pos];
        
        switch (space.type) {
            case TYPE_START:
                statusMessage += " - Safe on START!";
                canBuy = false;
                break;
            case TYPE_VILLAGE:
            case TYPE_MINE:
            case TYPE_FARM:
            case TYPE_FORTRESS:
                if (space.ownedByPlayer == 0 && space.cost > 0) {
                    canBuy = true;
                    statusMessage += " - " + space.name + " for sale: " + space.cost;
                } else if (space.ownedByPlayer == 1 && !isPlayer) {
                    int rent = space.cost / 4;
                    cpuMoney -= rent;
                    playerMoney += rent;
                    statusMessage = "CPU pays you " + rent + " rent!";
                    canBuy = false;
                } else if (space.ownedByPlayer == 2 && isPlayer) {
                    int rent = space.cost / 4;
                    playerMoney -= rent;
                    cpuMoney += rent;
                    statusMessage = "You pay CPU " + rent + " rent!";
                    canBuy = false;
                } else {
                    canBuy = false;
                }
                break;
            case TYPE_CHANCE:
                int chanceAmount = (random.nextInt(5) + 1) * 20;
                if (random.nextBoolean()) {
                    if (isPlayer) playerMoney += chanceAmount;
                    else cpuMoney += chanceAmount;
                    statusMessage = (isPlayer ? "You" : "CPU") + " found " + chanceAmount + " coins!";
                } else {
                    if (isPlayer) playerMoney -= chanceAmount;
                    else cpuMoney -= chanceAmount;
                    statusMessage = (isPlayer ? "You" : "CPU") + " lost " + chanceAmount + " coins!";
                }
                canBuy = false;
                break;
            case TYPE_TAX:
                int tax = 50;
                if (isPlayer) playerMoney -= tax;
                else cpuMoney -= tax;
                statusMessage = (isPlayer ? "You" : "CPU") + " paid " + tax + " tax!";
                canBuy = false;
                break;
            case TYPE_CHEST:
                int bonus = 75;
                if (isPlayer) playerMoney += bonus;
                else cpuMoney += bonus;
                statusMessage = (isPlayer ? "You" : "CPU") + " found a treasure chest! +" + bonus;
                canBuy = false;
                ArcadeGame.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 0.8f, 1.0f);
                break;
        }
    }
    
    private void buyProperty(boolean isPlayer) {
        int pos = isPlayer ? playerPosition : cpuPosition;
        Space space = board[pos];
        
        if (space.ownedByPlayer == 0 && space.cost > 0) {
            if (isPlayer && playerMoney >= space.cost) {
                playerMoney -= space.cost;
                space.ownedByPlayer = 1;
                statusMessage = "You bought " + space.name + "!";
                ArcadeGame.playSound(SoundEvents.ENTITY_VILLAGER_YES, 0.8f, 1.0f);
            } else if (!isPlayer && cpuMoney >= space.cost) {
                cpuMoney -= space.cost;
                space.ownedByPlayer = 2;
                statusMessage = "CPU bought " + space.name + "!";
            }
        }
        canBuy = false;
    }
    
    private void cpuDecideBuy() {
        Space space = board[cpuPosition];
        // Simple AI: buy if can afford and random chance
        if (space.ownedByPlayer == 0 && cpuMoney >= space.cost && random.nextFloat() > 0.3f) {
            buyProperty(false);
        } else {
            canBuy = false;
            statusMessage = "CPU passed on buying.";
        }
    }
    
    private void endTurn() {
        isPlayerTurn = !isPlayerTurn;
        waitingForRoll = true;
        canBuy = false;
        turnCount++;
        if (isPlayerTurn) {
            statusMessage = "Your turn! Click Roll.";
        } else {
            statusMessage = "CPU's turn...";
        }
    }
    
    @Override
    @Environment(EnvType.CLIENT)
    public void drawBackground(MatrixStack matrices, int mouseX, int mouseY) {
        // Draw board (rectangular path)
        for (int i = 0; i < BOARD_SIZE; i++) {
            int[] pos = getBoardPosition(i);
            int x = pos[0];
            int y = pos[1];
            
            Space space = board[i];
            int bgColor = 0xFF334433;
            if (space.ownedByPlayer == 1) bgColor = 0xFF4444AA;
            else if (space.ownedByPlayer == 2) bgColor = 0xFFAA4444;
            else if (space.color != 0) bgColor = 0xFF000000 | space.color;
            
            fill(matrices, x, y, x + CELL_SIZE, y + CELL_SIZE, bgColor);
            fill(matrices, x + 1, y + 1, x + CELL_SIZE - 1, y + CELL_SIZE - 1, brighten(bgColor));
            
            // Draw player marker
            if (playerPosition == i) {
                fill(matrices, x + 3, y + 3, x + 10, y + 10, 0xFF0000FF);
            }
            // Draw CPU marker
            if (cpuPosition == i) {
                fill(matrices, x + 10, y + 10, x + 17, y + 17, 0xFFFF0000);
            }
        }
        
        // Draw buttons
        int rollColor = (isPlayerTurn && waitingForRoll && !gameOver) ? 0xFF44AA44 : 0xFF666666;
        fill(matrices, ROLL_BUTTON[0], ROLL_BUTTON[1], ROLL_BUTTON[0] + ROLL_BUTTON[2], ROLL_BUTTON[1] + ROLL_BUTTON[3], rollColor);
        
        int buyColor = (isPlayerTurn && canBuy && !waitingForRoll && !gameOver) ? 0xFF4444AA : 0xFF666666;
        fill(matrices, BUY_BUTTON[0], BUY_BUTTON[1], BUY_BUTTON[0] + BUY_BUTTON[2], BUY_BUTTON[1] + BUY_BUTTON[3], buyColor);
    }
    
    private int brighten(int color) {
        int r = Math.min(255, ((color >> 16) & 0xFF) + 30);
        int g = Math.min(255, ((color >> 8) & 0xFF) + 30);
        int b = Math.min(255, (color & 0xFF) + 30);
        return 0xFF000000 | (r << 16) | (g << 8) | b;
    }
    
    private int[] getBoardPosition(int index) {
        // Create a rectangular board path
        int x, y;
        if (index < 6) { // Bottom edge (left to right)
            x = BOARD_OFFSET_X + index * (CELL_SIZE + 2);
            y = BOARD_OFFSET_Y + 120;
        } else if (index < 10) { // Right edge (bottom to top)
            x = BOARD_OFFSET_X + 5 * (CELL_SIZE + 2);
            y = BOARD_OFFSET_Y + 120 - (index - 5) * (CELL_SIZE + 2);
        } else if (index < 16) { // Top edge (right to left)
            x = BOARD_OFFSET_X + (15 - index) * (CELL_SIZE + 2);
            y = BOARD_OFFSET_Y;
        } else { // Left edge (top to bottom)
            x = BOARD_OFFSET_X;
            y = BOARD_OFFSET_Y + (index - 15) * (CELL_SIZE + 2);
        }
        return new int[]{x, y};
    }
    
    @Override
    @Environment(EnvType.CLIENT)
    public void drawForeground(MatrixStack matrices, TextRenderer textRenderer) {
        drawString(matrices, textRenderer, "Mob Monopoly", 170, 5, 0x404040);
        
        // Player info
        drawString(matrices, textRenderer, "You (Blue): " + playerMoney + " coins", 250, 30, 0x0000AA);
        drawString(matrices, textRenderer, "CPU (Red): " + cpuMoney + " coins", 250, 45, 0xAA0000);
        drawString(matrices, textRenderer, "Turn: " + turnCount, 250, 65, 0x404040);
        drawString(matrices, textRenderer, "High Score: " + highscore, 250, 80, 0x404040);
        
        // Status message
        drawString(matrices, textRenderer, statusMessage, 80, 180, 0x404040);
        
        // Last roll
        if (lastRoll > 0) {
            drawString(matrices, textRenderer, "Last Roll: " + lastRoll, 250, 100, 0x404040);
        }
        
        // Buttons
        drawCenteredString(matrices, textRenderer, "Roll", ROLL_BUTTON[0] + 30, ROLL_BUTTON[1] + 6, 0xFFFFFF);
        drawCenteredString(matrices, textRenderer, "Buy", BUY_BUTTON[0] + 30, BUY_BUTTON[1] + 6, 0xFFFFFF);
        
        // Current space info
        Space currentSpace = board[playerPosition];
        drawString(matrices, textRenderer, "On: " + currentSpace.name, 250, 120, 0x404040);
        
        // Controls
        drawString(matrices, textRenderer, "R - Restart", 350, 200, 0x404040);
        
        if (gameOver) {
            drawCenteredString(matrices, textRenderer, statusMessage, 220, 90, playerMoney > cpuMoney ? 0x00AA00 : 0xAA0000);
        }
    }
    
    @Override
    @Environment(EnvType.CLIENT)
    public void mouseClicked(int x, int y, int button) {
        if (gameOver || !isPlayerTurn) return;
        
        if (button == 0) {
            if (inRect(x, y, ROLL_BUTTON) && waitingForRoll) {
                rollDice(true);
            } else if (inRect(x, y, BUY_BUTTON) && canBuy && !waitingForRoll) {
                buyProperty(true);
                endTurn();
            } else if (!waitingForRoll && !canBuy) {
                endTurn();
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
        nbt.putInt(generateNBTName("HighscoreMonopoly", id), highscore);
    }
    
    @Override
    public void load(NbtCompound nbt, int id) {
        highscore = nbt.getInt(generateNBTName("HighscoreMonopoly", id));
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
     * Represents a space on the board.
     */
    private static class Space {
        String name;
        int type;
        int cost;
        int color;
        int ownedByPlayer; // 0 = unowned, 1 = player, 2 = CPU
        
        Space(String name, int type, int cost, int color) {
            this.name = name;
            this.type = type;
            this.cost = cost;
            this.color = color;
            this.ownedByPlayer = 0;
        }
    }
}
