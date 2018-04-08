package org.halvors.nuclearphysics.common.tile.reactor;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;
import org.halvors.nuclearphysics.api.fluid.IBoilHandler;
import org.halvors.nuclearphysics.common.capabilities.fluid.GasTank;

public class TileGasFunnel extends TileEntity implements IBoilHandler, IFluidHandler {
    private final GasTank tank = new GasTank(FluidContainerRegistry.BUCKET_VOLUME * 16);

    public TileGasFunnel() {

    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        tank.readFromNBT(tag.getCompoundTag("tank"));
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        tank.writeToNBT(tag.getCompoundTag("tank"));
    }

    /*
    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return (capability == CapabilityBoilHandler.BOIL_HANDLER_CAPABILITY && facing == EnumFacing.DOWN) || (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing == EnumFacing.UP) || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nonnull
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityBoilHandler.BOIL_HANDLER_CAPABILITY && facing == EnumFacing.DOWN) {
            return (T) this;
        } else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing == EnumFacing.UP) {
            return (T) tank;
        }

        return super.getCapability(capability, facing);
    }
    */

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
    public int receiveGas(ForgeDirection from, FluidStack fluidStack, boolean doTransfer) {
        if (from == ForgeDirection.DOWN) {
            return tank.fill(fluidStack, doTransfer);
        }

        return 0;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return drain(from, resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        if (from == ForgeDirection.UP) {
            return tank.drain(maxDrain, doDrain);
        }

        return null;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return false;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return from == ForgeDirection.UP;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[] { tank.getInfo() };
    }
}

