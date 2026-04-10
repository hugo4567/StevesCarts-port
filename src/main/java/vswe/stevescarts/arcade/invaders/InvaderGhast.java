package vswe.stevescarts.arcade.invaders;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;
import vswe.stevescarts.arcade.ArcadeGame;

import java.util.Random;

/**
 * Ghast enemy in the Invaders game.
 */
public class InvaderGhast extends Unit {
    private int tentacleTextureId;
    private int shooting;
    protected boolean isPahighast;
    private boolean hasTarget;
    private int targetX;
    private int targetY;
    
    public InvaderGhast(ArcadeInvaders game, int x, int y) {
        super(game, x, y);
        tentacleTextureId = game.getRandom().nextInt(4);
        shooting = -10;
        if (game.canSpawnPahighast && !game.hasPahighast && game.getRandom().nextInt(1000) == 0) {
            isPahighast = true;
            game.hasPahighast = true;
        }
    }
    
    @Override
    @Environment(EnvType.CLIENT)
    public void draw(MatrixStack matrices) {
        if (isPahighast) {
            game.drawImageInArea(matrices, x, y, 32, 32, 16, 16);
        } else {
            game.drawImageInArea(matrices, x, y, (shooting > -10) ? 16 : 0, 0, 16, 16);
        }
        game.drawImageInArea(matrices, x, y + 16, 0, 16 + 8 * tentacleTextureId, 16, 8);
    }
    
    @Override
    public UpdateResult update() {
        if (hasTarget) {
            boolean flag = false;
            if (x != targetX) {
                if (x > targetX) {
                    x = Math.max(targetX, x - 4);
                } else {
                    x = Math.min(targetX, x + 4);
                }
                flag = true;
            }
            if (y != targetY) {
                if (y > targetY) {
                    y = Math.max(targetY, y - 4);
                } else {
                    y = Math.min(targetY, y + 4);
                }
                flag = true;
            }
            return flag ? UpdateResult.TARGET : UpdateResult.DONE;
        }
        if (super.update() == UpdateResult.DEAD) {
            return UpdateResult.DEAD;
        }
        if (shooting > -10) {
            if (shooting == 0) {
                Random random = game.getRandom();
                ArcadeGame.playSound(SoundEvents.ENTITY_GHAST_SHOOT, 0.1f, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
                game.projectiles.add(new Projectile(game, x + 8 - 3, y + 8 - 3, false));
            }
            --shooting;
        }
        if (game.moveDown > 0) {
            ++y;
        } else {
            x += game.moveDirection * game.moveSpeed;
            if (y > 130) {
                return UpdateResult.GAME_OVER;
            }
            if (x > 417 || x < 10) {
                return UpdateResult.TURN_BACK;
            }
        }
        if (!isPahighast && shooting == -10 && game.getRandom().nextInt(300) == 0) {
            shooting = 10;
        }
        return UpdateResult.DONE;
    }
    
    @Override
    protected int getHitboxWidth() {
        return 16;
    }
    
    @Override
    protected int getHitboxHeight() {
        return 24;
    }
    
    public void setTarget(int x, int y) {
        hasTarget = true;
        targetX = x;
        targetY = y;
    }
}
