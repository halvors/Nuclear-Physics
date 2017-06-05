package org.halvors.quantum.lib.utility.nbt;

import net.minecraft.nbt.NBTTagCompound;

public interface ISaveObject {
    void save(NBTTagCompound paramNBTTagCompound);

    void load(NBTTagCompound paramNBTTagCompound);
}
