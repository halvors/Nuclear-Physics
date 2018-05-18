package org.halvors.nuclearphysics.common.capabilities.fluid;

import net.minecraftforge.fluids.FluidStack;

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
}
