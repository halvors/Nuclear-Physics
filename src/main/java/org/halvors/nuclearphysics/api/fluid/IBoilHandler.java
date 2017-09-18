package org.halvors.nuclearphysics.api.fluid;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public interface IBoilHandler {
    int receiveGas(FluidStack fluidStack, boolean doTransfer);
}
