package org.halvors.nuclearphysics.api.fluid;

import net.minecraftforge.fluids.FluidStack;

public interface IBoilHandler {
    int receiveGas(FluidStack fluidStack, boolean doTransfer);
}
