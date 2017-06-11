package org.halvors.quantum.common.tile.reactor;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.quantum.Quantum;

public class TileElectricTurbine extends TileEntity implements IEnergyHandler {
    private final EnergyStorage energyStorage = new EnergyStorage(32000, 32000);

    public TileElectricTurbine() {

    }

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            Quantum.getLogger().info("Energy stored: " + energyStorage.getEnergyStored());

            // Send energy to available receivers.
            for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
                if (canConnectEnergy(direction)) {
                    TileEntity tileReceiver = worldObj.getTileEntity(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);

                    if (tileReceiver instanceof IEnergyReceiver) {
                        IEnergyReceiver energyReceiver = (IEnergyReceiver) tileReceiver;

                        extractEnergy(direction, energyReceiver.receiveEnergy(direction.getOpposite(), energyStorage.extractEnergy(energyStorage.getMaxExtract(), true), false), false);
                    }
                }
            }

            Block block = worldObj.getBlock(xCoord, yCoord - 1, zCoord);

            if (block != null && block == Blocks.water) {
                energyStorage.receiveEnergy(10, false);
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        energyStorage.setEnergyStored(tagCompound.getInteger("energy"));
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        tagCompound.setInteger("energy", energyStorage.getEnergyStored());
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection from) {
        return from == ForgeDirection.UP;
    }

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
        return energyStorage.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
        return energyStorage.extractEnergy(maxExtract, simulate);
    }

    @Override
    public int getEnergyStored(ForgeDirection from) {
        return energyStorage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from) {
        return energyStorage.getMaxEnergyStored();
    }
}
