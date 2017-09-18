package org.halvors.nuclearphysics.common.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import org.halvors.nuclearphysics.api.fluid.IBoilHandler;
import org.halvors.nuclearphysics.common.capabilities.fluid.GasTank;
import org.halvors.nuclearphysics.common.tile.ITileNetwork;

public class CapabilityBoilHandler {
    @CapabilityInject(IBoilHandler.class)
    public static Capability<IBoilHandler> BOIL_HANDLER_CAPABILITY = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IBoilHandler.class, new NullStorage<>(), IBoilHandler.class);
    }
}