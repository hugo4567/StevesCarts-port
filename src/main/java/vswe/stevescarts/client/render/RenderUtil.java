package vswe.stevescarts.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.Identifier;

/**
 * Utilitaire de rendu pour les fluides et autres Ã©lÃ©ments graphiques
 */
public class RenderUtil {

    /**
     * Renderise un tank de fluide dans l'interface graphique
     */
    public static void renderGuiTank(Fluid fluid, int maxCapacity, int amount, int x, int y, double zLevel, int width, int height) {
        if (fluid == null || amount <= 0) {
            return;
        }        MinecraftClient minecraft = MinecraftClient.getInstance();
        SpriteAtlasTexture textureMap = minecraft.getBakedModelManager().getAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
        Sprite fluidSprite = null;
        
        try {
            // En 1.18.2, nous devons passer par une autre méthode pour obtenir le sprite de fluide
            Identifier resourceId = fluid.getDefaultState().getBlockState().getBlock().getRegistryEntry().registryKey().getValue();
            fluidSprite = minecraft.getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(resourceId);
        } catch (Exception e) {
            // Si la méthode échoue, utilisez un sprite par défaut
            fluidSprite = minecraft.getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
        }
        
        if (fluidSprite == null) {
            return;
        }
        
        int fluidColor = 0xFFFFFFFF; // Couleur blanche par défaut
        float scale = (float) amount / (float) maxCapacity;
        
        RenderSystem.setShaderTexture(0, SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        int fluidHeight = (int) (height * scale);
        int yOffset = y + height - fluidHeight;

        for (int i = 0; i < width; i += 16) {
            for (int j = 0; j < fluidHeight; j += 16) {
                int drawWidth = Math.min(width - i, 16);
                int drawHeight = Math.min(fluidHeight - j, 16);

                int drawX = x + i;
                int drawY = yOffset + j;

                float minU = fluidSprite.getMinU();
                float maxU = fluidSprite.getMaxU();
                float minV = fluidSprite.getMinV();
                float maxV = fluidSprite.getMaxV();

                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferBuilder = tessellator.getBuffer();
                bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
                
                bufferBuilder.vertex(drawX, drawY + drawHeight, zLevel).color(fluidColor).texture(minU, maxV).next();
                bufferBuilder.vertex(drawX + drawWidth, drawY + drawHeight, zLevel).color(fluidColor).texture(maxU, maxV).next();
                bufferBuilder.vertex(drawX + drawWidth, drawY, zLevel).color(fluidColor).texture(maxU, minV).next();
                bufferBuilder.vertex(drawX, drawY, zLevel).color(fluidColor).texture(minU, minV).next();
                
                tessellator.draw();
            }
        }
    }
}
