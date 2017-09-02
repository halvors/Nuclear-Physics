package org.halvors.nuclearphysics.api.fluid;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public interface IBoilHandler extends IFluidHandler {
    int fillInternal(FluidStack resource, boolean doFill);
}
