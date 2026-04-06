package vswe.stevescarts.util;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * Classe utilitaire pour faciliter la compatibilité entre les différentes versions de Minecraft
 * Cette classe fournit des méthodes pour créer des textes traduisibles
 */
public class TextHelper {

    /**
     * Crée un Text littéral à partir d'une String
     */
    public static MutableText literal(String text) {
        return Text.literal(text);
    }
    
    /**
     * Crée un Text traduisible à partir d'une clé de traduction
     */
    public static Text translatable(String key) {
        return Text.translatable(key);
    }
    
    /**
     * Crée un Text traduisible avec un argument entier
     */
    public static Text translatable(String key, int value) {
        return Text.translatable(key, value);
    }
    
    /**
     * Crée un Text traduisible avec un argument long
     */
    public static Text translatable(String key, long value) {
        return Text.translatable(key, value);
    }
    
    /**
     * Crée un Text traduisible avec un argument String
     */
    public static Text translatable(String key, String value) {
        return Text.translatable(key, value);
    }
    
    /**
     * Crée un Text traduisible avec un argument Text
     */
    public static Text translatable(String key, Text text) {
        return Text.translatable(key, text);
    }
    
    /**
     * Crée un Text traduisible avec deux arguments Text
     */
    public static Text translatable(String key, Text text1, Text text2) {
        return Text.translatable(key, text1, text2);
    }
    
    /**
     * Crée un Text traduisible avec trois arguments Text
     */
    public static Text translatable(String key, Text text1, Text text2, Text text3) {
        return Text.translatable(key, text1, text2, text3);
    }
    
    /**
     * Crée un Text traduisible avec un argument Text et un entier
     */
    public static Text translatable(String key, Text text, int value) {
        return Text.translatable(key, text, value);
    }
    
    /**
     * Crée un Text vide
     */
    public static MutableText empty() {
        return Text.empty();
    }
    
    /**
     * Convertit un Text en MutableText
     */
    public static MutableText asMutable(Text text) {
        return Text.literal("").append(text);
    }
    
    /**
     * Méthode pour ajouter du texte à un Text (équivalent à append en 1.19)
     */
    public static MutableText append(Text base, String text) {
        return asMutable(base).append(text);
    }
    
    /**
     * Méthode pour ajouter un autre Text à un Text (équivalent à append en 1.19)
     */
    public static MutableText append(Text base, Text text) {
        return asMutable(base).append(text);
    }
    
    /**
     * Méthode pour formater un Text (équivalent à formatted en 1.19)
     */
    public static MutableText formatted(Text text, Formatting formatting) {
        return asMutable(text).formatted(formatting);
    }
}
