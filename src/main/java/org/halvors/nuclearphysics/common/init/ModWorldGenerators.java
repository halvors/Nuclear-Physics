package org.halvors.nuclearphysics.common.init;

import cpw.mods.fml.common.registry.GameRegistry;
import org.halvors.nuclearphysics.common.world.WorldGeneratorOre;

public class ModWorldGenerators {
    public static void registerWorldGenerators() {
        GameRegistry.registerWorldGenerator(new WorldGeneratorOre(), 0);
    }
}
