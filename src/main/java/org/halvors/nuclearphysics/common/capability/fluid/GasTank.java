package org.halvors.nuclearphysics.common.capability.fluid;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public class GasTank extends LiquidTank {
    public GasTank(int capacity) {
        super(capacity);
    }

    public GasTank(@Nullable FluidStack fluidStack, int capacity) {
        super(fluidStack, capacity);
    }

    public GasTank(Fluid fluid, int amount, int capacity) {
        super(fluid, amount, capacity);
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
            return drain(resource.amount, doDrain);
        }

        return null;
    }
}
