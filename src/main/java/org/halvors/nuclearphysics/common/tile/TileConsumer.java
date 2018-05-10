package org.halvors.nuclearphysics.common.tile;

import cofh.api.energy.IEnergyReceiver;
import cofh.api.energy.IEnergyStorage;
import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.common.Optional.Method;
import ic2.api.energy.tile.IEnergySink;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.nuclearphysics.common.ConfigurationManager.General;
import org.halvors.nuclearphysics.common.Integration;
import org.halvors.nuclearphysics.common.capabilities.energy.EnergyStorage;

import java.util.List;

@Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = Integration.IC2_ID)
public class TileConsumer extends TileRotatable implements IEnergyReceiver, IEnergySink {
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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    @Method(modid = Integration.IC2_ID)
    public double getDemandedEnergy() {
        return (energyStorage.getMaxEnergyStored() - energyStorage.getEnergyStored()) * General.toIC2;
    }

    @Override
    @Method(modid = Integration.IC2_ID)
    public int getSinkTier() {
        return 4;
    }

    @Override
    @Method(modid = Integration.IC2_ID)
    public double injectEnergy(ForgeDirection from, double amount, double voltage) {
        return receiveEnergy(from, (int) (amount * General.fromIC2), false);
    }

    @Override
    @Method(modid = Integration.IC2_ID)
    public boolean acceptsEnergyFrom(TileEntity tile, ForgeDirection from) {
        return canConnectEnergy(from);
    }
}
