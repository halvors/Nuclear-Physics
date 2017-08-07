package org.halvors.quantum.atomic.client.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SoundHandler {
	private static final Minecraft game = Minecraft.getMinecraft();

	public static void playSound(ISound sound) {
		game.getSoundHandler().playSound(sound);
	}

	/*
	public static void playSound(String sound) {
		playSound(PositionedSoundRecord.createPositionedSoundRecord(ResourceUtility.getResource(ResourceType.SOUND, sound), 1));
	}
	*/
}

