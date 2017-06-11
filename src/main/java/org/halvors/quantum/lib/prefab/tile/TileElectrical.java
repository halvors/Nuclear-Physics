package org.halvors.quantum.lib.prefab.tile;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.quantum.common.transform.vector.Vector3;
import org.halvors.quantum.lib.tile.TileIO;

public class TileElectrical extends TileIO implements IEnergyHandler { //IEnergyInterface, IEnergyContainer {
    protected EnergyStorage energyStorage;

    //protected EnergyStorageHandler energy;

    public TileElectrical() {
        super(null);
    }

    public TileElectrical(Material material) {
        super(material);
    }

    /** Recharges electric item. */
    public void recharge(ItemStack itemStack) {
        if (this.getEnergyHandler() != null)
        {
            // TODO: Fix this.
            //this.getEnergyHandler().extractEnergy(CompatibilityModule.chargeItem(itemStack, this.getEnergyHandler().getEnergy(), true), true);
        }
    }

    /** Discharges electric item. */
    public void discharge(ItemStack itemStack) {
        if (this.getEnergyHandler() != null)
        {
            // TODO: Fix this.
            //this.getEnergyHandler().receiveEnergy(CompatibilityModule.dischargeItem(itemStack, this.getEnergyHandler().getEmptySpace(), true), true);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        if (this.getEnergyHandler() != null) {
            this.getEnergyHandler().readFromNBT(nbt);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);

        if (this.getEnergyHandler() != null) {
            this.getEnergyHandler().writeToNBT(nbt);
        }
    }

    protected long produce(long outputEnergy) {
        long usedEnergy = 0;

        for (ForgeDirection direction : this.getOutputDirections()) {
            if (outputEnergy > 0) {
                TileEntity tileEntity = new Vector3(this).translate(direction).getTileEntity(this.worldObj);

                if (tileEntity != null) {
                    // TODO: Fix this.
                    //usedEnergy += CompatibilityModule.receiveEnergy(tileEntity, direction.getOpposite(), outputEnergy, true);
                }
            }
        }

        return usedEnergy;
    }

    protected long produce() {
        long totalUsed = 0;

        for (ForgeDirection direction : this.getOutputDirections()) {
            if (this.getEnergyHandler().getEnergyStored() > 0) {
                TileEntity tileEntity = new Vector3(this).translate(direction).getTileEntity(this.worldObj);

                if (tileEntity != null) {
                    // TODO: Fix this.'
                    //long used = CompatibilityModule.receiveEnergy(tileEntity, direction.getOpposite(), getEnergyHandler().extractEnergy(getEnergyHandler().getEnergy(), false), true);
                    //totalUsed += this.getEnergyHandler().extractEnergy(used, true);
                }
            }
        }

        return totalUsed;
    }

    public EnergyStorage getEnergyHandler()
    {
        return energyStorage;
    }

    public void setEnergyHandler(EnergyStorage energyStorage)
    {
        this.energyStorage = energyStorage;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean canConnectEnergy(ForgeDirection from) {
        //if (CompatibilityModule.isHandler(obj)) {
        if (from != null || !from.equals(ForgeDirection.UNKNOWN)) {
            return getInputDirections().contains(from) || getOutputDirections().contains(from);
        }
        //}

        return false;
    }

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
        if (getEnergyHandler() != null && (from == ForgeDirection.UNKNOWN || getInputDirections().contains(from))) {
            return getEnergyHandler().receiveEnergy(maxReceive, simulate);
        }

        return 0;
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
        if (getEnergyHandler() != null && (from == ForgeDirection.UNKNOWN || getOutputDirections().contains(from))) {
            return getEnergyHandler().extractEnergy(maxExtract, simulate);
        }

        return 0;
    }

    @Override
    public int getEnergyStored(ForgeDirection from) {
        if (getEnergyHandler() != null) {
            return getEnergyHandler().getEnergyStored();
        }

        return 0;
    }

    public void setEnergy(ForgeDirection from, int energy) {
        if (getEnergyHandler() != null) {
            getEnergyHandler().setEnergyStored(energy);
        }
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from) {
        if (getEnergyHandler() != null) {
            return getEnergyHandler().getMaxEnergyStored();
        }

        return 0;
    }
}