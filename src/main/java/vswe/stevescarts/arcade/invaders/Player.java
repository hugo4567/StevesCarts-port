package vswe.stevescarts.arcade.invaders;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;

/**
 * The player's cart in Ghast Invaders.
 */
public class Player extends Unit {
    protected boolean ready;
    private int targetX;
    private int targetY;
    
    public Player(ArcadeInvaders game, int x, int y) {
        super(game, x, y);
    }
    
    public Player(ArcadeInvaders game) {
        this(game, 200, 150);
        ready = true;
    }
    
    @Override
    @Environment(EnvType.CLIENT)
    public void draw(MatrixStack matrices) {
        if (ready || targetY == y) {
            game.drawImageInArea(matrices, x, y, 16, 16, 16, 16);
        } else {
            game.drawImageInArea(matrices, x, y, 16, 16, 16, 16, 3, 0, 1000, 1000);
        }
    }
    
    protected void setTarget(int x, int y) {
        targetX = x;
        targetY = y;
    }
    
    @Override
    public UpdateResult update() {
        if (!ready) {
            if (targetY == y && targetX == x) {
                ready = true;
            } else if (targetY == y) {
                x = Math.min(targetX, x + 8);
            } else if (x == -15) {
                y = Math.max(targetY, y - 8);
            } else {
                x = Math.max(-15, x - 8);
            }
        } else if (super.update() == UpdateResult.DEAD) {
            return UpdateResult.DEAD;
        }
        return UpdateResult.DONE;
    }
    
    public void move(int dir) {
        x += dir * 5;
        if (x < 10) {
            x = 10;
        } else if (x > 417) {
            x = 417;
        }
    }
    
    @Override
    protected boolean isPlayer() {
        return true;
    }
    
    @Override
    protected int getHitboxWidth() {
        return 16;
    }
    
    @Override
    protected int getHitboxHeight() {
        return 16;
    }
}
