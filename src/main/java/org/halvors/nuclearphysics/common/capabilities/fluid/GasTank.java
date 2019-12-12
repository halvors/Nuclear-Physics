package org.halvors.nuclearphysics.common.capabilities.fluid;

import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;

public class GasTank extends LiquidTank {
    public GasTank(final int capacity) {
        super(capacity);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (resource.getFluid().isGaseous()) {
            return super.fill(resource, action);
        }

        return 0;
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (resource.getFluid().isGaseous()) {
            return super.drain(resource, action);
        }

        return null;
    }
}
