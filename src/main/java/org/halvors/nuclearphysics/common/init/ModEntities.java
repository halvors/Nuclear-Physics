package org.halvors.nuclearphysics.common.init;

import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraft.entity.Entity;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.entity.EntityParticle;

public class ModEntities {
    private static int entityId = 0;

    public static void registerEntities() {
        registerEntity(EntityParticle.class, "particle", 80, 3, true);
    }

    /**
     * Register an entity with the specified tracking values.
     *
     * @param entityClass          The entity's class
     * @param entityName           The entity's unique name
     * @param trackingRange        The range at which MC will send tracking updates
     * @param updateFrequency      The frequency of tracking updates
     * @param sendsVelocityUpdates Whether to send velocity information packets as well
     */
    private static void registerEntity(final Class<? extends Entity> entityClass, final String entityName, final int trackingRange, final int updateFrequency, final boolean sendsVelocityUpdates) {
        EntityRegistry.registerModEntity(entityClass, entityName, entityId++, NuclearPhysics.getInstance(), trackingRange, updateFrequency, sendsVelocityUpdates);
    }
}
