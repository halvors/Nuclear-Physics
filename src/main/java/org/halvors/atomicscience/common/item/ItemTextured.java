package org.halvors.atomicscience.common.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import org.halvors.electrometrics.common.Reference;

public class ItemTextured extends Item {
	protected ItemTextured(String name) {
		super(name);
	}

	@Override
	public void registerIcons(IIconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon(Reference.PREFIX + name);
	}
}
