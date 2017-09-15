package org.halvors.nuclearphysics.common.capabilities.fluid;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import org.halvors.nuclearphysics.api.fluid.IBoilHandler;

import javax.annotation.Nullable;

public class GasTank extends LiquidTank implements IBoilHandler, IFluidTank {
    public GasTank(int capacity) {
        super(capacity);
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        if (resource.getFluid().isGaseous()) {
            return super.fill(resource, doFill);
        }

        return 0;
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        if (resource.getFluid().isGaseous()) {
            return super.drain(resource, doDrain);
        }

        return null;
    }

    @Override
    public int catchSteam(FluidStack resource, boolean doFill) {
        return fillInternal(resource, doFill);
    }
}
