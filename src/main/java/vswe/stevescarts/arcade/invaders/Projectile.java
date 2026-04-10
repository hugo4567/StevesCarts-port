package vswe.stevescarts.arcade.invaders;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;

/**
 * Projectile (bullet) in the Invaders game.
 */
public class Projectile extends Unit {
    protected boolean playerProjectile;
    
    public Projectile(ArcadeInvaders game, int x, int y, boolean playerProjectile) {
        super(game, x, y);
        this.playerProjectile = playerProjectile;
    }
    
    @Override
    @Environment(EnvType.CLIENT)
    public void draw(MatrixStack matrices) {
        if (playerProjectile) {
            game.drawTexture(matrices, x, y, 38, 0, 5, 16);
        } else {
            game.drawTexture(matrices, x, y, 32, 0, 6, 6);
        }
    }
    
    @Override
    protected void hitCalculation() {
        // Projectiles don't get hit by other projectiles
    }
    
    @Override
    public UpdateResult update() {
        if (super.update() == UpdateResult.DEAD) {
            return UpdateResult.DEAD;
        }
        y += (playerProjectile ? -5 : 5);
        if (y < 0 || y > 168) {
            dead = true;
            return UpdateResult.DEAD;
        }
        return UpdateResult.DONE;
    }
    
    @Override
    protected int getHitboxWidth() {
        return playerProjectile ? 5 : 6;
    }
    
    @Override
    protected int getHitboxHeight() {
        return playerProjectile ? 16 : 6;
    }
}
