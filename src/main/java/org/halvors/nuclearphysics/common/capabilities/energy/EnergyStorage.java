package org.halvors.nuclearphysics.common.capabilities.energy;

import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;
import org.halvors.nuclearphysics.common.ConfigurationManager.General;

public class EnergyStorage extends net.minecraftforge.energy.EnergyStorage implements ITeslaHolder, ITeslaConsumer, ITeslaProducer {
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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public long givePower(long power, boolean simulated) {
        return receiveEnergy((int) (Math.toIntExact(power) * General.fromTesla), simulated);
    }

    @Override
    public long getStoredPower() {
        return (long) (energy * General.toTesla);
    }

    @Override
    public long getCapacity() {
        return (long) (capacity * General.toTesla);
    }

    @Override
    public long takePower(long power, boolean simulated) {
        return extractEnergy((int) (Math.toIntExact(power) * General.fromTesla), simulated);
    }
}
