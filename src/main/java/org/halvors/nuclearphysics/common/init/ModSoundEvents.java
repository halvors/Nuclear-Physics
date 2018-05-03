package org.halvors.nuclearphysics.common.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import org.halvors.nuclearphysics.common.Reference;

public class ModSoundEvents {
    public static final SoundEvent ACCELERATOR = createSoundEvent("block.accelerator");
    public static final SoundEvent ANTIMATTER = createSoundEvent("block.antimatter");
    public static final SoundEvent ASSEMBLER = createSoundEvent("block.assembler");
    public static final SoundEvent ELECTRIC_TURBINE = createSoundEvent("block.electric_turbine");
    public static final SoundEvent REACTOR_CELL = createSoundEvent("block.reactor_cell");
    public static final SoundEvent SIREN = createSoundEvent("block.siren");

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
                    ACCELERATOR,
                    ANTIMATTER,
                    ASSEMBLER,
                    ELECTRIC_TURBINE,
                    REACTOR_CELL,
                    SIREN
            };

            registry.registerAll(soundEvents);
        }
    }
}

