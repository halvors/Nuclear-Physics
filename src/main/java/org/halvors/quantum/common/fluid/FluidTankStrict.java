package org.halvors.quantum.common.fluid;

import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public class FluidTankStrict extends FluidTankQuantum {
    private FluidStack fluidOther;

    public FluidTankStrict(@Nullable FluidStack fluidStack, @Nullable FluidStack fluidStackOther, int capacity, boolean canFill, boolean canDrain) {
        super(fluidStack, capacity);

        this.fluidOther = fluidStackOther;
        this.canFill = canFill;
        this.canDrain = canDrain;
    }

    public FluidTankStrict(@Nullable FluidStack fluidStack, @Nullable FluidStack fluidStackOther, int capacity) {
        this(fluidStack, fluidStackOther, capacity, true, true);
    }

    public FluidTankStrict(@Nullable FluidStack fluidStack, int capacity, boolean canFill, boolean canDrain) {
        this(fluidStack, fluidStack, capacity, canFill, canDrain);
    }

    public FluidTankStrict(@Nullable FluidStack fluidStack, int capacity) {
        this(fluidStack, fluidStack, capacity, true, true);
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        // TODO: Does this work?
        if (resource.isFluidEqual(fluid) || resource.isFluidEqual(fluidOther)) {
            return super.fill(resource, doFill);
        }

        return 0;
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        // TODO: Does this work?
        if (resource.isFluidEqual(fluid) || resource.isFluidEqual(fluidOther)) {
            return drain(resource.amount, doDrain);
        }

        return null;
    }
}
