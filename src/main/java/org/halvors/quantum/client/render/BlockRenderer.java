package org.halvors.quantum.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.base.ResourceType;
import org.halvors.quantum.common.util.ResourceUtils;

@SideOnly(Side.CLIENT)
public class BlockRenderer {
	private static final String[] sides = new String[] { "Bottom", "Top", "Front", "Back", "Left", "Right" };

	public static void loadDynamicTextures(IIconRegister iconRegister, String name, IIcon[] textures, DefaultIcon... defaultIcons) {
		for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
			String texture = name + sides[side.ordinal()];
			String textureActive = texture + "Active";

			if (textureExists(texture)) {
				textures[side.ordinal()] = iconRegister.registerIcon(Reference.PREFIX + texture);

				if (textureExists(textureActive)) {
					textures[side.ordinal() + 6] = iconRegister.registerIcon(Reference.PREFIX + textureActive);
				} else {
					boolean found = false;

					for (DefaultIcon defaultIcon : defaultIcons) {
						if (defaultIcon.getIcons().contains(side.ordinal() + 6)) {
							textures[side.ordinal() + 6] = defaultIcon.getDefaultIcon();
							found = true;
						}
					}

					if (!found) {
						textures[side.ordinal() + 6] = iconRegister.registerIcon(Reference.PREFIX + texture);
					}
				}
			} else {
				for (DefaultIcon defaultIcon : defaultIcons) {
					if (defaultIcon.getIcons().contains(side.ordinal())) {
						textures[side.ordinal()] = defaultIcon.getDefaultIcon();
					}

					if (defaultIcon.getIcons().contains(side.ordinal() + 6)) {
						textures[side.ordinal() + 6] = defaultIcon.getDefaultIcon();
					}
				}
			}
		}
	}

	private static boolean textureExists(String texture) {
		try {
			Minecraft.getMinecraft().getResourceManager().getAllResources(ResourceUtils.getResource(ResourceType.TEXTURE_BLOCKS, texture + ".png"));

			return true;
		} catch (Exception e) {
			return false;
		}
	}
}