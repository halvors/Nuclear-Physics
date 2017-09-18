package org.halvors.nuclearphysics.common.capabilities.energy;

import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;

public class EnergyStorage extends net.minecraftforge.energy.EnergyStorage implements ITeslaHolder, ITeslaConsumer, ITeslaProducer {
    public EnergyStorage(int capacity) {
        super(capacity, capacity, capacity);
    }

    public EnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public EnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public void setEnergyStored(int energy) {
        this.energy = energy;
    }

    @Override
    public long givePower(long power, boolean simulated) {
        return receiveEnergy(Math.toIntExact(power), simulated);
    }

    @Override
    public long getStoredPower() {
        return energy;
    }

    @Override
    public long getCapacity() {
        return capacity;
    }

    @Override
    public long takePower(long power, boolean simulated) {
        return extractEnergy(Math.toIntExact(power), simulated);
    }
}
