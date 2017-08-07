package org.halvors.quantum.common;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import org.halvors.quantum.common.entity.EntityParticle;

public class QuantumEntities {
    public static void register() {
        // Register entities.
        EntityRegistry.registerModEntity(new ResourceLocation(Reference.ID, "Particle"), EntityParticle.class, "Particle", 0, Quantum.getInstance(), 80, 3, true);
    }
}
