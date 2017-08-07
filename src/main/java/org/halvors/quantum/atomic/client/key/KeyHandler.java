package org.halvors.quantum.atomic.client.key;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

@SideOnly(Side.CLIENT)
public class KeyHandler {
	public static boolean getIsKeyPressed(KeyBinding keyBinding) {
		int keyCode = keyBinding.getKeyCode();

		return keyCode < 0 ? Mouse.isButtonDown(keyCode + 100) : Keyboard.isKeyDown(keyCode);
	}
}