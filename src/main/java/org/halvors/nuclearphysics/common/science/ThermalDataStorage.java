package org.halvors.nuclearphysics.common.science;

import org.halvors.nuclearphysics.common.storage.nbt.chunk.ChunkDataStorage;

public class ThermalDataStorage extends ChunkDataStorage {
    private static final ThermalDataStorage instance = new ThermalDataStorage();

    public ThermalDataStorage() {
        super("thermal");
    }

    public static ThermalDataStorage getInstance() {
        return instance;
    }
}
