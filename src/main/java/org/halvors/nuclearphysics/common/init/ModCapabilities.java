package org.halvors.nuclearphysics.common.init;

import org.halvors.nuclearphysics.common.capabilities.CapabilityBoilHandler;

public class ModCapabilities {
    /**
     * Register the capabilities.
     */
    public static void registerCapabilities() {
        CapabilityBoilHandler.register();
    }
}