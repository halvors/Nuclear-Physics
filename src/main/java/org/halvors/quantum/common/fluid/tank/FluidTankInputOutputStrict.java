package org.halvors.quantum.common.fluid.tank;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nullable;

public class FluidTankInputOutputStrict extends FluidTankInputOutput {
    public FluidTankInputOutputStrict(FluidTank inputTank, FluidTank outputTank) {
        super(inputTank, outputTank);
    }

    public FluidTankInputOutputStrict(FluidTank tank) {
        super(tank, tank);
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        if (inputTank.getFluid() != null && resource.getFluid() == inputTank.getFluid().getFluid()) {
            return inputTank.fill(resource, doFill);
        }

        return 0;
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        if (outputTank.getFluid() != null && resource.getFluid() == outputTank.getFluid().getFluid()) {
            return drain(resource.amount, doDrain);
        }

        return null;
    }
}
