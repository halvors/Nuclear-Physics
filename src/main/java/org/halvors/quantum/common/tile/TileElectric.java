package org.halvors.quantum.common.tile;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.quantum.common.transform.vector.Vector3;

import java.util.EnumSet;

public class TileElectric extends TileQuantum implements IEnergyHandler {
    protected EnergyStorage energyStorage;

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        if (energyStorage != null) {
            energyStorage.readFromNBT(nbt);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);

        if (energyStorage != null) {
            energyStorage.writeToNBT(nbt);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean canConnectEnergy(ForgeDirection from) {
        return getReceivingDirections().contains(from) || getExtractingDirections().contains(from);
    }

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
        if (energyStorage != null && getReceivingDirections().contains(from)) {
            return energyStorage.receiveEnergy(maxReceive, simulate);
        }

        return 0;
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
        if (energyStorage != null && getExtractingDirections().contains(from)) {
            return energyStorage.extractEnergy(maxExtract, simulate);
        }

        return 0;
    }

    @Override
    public int getEnergyStored(ForgeDirection from) {
        if (energyStorage != null) {
            return energyStorage.getEnergyStored();
        }

        return 0;
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from) {
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

    public EnumSet<ForgeDirection> getReceivingDirections() {
        EnumSet<ForgeDirection> directions = EnumSet.allOf(ForgeDirection.class);
        directions.remove(ForgeDirection.UNKNOWN);

        return directions;
    }

    public EnumSet<ForgeDirection> getExtractingDirections() {
        return EnumSet.noneOf(ForgeDirection.class);
    }

    protected int produce() {
        int totalUsed = 0;

        // Send energy to available receivers.
        for (ForgeDirection direction : getExtractingDirections()) {
            if (energyStorage.getEnergyStored() > 0) {
                TileEntity tileEntity = new Vector3(this).translate(direction).getTileEntity(worldObj);

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