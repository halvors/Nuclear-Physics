package org.halvors.quantum.common.item;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.common.Reference;

public abstract class ItemTextured extends ItemQuantum {
	public ItemTextured(String name) {
		super(name);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon(Reference.PREFIX + name);
	}
}
