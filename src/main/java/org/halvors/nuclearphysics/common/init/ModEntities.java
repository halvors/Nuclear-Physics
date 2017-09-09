package org.halvors.nuclearphysics.common.init;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.Reference;
import org.halvors.nuclearphysics.common.entity.EntityParticle;

public class ModEntities {
    public static void registerEntities() {
        // Register entities.
        EntityRegistry.registerModEntity(new ResourceLocation(Reference.ID, "Particle"), EntityParticle.class, "Particle", 0, NuclearPhysics.getInstance(), 80, 3, true);
    }
}
