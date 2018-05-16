package org.halvors.nuclearphysics.common.init;

import net.minecraftforge.fml.common.registry.GameRegistry;
import org.halvors.nuclearphysics.common.worldgen.OreGenerator;

public class ModWorldGenerators {
    public static void registerWorldGenerators() {
        GameRegistry.registerWorldGenerator(new OreGenerator(), 0);
    }
}
