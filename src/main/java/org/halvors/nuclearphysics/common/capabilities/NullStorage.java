package org.halvors.nuclearphysics.common.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class NullStorage<T> implements IStorage<T> {
    @Override
    public NBTBase writeNBT(final Capability<T> capability, final T instance, final EnumFacing side) {
        return new NBTTagCompound();
    }

    @Override
    public void readNBT(final Capability<T> capability, final T instance, final EnumFacing side, final NBTBase nbt) {

    }
}
