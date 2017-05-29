package org.halvors.atomicscience.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.IIcon;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class DefaultIcon {
	private final IIcon defaultIcon;
	private final List<Integer> icons = new ArrayList<>();

	private DefaultIcon(IIcon defaultIcon, int ...is) {
		this.defaultIcon = defaultIcon;

		for (int i : is) {
			icons.add(i);
		}
	}

	public static DefaultIcon getAll(IIcon icon) {
		return new DefaultIcon(icon, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11);
	}

	public static DefaultIcon getActivePair(IIcon icon, int... is) {
		DefaultIcon defaultIcon = new DefaultIcon(icon, is);

		for (int i : is) {
			defaultIcon.icons.add(i + 6);
		}

		return defaultIcon;
	}

	public IIcon getDefaultIcon() {
		return defaultIcon;
	}

	public List<Integer> getIcons() {
		return icons;
	}
}
