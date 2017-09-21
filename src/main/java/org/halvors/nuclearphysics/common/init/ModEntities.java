package org.halvors.nuclearphysics.common.init;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import org.halvors.nuclearphysics.common.Reference;
import org.halvors.nuclearphysics.common.entity.EntityParticle;

public class ModEntities {
    @EventBusSubscriber
    public static class RegistrationHandler {
        private static int entityId = 0;

        /**
         * Register this mod's {@link Entity} types.
         *
         * @param event The event
         */
        @SubscribeEvent
        public static void registerEntities(final RegistryEvent.Register<EntityEntry> event) {
            final EntityEntry[] entries = {
                    createBuilder("particle")
                            .entity(EntityParticle.class)
                            .tracker(80, 3, true)
                            .build()
            };

            event.getRegistry().registerAll(entries);
        }

        /**
         * Create an {@link EntityEntryBuilder} with the specified unlocalised/registry name and an automatically-assigned network ID.
         *
         * @param name The name
         * @param <E>  The entity type
         * @return The builder
         */
        private static <E extends Entity> EntityEntryBuilder<E> createBuilder(final String name) {
            final EntityEntryBuilder<E> builder = EntityEntryBuilder.create();
            final ResourceLocation registryName = new ResourceLocation(Reference.ID, name);
            return builder.id(registryName, entityId++).name(registryName.toString());
        }
    }
}