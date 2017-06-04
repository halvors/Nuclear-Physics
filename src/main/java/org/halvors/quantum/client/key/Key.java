package org.halvors.quantum.client.key;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

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
