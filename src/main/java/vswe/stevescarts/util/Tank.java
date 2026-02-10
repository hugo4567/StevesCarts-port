package vswe.stevescarts.util;

import java.util.Collections;
import java.util.Iterator;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.registry.Registry;

/**
 * Une classe Tank personnalisée compatible avec Minecraft 1.18.2
 * Cette classe est basée sur la classe Tank de RebornCore mais adaptée pour notre usage
 * avec l'API Fabric Transfer.
 */
public class Tank {
    private String name;
    private int capacity;
    private int amount;
    private Fluid fluid;

    public Tank(String name, int capacity, Fluid fluid) {
        this.name = name;
        this.capacity = capacity;
        this.fluid = fluid;
        this.amount = 0;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getAmount() {
        return amount;
    }

    public Fluid getFluid() {
        return fluid;
    }

    public boolean isEmpty() {
        return fluid == null || fluid == Fluids.EMPTY || amount <= 0;
    }

    public void setFluid(Fluid fluid, int amount) {
        this.fluid = fluid;
        this.amount = Math.min(amount, capacity);
    }
    
    // Méthodes compatibles avec l'API Fabric Transfer
    public long insert(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        if (resource == null || resource.isBlank() || maxAmount <= 0) {
            return 0;
        }
        
        Fluid resourceFluid = resource.getFluid();
        int amountToInsert = (int) Math.min(maxAmount, Integer.MAX_VALUE);
        
        int inserted = fill(resourceFluid, amountToInsert, false);
        
        if (inserted > 0) {
            transaction.addCloseCallback((tx, result) -> {
                if (result.wasCommitted()) {
                    fill(resourceFluid, inserted, true);
                }
            });
        }
        
        return inserted;
    }
    
    public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        if (resource == null || resource.isBlank() || maxAmount <= 0 || isEmpty()) {
            return 0;
        }
        
        if (resource.getFluid() != fluid) {
            return 0;
        }
        
        int amountToExtract = (int) Math.min(maxAmount, Integer.MAX_VALUE);
        int extracted = drain(amountToExtract, false);
        
        if (extracted > 0) {
            transaction.addCloseCallback((tx, result) -> {
                if (result.wasCommitted()) {
                    drain(extracted, true);
                }
            });
        }
        
        return extracted;
    }
    
    public long simulateInsert(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        if (resource == null || resource.isBlank() || maxAmount <= 0) {
            return 0;
        }
        
        Fluid resourceFluid = resource.getFluid();
        int amountToInsert = (int) Math.min(maxAmount, Integer.MAX_VALUE);
        
        return fill(resourceFluid, amountToInsert, false);
    }
    
    public long simulateExtract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        if (resource == null || resource.isBlank() || maxAmount <= 0 || isEmpty()) {
            return 0;
        }
        
        if (resource.getFluid() != fluid) {
            return 0;
        }
        
        int amountToExtract = (int) Math.min(maxAmount, Integer.MAX_VALUE);
        return drain(amountToExtract, false);
    }
    
    public Iterator<StorageView<FluidVariant>> iterator() {
        if (isEmpty()) {
            return Collections.emptyIterator();
        }
        
        return Collections.<StorageView<FluidVariant>>singletonList(
            new StorageView<FluidVariant>() {
                @Override
                public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
                    return Tank.this.extract(resource, maxAmount, transaction);
                }

                @Override
                public boolean isResourceBlank() {
                    return Tank.this.isEmpty();
                }

                @Override
                public FluidVariant getResource() {
                    return Tank.this.isEmpty() ? FluidVariant.blank() : FluidVariant.of(Tank.this.getFluid());
                }

                @Override
                public long getAmount() {
                    return Tank.this.getAmount();
                }

                @Override
                public long getCapacity() {
                    return Tank.this.getCapacity();
                }
            }
        ).iterator();
    }

    public int fill(Fluid fluid, int amount, boolean doFill) {
        if (fluid == null || fluid == Fluids.EMPTY || amount <= 0) {
            return 0;
        }

        if (!isEmpty() && this.fluid != fluid) {
            return 0;
        }

        int filled = Math.min(capacity - this.amount, amount);
        if (doFill && filled > 0) {
            this.fluid = fluid;
            this.amount += filled;
        }
        return filled;
    }

    public int drain(int amount, boolean doDrain) {
        if (isEmpty() || amount <= 0) {
            return 0;
        }

        int drained = Math.min(this.amount, amount);
        if (doDrain && drained > 0) {
            this.amount -= drained;
            if (this.amount <= 0) {
                this.fluid = Fluids.EMPTY;
            }
        }
        return drained;
    }

    public void write(NbtCompound nbt) {
        NbtCompound tankTag = new NbtCompound();
        if (!isEmpty()) {
            tankTag.putString("FluidName", Registry.FLUID.getId(fluid).toString());
            tankTag.putInt("Amount", amount);
        }
        nbt.put("Tank", tankTag);
    }

    public NbtCompound write(NbtCompound nbt, String key) {
        NbtCompound tankTag = new NbtCompound();
        if (!isEmpty()) {
            tankTag.putString("FluidName", Registry.FLUID.getId(fluid).toString());
            tankTag.putInt("Amount", amount);
        }
        nbt.put(key, tankTag);
        return nbt;
    }

    public void read(NbtCompound nbt) {
        if (nbt == null || !nbt.contains("Tank")) {
            clear();
            return;
        }

        NbtCompound tankTag = nbt.getCompound("Tank");
        if (tankTag.contains("FluidName") && tankTag.contains("Amount")) {
            String fluidName = tankTag.getString("FluidName");
            fluid = Registry.FLUID.get(new net.minecraft.util.Identifier(fluidName));
            amount = tankTag.getInt("Amount");
        } else {
            clear();
        }
    }

    public void clear() {
        this.fluid = Fluids.EMPTY;
        this.amount = 0;
    }

    public String getName() {
        return name;
    }
}
