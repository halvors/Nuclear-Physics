package org.halvors.nuclearphysics.common.capabilities.fluid;

import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public class GasTank extends LiquidTank {
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
}
