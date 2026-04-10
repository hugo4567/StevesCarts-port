package vswe.stevescarts.arcade.invaders;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;

/**
 * Base class for all units in the Ghast Invaders game.
 */
public abstract class Unit {
    protected int x;
    protected int y;
    protected ArcadeInvaders game;
    protected boolean dead;
    protected int health;
    
    public Unit(ArcadeInvaders game, int x, int y) {
        this.x = x;
        this.y = y;
        this.game = game;
        this.health = 1;
    }
    
    @Environment(EnvType.CLIENT)
    public abstract void draw(MatrixStack matrices);
    
    public UpdateResult update() {
        if (!dead) {
            hitCalculation();
        }
        return dead ? UpdateResult.DEAD : UpdateResult.DONE;
    }
    
    protected void hitCalculation() {
        for (Projectile projectile : game.projectiles) {
            if (!projectile.dead && (isObstacle() || projectile.playerProjectile != isPlayer()) && collidesWith(projectile)) {
                --health;
                if (health == 0) {
                    dead = true;
                }
                projectile.dead = true;
            }
        }
    }
    
    protected boolean collidesWith(Unit unit) {
        return isUnitAinUnitB(this, unit) || isUnitAinUnitB(unit, this);
    }
    
    private boolean isUnitAinUnitB(Unit a, Unit b) {
        return ((a.x >= b.x && a.x <= b.x + b.getHitboxWidth()) || 
                (a.x + a.getHitboxWidth() >= b.x && a.x + a.getHitboxWidth() <= b.x + b.getHitboxWidth())) &&
               ((a.y >= b.y && a.y <= b.y + b.getHitboxHeight()) || 
                (a.y + a.getHitboxHeight() >= b.y && a.y + a.getHitboxHeight() <= b.y + b.getHitboxHeight()));
    }
    
    protected boolean isPlayer() {
        return false;
    }
    
    protected boolean isObstacle() {
        return false;
    }
    
    protected abstract int getHitboxWidth();
    protected abstract int getHitboxHeight();
    
    public int getX() { return x; }
    public int getY() { return y; }
    
    public enum UpdateResult {
        DONE,
        TURN_BACK,
        DEAD,
        GAME_OVER,
        TARGET
    }
}
