package org.halvors.quantum.common.init;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import org.halvors.quantum.common.Reference;

public class QuantumSoundEvents {
    public static final SoundEvent SIREN = createSoundEvent("tile.siren");

    /**
     * Create a {@link SoundEvent}.
     *
     * @param soundName The SoundEvent's name without the testmod3 prefix
     * @return The SoundEvent
     */
    private static SoundEvent createSoundEvent(final String soundName) {
        final ResourceLocation sound = new ResourceLocation(Reference.ID, soundName);

        return new SoundEvent(sound).setRegistryName(sound);
    }

    @EventBusSubscriber
    public static class RegistrationHandler {
        @SubscribeEvent
        public static void registerSoundEvents(final Register<SoundEvent> event) {
            final IForgeRegistry<SoundEvent> registry = event.getRegistry();

            final SoundEvent[] soundEvents = {
                    SIREN
            };

            registry.registerAll(soundEvents);
        }
    }
}

