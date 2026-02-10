package vswe.stevescarts.util;

/**
 * Interface pour remplacer LidOpenable de Minecraft 1.19
 * qui n'est pas disponible dans Minecraft 1.18.2
 */
public interface LidOpenable {
    float getAnimationProgress(float tickDelta);
    boolean isOpen();
}
