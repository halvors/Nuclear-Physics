package org.halvors.nuclearphysics.client.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SoundHandler {
	public static void playSound(ISound sound) {
		Minecraft.getMinecraft().getSoundHandler().playSound(sound);
	}

	public static void playSound(SoundEvent sound) {
		playSound(PositionedSoundRecord.getMasterRecord(sound, 1));
	}
}

