package org.halvors.nuclearphysics.api.fluid;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public interface IBoilHandler extends IFluidHandler {
    int catchSteam(FluidStack resource, boolean doFill);
}
