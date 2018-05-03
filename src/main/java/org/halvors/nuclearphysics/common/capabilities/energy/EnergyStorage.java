package org.halvors.nuclearphysics.common.capabilities.energy;

public class EnergyStorage extends net.minecraftforge.energy.EnergyStorage {
    public EnergyStorage(final int capacity) {
        super(capacity, capacity, capacity);
    }

    public EnergyStorage(final int capacity, final int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public EnergyStorage(final int capacity, final int maxReceive, final int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public void setEnergyStored(final int energy) {
        this.energy = energy;
    }
}
