package org.halvors.quantum.common.fluid.tank;

import net.minecraftforge.fluids.FluidStack;
import scala.actors.threadpool.Arrays;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class FluidTankStrict extends FluidTankQuantum {
    private FluidStack validFillFluid;
    private FluidStack validDrainFluid;

    public FluidTankStrict(int capacity, boolean canFill, boolean canDrain, FluidStack validFillFluid, FluidStack validDrainFluid) {
        super(capacity);

        this.canFill = canFill;
        this.canDrain = canDrain;
        this.validFillFluid = validFillFluid;
        this.validDrainFluid = validDrainFluid;
    }

    public FluidTankStrict(FluidStack fluid, int capacity, boolean canFill, boolean canDrain) {
        super(fluid, capacity);

        this.canFill = canFill;
        this.canDrain = canDrain;
        this.validFillFluid = fluid;
        this.validDrainFluid = fluid;
    }

    /*
    public FluidTankStrict(@Nullable FluidStack fluidStack, @Nullable FluidStack fluidStackOther, int capacity) {
        this(fluidStack, fluidStackOther, capacity, true, true);
    }

    public FluidTankStrict(@Nullable FluidStack fluidStack, int capacity, boolean canFill, boolean canDrain) {
        this(fluidStack, fluidStack, capacity, canFill, canDrain);
    }

    public FluidTankStrict(@Nullable FluidStack fluidStack, int capacity) {
        this(fluidStack, fluidStack, capacity, true, true);
    }
    */

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        if (validFillFluid.isFluidEqual(resource)) {
            return super.fill(resource, doFill);
        }

        return 0;
    }

    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        if (validDrainFluid.isFluidEqual(resource)) {
            return drain(resource.amount, doDrain);
        }

        return null;
    }
}
