package org.halvors.nuclearphysics.common.capabilities.fluid;

import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public class GasTank extends LiquidTank {
    public GasTank(final int capacity) {
        super(capacity);
    }

    @Override
    public int fill(final FluidStack resource, final boolean doFill) {
        if (resource.getFluid().isGaseous()) {
            return super.fill(resource, doFill);
        }

        return 0;
    }

    @Nullable
    @Override
    public FluidStack drain(final FluidStack resource, final boolean doDrain) {
        if (resource.getFluid().isGaseous()) {
            return super.drain(resource, doDrain);
        }

        return null;
    }
}
