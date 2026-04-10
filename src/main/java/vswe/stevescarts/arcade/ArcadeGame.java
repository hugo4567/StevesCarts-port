package vswe.stevescarts.arcade;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.module.addon.StevesArcadeModule;

/**
 * Base class for all arcade mini-games in Steve's Carts.
 * Ported from Forge 1.12 to Fabric 1.18.2.
 */
public abstract class ArcadeGame {
    protected final StevesArcadeModule module;
    protected final String name;
    
    // Game area bounds (matching original)
    public static final int GAME_AREA_X = 5;
    public static final int GAME_AREA_Y = 4;
    public static final int GAME_AREA_WIDTH = 443;
    public static final int GAME_AREA_HEIGHT = 168;
    
    public ArcadeGame(StevesArcadeModule module, String name) {
        this.module = module;
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public StevesArcadeModule getModule() {
        return module;
    }
    
    /**
     * Called every tick to update game logic.
     */
    @Environment(EnvType.CLIENT)
    public void update() {
        // Override in subclasses
    }
    
    /**
     * Draw background elements (game board, sprites).
     */
    @Environment(EnvType.CLIENT)
    public void drawBackground(MatrixStack matrices, int mouseX, int mouseY) {
        // Override in subclasses
    }
    
    /**
     * Draw foreground elements (text, UI overlays).
     */
    @Environment(EnvType.CLIENT)
    public void drawForeground(MatrixStack matrices, TextRenderer textRenderer) {
        // Override in subclasses
    }
    
    /**
     * Draw mouse-over tooltips.
     */
    @Environment(EnvType.CLIENT)
    public void drawMouseOver(MatrixStack matrices, int mouseX, int mouseY) {
        // Override in subclasses
    }
    
    /**
     * Handle mouse click events.
     */
    @Environment(EnvType.CLIENT)
    public void mouseClicked(int x, int y, int button) {
        // Override in subclasses
    }
    
    /**
     * Handle mouse release events.
     */
    @Environment(EnvType.CLIENT)
    public void mouseReleased(int x, int y, int button) {
        // Override in subclasses
    }
    
    /**
     * Handle key press events.
     */
    @Environment(EnvType.CLIENT)
    public void keyPress(char character, int keyCode) {
        // Override in subclasses
    }
    
    /**
     * Save game state to NBT.
     */
    public void save(NbtCompound nbt, int id) {
        // Override in subclasses
    }
    
    /**
     * Load game state from NBT.
     */
    public void load(NbtCompound nbt, int id) {
        // Override in subclasses
    }
    
    /**
     * Receive network packet data.
     */
    public void receivePacket(int id, byte[] data, PlayerEntity player) {
        // Override in subclasses
    }
    
    /**
     * Called when the game is loaded into the GUI.
     */
    @Environment(EnvType.CLIENT)
    public void onLoad() {
        // Override in subclasses
    }
    
    /**
     * Called when the game is unloaded from the GUI.
     */
    @Environment(EnvType.CLIENT)
    public void onUnload() {
        // Override in subclasses
    }
    
    /**
     * Whether to disable standard key functionality (like closing GUI with E).
     */
    public boolean disableStandardKeyFunctionality() {
        return false;
    }
    
    /**
     * Whether the game allows key repeat.
     */
    public boolean allowKeyRepeat() {
        return false;
    }
    
    // ==================== Rendering Utilities ====================
    
    /**
     * Bind a texture resource for rendering.
     */
    @Environment(EnvType.CLIENT)
    protected void bindTexture(String texturePath) {
        Identifier texture = StevesCarts.id("textures/gui/" + texturePath + ".png");
        RenderSystem.setShaderTexture(0, texture);
    }
    
    /**
     * Draw a textured rectangle.
     */
    @Environment(EnvType.CLIENT)
    public void drawTexture(MatrixStack matrices, int x, int y, int u, int v, int width, int height) {
        DrawableHelper.drawTexture(matrices, x, y, u, v, width, height, 256, 256);
    }
    
    /**
     * Draw a textured rectangle with custom texture size.
     */
    @Environment(EnvType.CLIENT)
    public void drawTexture(MatrixStack matrices, int x, int y, int u, int v, int width, int height, int texWidth, int texHeight) {
        DrawableHelper.drawTexture(matrices, x, y, u, v, width, height, texWidth, texHeight);
    }
    
    /**
     * Draw an image clipped to the game area.
     */
    @Environment(EnvType.CLIENT)
    public void drawImageInArea(MatrixStack matrices, int x, int y, int u, int v, int w, int h) {
        drawImageInArea(matrices, x, y, u, v, w, h, GAME_AREA_X, GAME_AREA_Y, 
                       GAME_AREA_X + GAME_AREA_WIDTH, GAME_AREA_Y + GAME_AREA_HEIGHT);
    }
    
    /**
     * Draw an image clipped to a specific area.
     */
    @Environment(EnvType.CLIENT)
    public void drawImageInArea(MatrixStack matrices, int x, int y, int u, int v, int w, int h,
                                   int x1, int y1, int x2, int y2) {
        // Clip left
        if (x < x1) {
            w -= (x1 - x);
            u += (x1 - x);
            x = x1;
        } else if (x + w > x2) {
            w = x2 - x;
        }
        
        // Clip top
        if (y < y1) {
            h -= (y1 - y);
            v += (y1 - y);
            y = y1;
        } else if (y + h > y2) {
            h = y2 - y;
        }
        
        if (w > 0 && h > 0) {
            drawTexture(matrices, x, y, u, v, w, h);
        }
    }
    
    /**
     * Draw a filled rectangle.
     */
    @Environment(EnvType.CLIENT)
    protected void fill(MatrixStack matrices, int x1, int y1, int x2, int y2, int color) {
        DrawableHelper.fill(matrices, x1, y1, x2, y2, color);
    }
    
    /**
     * Draw text at a position.
     */
    @Environment(EnvType.CLIENT)
    protected void drawString(MatrixStack matrices, TextRenderer textRenderer, String text, int x, int y, int color) {
        textRenderer.draw(matrices, text, x, y, color);
    }
    
    /**
     * Draw centered text.
     */
    @Environment(EnvType.CLIENT)
    protected void drawCenteredString(MatrixStack matrices, TextRenderer textRenderer, String text, int x, int y, int color) {
        textRenderer.draw(matrices, text, x - textRenderer.getWidth(text) / 2f, y, color);
    }
    
    /**
     * Play a sound effect.
     */
    @Environment(EnvType.CLIENT)
    public static void playSound(SoundEvent sound, float volume, float pitch) {
        if (sound != null) {
            MinecraftClient client = MinecraftClient.getInstance();
            client.getSoundManager().play(PositionedSoundInstance.master(sound, pitch, volume));
        }
    }
    
    /**
     * Check if coordinates are within a rectangle.
     */
    protected boolean inRect(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }
    
    /**
     * Check if coordinates are within a rectangle defined by an array [x, y, width, height].
     */
    protected boolean inRect(int mouseX, int mouseY, int[] rect) {
        return inRect(mouseX, mouseY, rect[0], rect[1], rect[2], rect[3]);
    }
    
    /**
     * Generate a unique NBT key name.
     */
    protected String generateNBTName(String base, int id) {
        return base + "_" + id;
    }
}
