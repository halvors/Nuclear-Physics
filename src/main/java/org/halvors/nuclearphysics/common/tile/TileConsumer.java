package org.halvors.nuclearphysics.common.tile;

import cofh.api.energy.IEnergyReceiver;
import cofh.api.energy.IEnergyStorage;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.nuclearphysics.common.capabilities.energy.EnergyStorage;

import java.util.List;

public class TileConsumer extends TileRotatable implements IEnergyReceiver {
    protected EnergyStorage energyStorage;

    public TileConsumer() {

    }

    @Override
    public void readFromNBT(final NBTTagCompound tag) {
        super.readFromNBT(tag);

        if (energyStorage != null) {
            energyStorage.readFromNBT(tag);
        }
    }

    @Override
    public void writeToNBT(final NBTTagCompound tag) {
        super.writeToNBT(tag);

        if (energyStorage != null) {
            energyStorage.writeToNBT(tag);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handlePacketData(final ByteBuf dataStream) {
        super.handlePacketData(dataStream);

        if (worldObj.isRemote) {
            energyStorage.setEnergyStored(dataStream.readInt());
        }
    }

    @Override
    public List<Object> getPacketData(final List<Object> objects) {
        super.getPacketData(objects);

        objects.add(energyStorage.getEnergyStored());

        return objects;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public int receiveEnergy(final ForgeDirection from, final int maxReceive, final boolean simulate) {
        return energyStorage.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int getEnergyStored(final ForgeDirection from) {
        return energyStorage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(final ForgeDirection from) {
        return energyStorage.getMaxEnergyStored();
    }

    @Override
    public boolean canConnectEnergy(final ForgeDirection from) {
        return true;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public IEnergyStorage getEnergyStorage() {
        return energyStorage;
    }
}
