package org.halvors.nuclearphysics.common.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class NullStorage<T> implements Capability.IStorage<T> {
    @Override
    public NBTBase writeNBT(Capability<T> capability, T instance, EnumFacing side) {
        return new NBTTagCompound();
    }

    @Override
    public void readNBT(Capability<T> capability, T instance, EnumFacing side, NBTBase nbt) {

    }
}
