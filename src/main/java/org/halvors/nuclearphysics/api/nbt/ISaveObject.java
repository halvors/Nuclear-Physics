package org.halvors.nuclearphysics.api.nbt;

import net.minecraft.nbt.CompoundNBT;

public interface ISaveObject {
    void read(final CompoundNBT compound);

    CompoundNBT write(final CompoundNBT compound);
}
