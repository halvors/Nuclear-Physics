package org.halvors.quantum.common.tile.reactor;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;
import org.halvors.quantum.lib.thermal.IBoilHandler;

public class TileGasFunnel extends TileEntity implements IBoilHandler {
    private final FluidTank tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 16);

    public TileGasFunnel() {

    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        if (tank.getFluidAmount() > 0) {
            TileEntity tile = worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);

            if (tile instanceof IFluidHandler) {
                IFluidHandler handler = (IFluidHandler) tile;

                if (handler.canFill(ForgeDirection.DOWN, tank.getFluid().getFluid())) {
                    FluidStack drainedStack = tank.drain(tank.getCapacity(), false);

                    if (drainedStack != null) {
                        tank.drain(handler.fill(ForgeDirection.DOWN, drainedStack, true), true);
                    }
                }
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);

        tank.writeToNBT(tagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        tank.readFromNBT(tagCompound);
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        return tank.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        if (resource != null) {
            if (resource.isFluidEqual(tank.getFluid())){
                return tank.drain(resource.amount, doDrain);
            }
        }

        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return tank.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return fluid.isGaseous() && from == ForgeDirection.DOWN;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return fluid.isGaseous() && from == ForgeDirection.UP;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[] { tank.getInfo() };
    }
}

