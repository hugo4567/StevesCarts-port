package vswe.stevescarts.arcade.invaders;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;

/**
 * Defensive building/barrier in the Invaders game.
 */
public class Building extends Unit {
    
    public Building(ArcadeInvaders game, int x, int y) {
        super(game, x, y);
        health = 10;
    }
    
    @Override
    @Environment(EnvType.CLIENT)
    public void draw(MatrixStack matrices) {
        game.drawTexture(matrices, x, y, 32 + (10 - health) * 16, 16, 16, 16);
    }
    
    @Override
    protected int getHitboxWidth() {
        return 16;
    }
    
    @Override
    protected int getHitboxHeight() {
        return 16;
    }
    
    @Override
    protected boolean isObstacle() {
        return true;
    }
    
    @Override
    public UpdateResult update() {
        if (super.update() == UpdateResult.DEAD) {
            return UpdateResult.DEAD;
        }
        for (Unit invader : game.invaders) {
            if (!invader.dead && collidesWith(invader)) {
                dead = true;
                health = 0;
                return UpdateResult.DEAD;
            }
        }
        return UpdateResult.DONE;
    }
}
