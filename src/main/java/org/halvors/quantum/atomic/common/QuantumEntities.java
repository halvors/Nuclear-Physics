package org.halvors.quantum.atomic.common;

import net.minecraftforge.fml.common.registry.EntityRegistry;
import org.halvors.quantum.atomic.common.entity.EntityParticle;

public class QuantumEntities {
    public static void register() {
        // Register entities.
        EntityRegistry.registerModEntity(EntityParticle.class, "Particle", 0, Quantum.getInstance(), 80, 3, true);
    }
}
