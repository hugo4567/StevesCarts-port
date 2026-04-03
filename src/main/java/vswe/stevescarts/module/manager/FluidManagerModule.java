package vswe.stevescarts.module.manager;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.NbtCompound;
import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.Configurable;
import vswe.stevescarts.module.ModuleType;
import vswe.stevescarts.screen.CartHandler;
import vswe.stevescarts.util.FluidValue;
import vswe.stevescarts.util.TextHelper;

/**
 * Fluid Manager Module - Manages fluid distribution
 * Controls distribution of fluids from tanks to adjacent containers
 */
public class FluidManagerModule extends CartModule implements Configurable {
    private int mode = 0;  // 0=input, 1=output, 2=balanced
    private Fluid targetFluid = Fluids.WATER;
    private int transferredAmount = 0;
    private int checkInterval = 15;
    private int lastCheckTick = 0;

    public FluidManagerModule(CartEntity minecart, ModuleType<?> type) {
        super(minecart, type);
    }

    @Override
    public void tick() {
        if (getEntity() == null) return;
        
        lastCheckTick++;
        if (lastCheckTick >= checkInterval) {
            lastCheckTick = 0;
            manageFluidDistribution();
        }
    }

    private void manageFluidDistribution() {
        // This would handle fluid transfers between tanks and containers
        // based on the selected mode
    }

    @Override
    public void configure(WPlainPanel panel, CartHandler handler, PlayerEntity player) {
        WLabel title = new WLabel(TextHelper.literal("Fluid Manager"));
        panel.add(title, 0, 0);
        
        String modeText = switch(mode) {
            case 0 -> "Input";
            case 1 -> "Output";
            case 2 -> "Balanced";
            default -> "Unknown";
        };
        
        WLabel modeLabel = new WLabel(TextHelper.literal("Mode: " + modeText));
        panel.add(modeLabel, 0, 12);
        
        WLabel transferLabel = new WLabel(TextHelper.literal("Transferred: " + (transferredAmount / 1000) + "k mB"));
        panel.add(transferLabel, 70, 12);
        
        panel.setSize(160, 30);
    }

    public void setMode(int newMode) {
        mode = newMode % 3;
    }

    public void setTargetFluid(Fluid fluid) {
        targetFluid = fluid;
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        super.writeToNbt(nbt);
        nbt.putInt("Mode", mode);
        nbt.putString("TargetFluid", targetFluid.getDefaultState().toString());
        nbt.putInt("TransferredAmount", transferredAmount);
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        super.readFromNbt(nbt);
        mode = nbt.getInt("Mode");
        transferredAmount = nbt.getInt("TransferredAmount");
    }
}