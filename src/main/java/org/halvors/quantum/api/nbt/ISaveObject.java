package org.halvors.quantum.api.nbt;

import net.minecraft.nbt.NBTTagCompound;

public interface ISaveObject {
    NBTTagCompound save(NBTTagCompound tag);

    void load(NBTTagCompound tag);
}
