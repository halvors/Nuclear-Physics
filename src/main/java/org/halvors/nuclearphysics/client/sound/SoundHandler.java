package org.halvors.nuclearphysics.client.sound;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class SoundHandler {
	public static void playSound(final ISound sound) {
		Minecraft.getMinecraft().getSoundHandler().playSound(sound);
	}

	public static void playSound(final String sound) {
		playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation(sound), 1.0F));
	}
}

