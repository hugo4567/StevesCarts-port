package vswe.stevescarts.arcade.invaders;

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

import java.util.ArrayList;
import java.util.Random;

/**
 * Ghast Invaders - A Space Invaders clone with Ghasts.
 */
public class ArcadeInvaders extends ArcadeGame {
    protected ArrayList<Unit> invaders;
    private ArrayList<Player> lives;
    private ArrayList<Unit> buildings;
    protected ArrayList<Projectile> projectiles;
    private Player player;
    protected int moveDirection;
    protected int moveSpeed;
    protected int moveDown;
    private int fireDelay;
    private int score;
    private int highscore;
    protected boolean hasPahighast;
    protected boolean canSpawnPahighast;
    private boolean newHighscore;
    private int gameoverCounter;
    
    private static final String[][] NUMBERS = {
        {"XXXX", "X  X", "X  X", "X  X", "X  X", "X  X", "XXXX"}, // 0
        {"   X", "   X", "   X", "   X", "   X", "   X", "   X"}, // 1
        {"XXXX", "   X", "   X", "XXXX", "X   ", "X   ", "XXXX"}, // 2
        {"XXXX", "   X", "   X", "XXXX", "   X", "   X", "XXXX"}, // 3
        {"X  X", "X  X", "X  X", "XXXX", "   X", "   X", "   X"}, // 4
        {"XXXX", "X   ", "X   ", "XXXX", "   X", "   X", "XXXX"}, // 5
        {"XXXX", "X   ", "X   ", "XXXX", "X  X", "X  X", "XXXX"}, // 6
        {"XXXX", "   X", "   X", "   X", "   X", "   X", "   X"}, // 7
        {"XXXX", "X  X", "X  X", "XXXX", "X  X", "X  X", "XXXX"}, // 8
        {"XXXX", "X  X", "X  X", "XXXX", "   X", "   X", "XXXX"}  // 9
    };
    
    // Key states for smooth movement
    private boolean keyLeft = false;
    private boolean keyRight = false;
    private boolean keyFire = false;
    
    public ArcadeInvaders(StevesArcadeModule module, String name) {
        super(module, name);
        invaders = new ArrayList<>();
        buildings = new ArrayList<>();
        lives = new ArrayList<>();
        projectiles = new ArrayList<>();
        start();
    }
    
    public Random getRandom() {
        return new Random();
    }
    
