package vswe.stevescarts.compat.minecraft;

import vswe.stevescarts.compat.CompatHelpers;
import vswe.stevescarts.compat.ICompatPlugin;

/**
 * Vanilla Minecraft compatibility.
 * Provides default tree and crop handling for vanilla Minecraft.
 * This is loaded last and acts as a fallback for unrecognized blocks.
 */
public class CompatMinecraft implements ICompatPlugin {

    @Override
    public void loadAddons(CompatHelpers helpers) {
        helpers.registerTree(new DefaultTreeModule());
        helpers.registerCrop(new DefaultCropModule());
    }
}
