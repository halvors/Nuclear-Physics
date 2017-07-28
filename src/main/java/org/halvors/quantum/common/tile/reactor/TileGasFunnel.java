package org.halvors.quantum.common.tile.reactor;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.halvors.quantum.common.fluid.GasTank;

import javax.annotation.Nonnull;

public class TileGasFunnel extends TileEntity implements ITickable {
    private final GasTank tank = new GasTank(Fluid.BUCKET_VOLUME * 16);

    public TileGasFunnel() {

    }

    @Override
    public void update() {
        if (tank.getFluidAmount() > 0) {
            TileEntity tile = world.getTileEntity(pos.up());

            if (tile instanceof IFluidHandler) {
                IFluidHandler handler = (IFluidHandler) tile;

                //if (handler.canFill(EnumFacing.DOWN, tank.getFluid().getFluid())) {
                    FluidStack drainedStack = tank.drain(tank.getCapacity(), false);

                    if (drainedStack != null) {
                        tank.drain(handler.fill(drainedStack, true), true);
                    }
                //}
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        tank.readFromNBT(tag);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag = super.writeToNBT(tag);

        tag = tank.writeToNBT(tag);

        return tag;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nonnull EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nonnull
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nonnull EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            if (facing == EnumFacing.DOWN || facing == EnumFacing.UP) {
                return (T) tank;
            }
        }

        return super.getCapability(capability, facing);
    }
}

