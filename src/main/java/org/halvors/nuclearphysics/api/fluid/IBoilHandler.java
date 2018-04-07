package org.halvors.nuclearphysics.api.fluid;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public interface IBoilHandler {
    int receiveGas(ForgeDirection from, FluidStack fluidStack, boolean doTransfer);
}
