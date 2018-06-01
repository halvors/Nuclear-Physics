package org.halvors.nuclearphysics.common.science;

import org.halvors.nuclearphysics.common.storage.nbt.data.ChunkDataStorage;

public class ThermalEnergyStorage extends ChunkDataStorage {
    private static final ThermalEnergyStorage instance = new ThermalEnergyStorage();

    public ThermalEnergyStorage() {
        super("ThermalEnergy");
    }

    public static ThermalEnergyStorage getInstance() {
        return instance;
    }
}
