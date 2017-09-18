package org.halvors.nuclearphysics.common.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import org.halvors.nuclearphysics.api.fluid.IBoilHandler;

public class CapabilityBoilHandler {
    @CapabilityInject(IBoilHandler.class)
    public static Capability<IBoilHandler> BOIL_HANDLER_CAPABILITY = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IBoilHandler.class, new NullStorage<>(), IBoilHandler.class);
    }
}