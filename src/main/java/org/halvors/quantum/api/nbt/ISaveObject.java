package org.halvors.quantum.api.nbt;

import net.minecraft.nbt.NBTTagCompound;

public interface ISaveObject {
    void save(NBTTagCompound tagCompound);

    void load(NBTTagCompound tagCompound);
}
