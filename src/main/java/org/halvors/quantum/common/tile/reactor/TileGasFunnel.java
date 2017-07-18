package org.halvors.quantum.common.tile.reactor;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.fluids.*;
import org.halvors.quantum.common.grid.thermal.IBoilHandler;

public class TileGasFunnel extends TileEntity implements ITickable, IBoilHandler {
    private final FluidTank tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 16);

    public TileGasFunnel() {

    }

    @Override
    public void update() {
        if (tank.getFluidAmount() > 0) {
            TileEntity tile = world.getTileEntity(pos.up());

            if (tile instanceof IFluidHandler) {
                IFluidHandler handler = (IFluidHandler) tile;

                if (handler.canFill(EnumFacing.DOWN, tank.getFluid().getFluid())) {
                    FluidStack drainedStack = tank.drain(tank.getCapacity(), false);

                    if (drainedStack != null) {
                        tank.drain(handler.fill(EnumFacing.DOWN, drainedStack, true), true);
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
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        tank.readFromNBT(tagCompound);

        return tagCompound;
    }

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
        return tank.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
        if (resource != null) {
            if (resource.isFluidEqual(tank.getFluid())){
                return tank.drain(resource.amount, doDrain);
            }
        }

        return null;
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
        return tank.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid) {
        return fluid.isGaseous() && from == EnumFacing.DOWN;
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid) {
        return fluid.isGaseous() && from == EnumFacing.UP;
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {
        return new FluidTankInfo[] { tank.getInfo() };
    }
}

