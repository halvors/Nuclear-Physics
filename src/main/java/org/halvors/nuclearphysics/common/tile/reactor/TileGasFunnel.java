package org.halvors.nuclearphysics.common.tile.reactor;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.halvors.nuclearphysics.api.fluid.IBoilHandler;
import org.halvors.nuclearphysics.common.capabilities.CapabilityBoilHandler;
import org.halvors.nuclearphysics.common.capabilities.fluid.GasTank;
import org.halvors.nuclearphysics.common.tile.TileBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileGasFunnel extends TileBase implements ITickable, IBoilHandler {
    private static final String NBT_TANK = "tank";

    private final GasTank tank = new GasTank(Fluid.BUCKET_VOLUME * 16) {
        @Override
        public boolean canFill() {
            return false;
        }
    };

    public TileGasFunnel() {

    }

    @Override
    public void readFromNBT(final NBTTagCompound tag) {
        super.readFromNBT(tag);

        CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.readNBT(tank, null, tag.getTag(NBT_TANK));
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(final NBTTagCompound tag) {
        super.writeToNBT(tag);

        tag.setTag(NBT_TANK, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.writeNBT(tank, null));

        return tag;
    }

    @Override
    public boolean hasCapability(@Nonnull final Capability<?> capability, @Nullable final EnumFacing facing) {
        return (capability == CapabilityBoilHandler.BOIL_HANDLER_CAPABILITY && facing == EnumFacing.DOWN) || (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing == EnumFacing.UP) || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nonnull
    public <T> T getCapability(@Nonnull final Capability<T> capability, @Nullable final EnumFacing facing) {
        if (capability == CapabilityBoilHandler.BOIL_HANDLER_CAPABILITY && facing == EnumFacing.DOWN) {
            return (T) this;
        } else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing == EnumFacing.UP) {
            return (T) tank;
        }

        return super.getCapability(capability, facing);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void update() {
        if (!world.isRemote) {
            if (tank.getFluidAmount() > 0) {
                final IFluidHandler fluidHandler = FluidUtil.getFluidHandler(world, pos.up(), EnumFacing.UP);

                if (fluidHandler != null) {
                    final FluidStack fluidStack = tank.drain(tank.getCapacity(), false);

                    if (fluidStack != null && fluidHandler.fill(fluidStack, false) > 0) {
                        tank.drain(fluidHandler.fill(fluidStack, true), true);
                    }
                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public int receiveGas(final FluidStack fluidStack, final boolean doTransfer) {
        return tank.fillInternal(fluidStack, doTransfer);
    }
}

