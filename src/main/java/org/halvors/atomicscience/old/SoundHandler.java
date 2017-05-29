package org.halvors.atomicscience.old;

import net.minecraft.client.audio.SoundManager;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;

public class SoundHandler
{
    public static final SoundHandler INSTANCE = new SoundHandler();
    public static final String[] SOUND_FILES = { "antimatter.ogg", "strangematter.ogg", "alarm.ogg", "accelerator.ogg", "turbine.ogg", "assembler.ogg" };

    @ForgeSubscribe
    public void loadSoundEvents(SoundLoadEvent event)
    {
        for (int i = 0; i < SOUND_FILES.length; i++) {
            event.manager.func_77372_a("atomicscience:" + SOUND_FILES[i]);
        }
    }
}
