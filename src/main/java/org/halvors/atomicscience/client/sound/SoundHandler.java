package org.halvors.atomicscience.client.sound;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import org.halvors.electrometrics.common.base.ResourceType;
import org.halvors.electrometrics.common.util.ResourceUtils;

@SideOnly(Side.CLIENT)
public class SoundHandler {
	private static final Minecraft mc = Minecraft.getMinecraft();

	public static void playSound(ISound sound) {
		mc.getSoundHandler().playSound(sound);
	}

	public static void playSound(String sound) {
		playSound(PositionedSoundRecord.createPositionedSoundRecord(ResourceUtils.getResource(ResourceType.SOUND, sound), 1.0F));
	}
}

