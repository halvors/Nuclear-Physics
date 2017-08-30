package org.halvors.nuclearphysics.common.init;

import net.minecraftforge.fml.common.registry.EntityRegistry;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.entity.EntityParticle;

public class ModEntities {
    public static void register() {
        // Register entities.
        EntityRegistry.registerModEntity(EntityParticle.class, "Particle", 0, NuclearPhysics.getInstance(), 80, 3, true);
    }
}
