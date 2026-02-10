package vswe.stevescarts.util;

/**
 * Interface pour remplacer FloatSupplier de Minecraft 1.19
 * qui n'est pas disponible dans Minecraft 1.18.2
 */
@FunctionalInterface
public interface FloatSupplier {
    float getAsFloat();
}
