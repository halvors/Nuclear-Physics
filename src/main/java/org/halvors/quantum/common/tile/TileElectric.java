package org.halvors.quantum.common.tile;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import org.halvors.quantum.common.utility.transform.vector.Vector3;

import java.util.EnumSet;

public class TileElectric extends TileQuantum implements IEnergyReceiver, IEnergyProvider {
    protected EnergyStorage energyStorage;

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);

        if (energyStorage != null) {
            energyStorage.readFromNBT(tagCompound);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        if (energyStorage != null) {
            energyStorage.writeToNBT(tagCompound);
        }

        return tagCompound;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean canConnectEnergy(EnumFacing from) {
        return getReceivingDirections().contains(from) || getExtractingDirections().contains(from);
    }

    @Override
    public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
        if (energyStorage != null && getReceivingDirections().contains(from)) {
            return energyStorage.receiveEnergy(maxReceive, simulate);
        }

        return 0;
    }

    @Override
    public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
        if (energyStorage != null && getExtractingDirections().contains(from)) {
            return energyStorage.extractEnergy(maxExtract, simulate);
        }

        return 0;
    }

    @Override
    public int getEnergyStored(EnumFacing from) {
        if (energyStorage != null) {
            return energyStorage.getEnergyStored();
        }

        return 0;
    }

    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        if (energyStorage != null) {
            return energyStorage.getMaxEnergyStored();
        }

        return 0;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public EnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public void setEnergyStorage(EnergyStorage energyStorage) {
        this.energyStorage = energyStorage;
    }

    public EnumSet<EnumFacing> getReceivingDirections() {
        return EnumSet.allOf(EnumFacing.class);
    }

    public EnumSet<EnumFacing> getExtractingDirections() {
        return EnumSet.noneOf(EnumFacing.class);
    }

    protected int produce() {
        int totalUsed = 0;

        // Send energy to available receivers.
        for (EnumFacing direction : getExtractingDirections()) {
            if (energyStorage.getEnergyStored() > 0) {
                TileEntity tileEntity = new Vector3(this).translate(direction).getTileEntity(world);

                if (tileEntity != null && tileEntity instanceof IEnergyReceiver) {
                    IEnergyReceiver tileReceiver = (IEnergyReceiver) tileEntity;
                    int used = extractEnergy(direction, tileReceiver.receiveEnergy(direction.getOpposite(), energyStorage.extractEnergy(energyStorage.getMaxExtract(), true), false), false);
                    totalUsed += energyStorage.extractEnergy(used, false);
                }
            }
        }

        return totalUsed;
    }

    /*
    // Recharges electric item.
    public void recharge(ItemStack itemStack) {
        energyStorage.extractEnergy(CompatibilityModule.chargeItem(itemStack, energyStorage.getEnergyStored(), true), true);
    }

    // Discharges electric item.
    public void discharge(ItemStack itemStack) {
        energyStorage.receiveEnergy(CompatibilityModule.dischargeItem(itemStack, energyStorage.getMaxEnergyStored() - energyStorage.getEnergyStored(), true), true);
    }
    */
}