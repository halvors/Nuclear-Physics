package org.halvors.quantum.common.item;

import net.minecraft.nbt.NBTTagCompound;

public interface ISaveObject {
    void save(NBTTagCompound tagCompound);

    void load(NBTTagCompound tagCompound);
}
