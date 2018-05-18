package org.halvors.nuclearphysics.common.tile.reactor;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;
import org.halvors.nuclearphysics.api.fluid.IBoilHandler;
import org.halvors.nuclearphysics.common.capabilities.fluid.GasTank;
import org.halvors.nuclearphysics.common.tile.TileBase;

public class TileGasFunnel extends TileBase implements IBoilHandler, IFluidHandler {
    private static final String NBT_TANK = "tank";

    private final GasTank tank = new GasTank(FluidContainerRegistry.BUCKET_VOLUME * 16);

    public TileGasFunnel() {

    }

    @Override
    public void readFromNBT(final NBTTagCompound tag) {
        super.readFromNBT(tag);

        tank.readFromNBT(tag.getCompoundTag(NBT_TANK));
    }

    @Override
    public void writeToNBT(final NBTTagCompound tag) {
        super.writeToNBT(tag);

        tag.setTag(NBT_TANK, tank.writeToNBT(new NBTTagCompound()));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            if (tank.getFluidAmount() > 0) {
                final TileEntity tile = worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);

                if (tile instanceof IFluidHandler) {
                    final IFluidHandler fluidHandler = (IFluidHandler) tile;
                    final FluidStack fluidStack = tank.drain(tank.getCapacity(), false);

                    if (fluidStack != null && fluidHandler.fill(ForgeDirection.UP, fluidStack, false) > 0) {
                        tank.drain(fluidHandler.fill(ForgeDirection.UP, fluidStack, true), true);
                    }
                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public int receiveGas(final ForgeDirection from, final FluidStack fluidStack, final boolean doTransfer) {
        if (from == ForgeDirection.DOWN) {
            return tank.fill(fluidStack, doTransfer);
        }

        return 0;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public int fill(final ForgeDirection from, final FluidStack resource, final boolean doFill) {
        return 0;
    }

    @Override
    public FluidStack drain(final ForgeDirection from, final FluidStack resource, final boolean doDrain) {
        return drain(from, resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(final ForgeDirection from, final int maxDrain, final boolean doDrain) {
        if (from == ForgeDirection.UP) {
            return tank.drain(maxDrain, doDrain);
        }

        return null;
    }

    @Override
    public boolean canFill(final ForgeDirection from, final Fluid fluid) {
        return false;
    }

    @Override
    public boolean canDrain(final ForgeDirection from, final Fluid fluid) {
        return from == ForgeDirection.UP;
    }

    @Override
    public FluidTankInfo[] getTankInfo(final ForgeDirection from) {
        return new FluidTankInfo[] { tank.getInfo() };
    }
}

