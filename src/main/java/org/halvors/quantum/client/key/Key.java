package org.halvors.quantum.client.key;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum Key {
	SNEAK(Minecraft.getMinecraft().gameSettings.keyBindSneak),
	JUMP(Minecraft.getMinecraft().gameSettings.keyBindJump);

	private final KeyBinding keyBinding;

	Key(KeyBinding keyBinding) {
		this.keyBinding = keyBinding;
	}

	public KeyBinding getKeyBinding() {
		return keyBinding;
	}
}
