package org.halvors.quantum.common.init;

import net.minecraftforge.fml.common.registry.EntityRegistry;
import org.halvors.quantum.common.Quantum;
import org.halvors.quantum.common.entity.EntityParticle;

public class QuantumEntities {
    public static void register() {
        // Register entities.
        EntityRegistry.registerModEntity(EntityParticle.class, "Particle", 0, Quantum.getInstance(), 80, 3, true);
    }
}
