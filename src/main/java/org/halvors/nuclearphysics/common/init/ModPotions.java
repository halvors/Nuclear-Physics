package org.halvors.nuclearphysics.common.init;

import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.halvors.nuclearphysics.common.effect.poison.PoisonRadiation;

/**
 * Registers this mod's {@link Potion}s.
 */
public class ModPotions {
    public static final PoisonRadiation poisonRadiation = new PoisonRadiation();

    @EventBusSubscriber
    public static class RegistrationHandler {
        /**
         * Register this mod's {@link Potion}s.
         *
         * @param event The event
         */
        @SubscribeEvent
        public static void registerPotions(RegistryEvent.Register<Potion> event) {
            event.getRegistry().registerAll(
                    poisonRadiation
            );
        }
    }

}

