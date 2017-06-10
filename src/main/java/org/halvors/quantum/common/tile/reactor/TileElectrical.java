package org.halvors.quantum.common.tile.reactor;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.transform.vector.Vector3;

import java.util.EnumSet;

public class TileElectrical extends TileEntity implements IEnergyHandler { // IEnergyInterface, IEnergyContainer
    protected EnergyStorage energyStorage;

    public TileElectrical() {

    }

    @Override
    public void updateEntity() {
        if (worldObj.getWorldTime() % 60 == 0) {
            Quantum.getLogger().info("Energy in storage is: " + energyStorage.getEnergyStored() + "/" + energyStorage.getMaxEnergyStored());
        }
    }

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
        //if (CompatibilityModule.isHandler(obj)) {
            if (from == null || from.equals(ForgeDirection.UNKNOWN)) {
                return false;
            }

            return getReceivingDirections().contains(from) || getExtractingDirections().contains(from);
        //}

        //return false;
    }

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
        if (energyStorage != null && (from == ForgeDirection.UNKNOWN || getReceivingDirections().contains(from))) {
            return energyStorage.receiveEnergy(maxReceive, simulate);
        }

        return 0;
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
        if (energyStorage != null && (from == ForgeDirection.UNKNOWN || getExtractingDirections().contains(from))) {
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

    protected int produce(int outputEnergy) {
        int usedEnergy = 0;

        for (ForgeDirection direction : getExtractingDirections()) {
            if (outputEnergy > 0) {
                TileEntity tileEntity = new Vector3(this).translate(direction).getTileEntity(worldObj);

                if (tileEntity != null) {
                    // TODO: Check this, should be correct.
                    // usedEnergy += CompatibilityModule.receiveEnergy(tileEntity, direction.getOpposite(), outputEnergy, true);
                    usedEnergy += receiveEnergy(direction.getOpposite(), outputEnergy, false);
                }
            }
        }

        return usedEnergy;
    }

    protected int produce() {
        int totalUsed = 0;

        for (ForgeDirection direction : getExtractingDirections()) {
            if (energyStorage.getEnergyStored() > 0) {
                TileEntity tileEntity = new Vector3(this).translate(direction).getTileEntity(this.worldObj);

                if (tileEntity != null) {
                    // TODO: Check this, should be correct.
                    //long used = CompatibilityModule.receiveEnergy(tileEntity, direction.getOpposite(), energyStorage.extractEnergy(energyStorage.getEnergy(), true), false);
                    int used = receiveEnergy(direction.getOpposite(), energyStorage.extractEnergy(energyStorage.getEnergyStored(), true), false);
                    totalUsed += energyStorage.extractEnergy(used, true);
                }
            }
        }

        Quantum.getLogger().info("Total produced: " + totalUsed);

        return totalUsed;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*
    // Recharges electric item.
    public void recharge(ItemStack itemStack) {
        if (this.getEnergyHandler() != null) {
            this.getEnergyHandler().extractEnergy(CompatibilityModule.chargeItem(itemStack, this.getEnergyHandler().getEnergy(), true), true);
        }
    }

    // Discharges electric item.
    public void discharge(ItemStack itemStack) {
        if (this.getEnergyHandler() != null) {
            this.getEnergyHandler().receiveEnergy(CompatibilityModule.dischargeItem(itemStack, this.getEnergyHandler().getEmptySpace(), true), true);
        }
    }

    @Override
    public void setEnergy(ForgeDirection from, long energy) {
        if (energyStorage != null)
            energyStorage.setEnergy(energy);
    }
    */
}