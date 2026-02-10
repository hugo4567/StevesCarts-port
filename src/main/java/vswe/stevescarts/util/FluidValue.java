package vswe.stevescarts.util;

/**
 * Une classe FluidValue personnalisÃ©e compatible avec Minecraft 1.18.2
 * Cette classe est basÃ©e sur la classe FluidValue de RebornCore mais adaptÃ©e pour notre usage
 */
public class FluidValue {
    private final int value;
    
    // Constante pour un seau (1000 mB)
    public static final FluidValue BUCKET = new FluidValue(1000);

    private FluidValue(int value) {
        this.value = value;
    }

    public static FluidValue fromMillibuckets(int millibuckets) {
        return new FluidValue(millibuckets);
    }

    public static FluidValue fromRaw(int raw) {
        return new FluidValue(raw);
    }

    public int getRawValue() {
        return value;
    }

    public int toMillibuckets() {
        return value;
    }

    @Override
    public String toString() {
        return value + " mB";
    }

    public FluidValue copy() {
        return new FluidValue(value);
    }
    
    public FluidValue multiply(int factor) {
        return new FluidValue(this.value * factor);
    }
}
