package org.halvors.quantum.client.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.common.utility.ResourceUtility;
import org.halvors.quantum.common.utility.type.ResourceType;

@SideOnly(Side.CLIENT)
public class SoundHandler {
	private static final Minecraft game = Minecraft.getMinecraft();

	public static void playSound(ISound sound) {
		game.getSoundHandler().playSound(sound);
	}

	public static void playSound(String sound) {
		playSound(PositionedSoundRecord.createPositionedSoundRecord(ResourceUtility.getResource(ResourceType.SOUND, sound), 1));
	}
}