    private void start() {
        buildings.clear();
        lives.clear();
        projectiles.clear();
        player = new Player(this);
        for (int i = 0; i < 3; ++i) {
            lives.add(new Player(this, 10 + i * 20, 190));
        }
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 3; ++j) {
                buildings.add(new Building(this, 48 + i * 96 + j * 16, 120));
            }
        }
        moveSpeed = 0;
        fireDelay = 0;
        score = 0;
        canSpawnPahighast = false;
        newHighscore = false;
        spawnInvaders();
    }
    
    private void spawnInvaders() {
        invaders.clear();
        hasPahighast = false;
        for (int j = 0; j < 3; ++j) {
            for (int i = 0; i < 14; ++i) {
                invaders.add(new InvaderGhast(this, 20 + i * 20, 10 + 25 * j));
            }
        }
        ++moveSpeed;
        moveDirection = 1;
        moveDown = 0;
    }
    
    @Override
    @Environment(EnvType.CLIENT)
    public void update() {
        super.update();
        if (player != null) {
            if (player.ready) {
                boolean turnBack = false;
                boolean gameOver = false;
                for (int i = invaders.size() - 1; i >= 0; --i) {
                    Unit invader = invaders.get(i);
                    Unit.UpdateResult result = invader.update();
                    if (result == Unit.UpdateResult.DEAD) {
                        if (((InvaderGhast) invader).isPahighast) {
                            hasPahighast = false;
                        }
                        ArcadeGame.playSound(SoundEvents.ENTITY_GHAST_HURT, 0.15f, 1.0f);
                        invaders.remove(i);
                        ++score;
                    } else if (result == Unit.UpdateResult.TURN_BACK) {
                        turnBack = true;
                    } else if (result == Unit.UpdateResult.GAME_OVER) {
                        gameOver = true;
                    }
                }
                if (moveDown > 0) {
                    --moveDown;
                }
                if (turnBack) {
                    moveDirection *= -1;
                    moveDown = 5;
                }
                if (invaders.isEmpty() || (hasPahighast && invaders.size() == 1)) {
                    score += (hasPahighast ? 200 : 50);
                    canSpawnPahighast = true;
                    spawnInvaders();
                }
                if (gameOver) {
                    lives.clear();
                    projectiles.clear();
                    player = null;
                    newHighScore();
                    return;
                }
                for (int i = buildings.size() - 1; i >= 0; --i) {
                    if (buildings.get(i).update() == Unit.UpdateResult.DEAD) {
                        buildings.remove(i);
                    }
                }
                for (int i = projectiles.size() - 1; i >= 0; --i) {
                    if (projectiles.get(i).update() == Unit.UpdateResult.DEAD) {
                        projectiles.remove(i);
                    }
                }
                // Handle continuous key presses
                if (keyLeft) {
                    player.move(-1);
                } else if (keyRight) {
                    player.move(1);
                }
                if (fireDelay == 0 && keyFire) {
                    projectiles.add(new Projectile(this, player.getX() + 8 - 2, player.getY() - 15, true));
                    ArcadeGame.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 0.8f, 1.0f / (getRandom().nextFloat() * 0.4f + 1.2f) + 0.5f);
                    fireDelay = 10;
                } else if (fireDelay > 0) {
                    --fireDelay;
                }
            }
            if (player.update() == Unit.UpdateResult.DEAD) {
                projectiles.clear();
                ArcadeGame.playSound(SoundEvents.ENTITY_GENERIC_HURT, 1.0f, 1.0f);
                if (!lives.isEmpty()) {
                    lives.get(0).setTarget(player.getX(), player.getY());
                    player = lives.get(0);
                    lives.remove(0);
                } else {
                    player = null;
                    newHighScore();
                }
            }
        } else if (gameoverCounter == 0) {
            boolean flag = false;
            for (int j = invaders.size() - 1; j >= 0; --j) {
                Unit invader = invaders.get(j);
                if (invader.update() == Unit.UpdateResult.TARGET) {
                    flag = true;
                }
            }
            if (!flag) {
                gameoverCounter = 1;
            }
        } else if (newHighscore && gameoverCounter < 5) {
            ++gameoverCounter;
            if (gameoverCounter == 5) {
                ArcadeGame.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            }
        }
    }
    
    @Override
    @Environment(EnvType.CLIENT)
    public void drawBackground(MatrixStack matrices, int mouseX, int mouseY) {
        bindTexture("invaders");
        // Draw ground
        for (int i = 0; i < 27; ++i) {
            drawTexture(matrices, 5 + i * 16, 150, 16, 32, 16, 16);
        }
        for (int i = 0; i < 5; ++i) {
            drawTexture(matrices, 3 + i * 16, 190, 16, 32, 16, 16);
        }
        // Draw invaders
        for (Unit invader : invaders) {
            invader.draw(matrices);
        }
        // Draw player
        if (player != null) {
            player.draw(matrices);
        }
        // Draw extra lives
        for (Unit life : lives) {
            life.draw(matrices);
        }
        // Draw projectiles
        for (Unit projectile : projectiles) {
            projectile.draw(matrices);
        }
        // Draw buildings
        for (Unit building : buildings) {
            building.draw(matrices);
        }
    }
    
    @Override
    @Environment(EnvType.CLIENT)
    public void drawForeground(MatrixStack matrices, TextRenderer textRenderer) {
        drawString(matrices, textRenderer, "Extra Lives:", 10, 180, 0x404040);
        drawString(matrices, textRenderer, "High Score: " + highscore, 10, 210, 0x404040);
        drawString(matrices, textRenderer, "Score: " + score, 10, 220, 0x404040);
        drawString(matrices, textRenderer, "W - Shoot", 330, 180, 0x404040);
        drawString(matrices, textRenderer, "A - Left", 330, 190, 0x404040);
        drawString(matrices, textRenderer, "D - Right", 330, 200, 0x404040);
        drawString(matrices, textRenderer, "R - Restart", 330, 220, 0x404040);
    }
    
    @Override
    @Environment(EnvType.CLIENT)
    public void keyPress(char character, int keyCode) {
        char lower = Character.toLowerCase(character);
        if (lower == 'r') {
            start();
        }
        // Track key states
        if (lower == 'a' || keyCode == GLFW.GLFW_KEY_A) {
            keyLeft = true;
        }
        if (lower == 'd' || keyCode == GLFW.GLFW_KEY_D) {
            keyRight = true;
        }
        if (lower == 'w' || keyCode == GLFW.GLFW_KEY_W) {
            keyFire = true;
        }
    }
    
    /**
     * Handle key release events.
     */
    @Environment(EnvType.CLIENT)
    public void keyRelease(int keyCode) {
        if (keyCode == GLFW.GLFW_KEY_A) {
            keyLeft = false;
        }
        if (keyCode == GLFW.GLFW_KEY_D) {
            keyRight = false;
        }
        if (keyCode == GLFW.GLFW_KEY_W) {
            keyFire = false;
        }
    }
    
    private void newHighScore() {
        buildings.clear();
        int digits = (score == 0) ? 1 : (int) Math.floor(Math.log10(score)) + 1;
        canSpawnPahighast = false;
        int currentGhast = 0;
        for (int i = 0; i < digits; ++i) {
            int digit = score / (int) Math.pow(10.0, digits - i - 1) % 10;
            String[] number = NUMBERS[digit];
            for (int j = 0; j < number.length; ++j) {
                String line = number[j];
                for (int k = 0; k < line.length(); ++k) {
                    if (line.charAt(k) == 'X') {
                        int x = (443 - (digits * 90 - 10)) / 2 + i * 90 + k * 20;
                        int y = 5 + j * 20;
                        InvaderGhast ghast;
                        if (currentGhast >= invaders.size()) {
                            invaders.add(ghast = new InvaderGhast(this, x, -20));
                            ++currentGhast;
                        } else {
                            ghast = (InvaderGhast) invaders.get(currentGhast++);
                        }
                        ghast.setTarget(x, y);
                    }
                }
            }
        }
        for (int i = currentGhast; i < invaders.size(); ++i) {
            InvaderGhast ghast = (InvaderGhast) invaders.get(i);
            ghast.setTarget(ghast.getX(), -25);
        }
        gameoverCounter = 0;
        if (score > highscore) {
            newHighscore = true;
            highscore = score;
        }
    }
    
    @Override
    public void receivePacket(int id, byte[] data, PlayerEntity player) {
        if (id == 2) {
            short data2 = data[0];
            short data3 = data[1];
            if (data2 < 0) data2 += 256;
            if (data3 < 0) data3 += 256;
            highscore = (data2 | data3 << 8);
        }
    }
    
    @Override
    public void save(NbtCompound nbt, int id) {
        nbt.putShort(generateNBTName("HighscoreGhast", id), (short) highscore);
    }
    
    @Override
    public void load(NbtCompound nbt, int id) {
        highscore = nbt.getShort(generateNBTName("HighscoreGhast", id));
    }
    
    @Override
    public boolean disableStandardKeyFunctionality() {
        return true;
    }
}
