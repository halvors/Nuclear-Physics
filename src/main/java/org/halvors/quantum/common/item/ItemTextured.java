package org.halvors.quantum.common.item;

public abstract class ItemTextured extends ItemQuantum {
	public ItemTextured(String name) {
		super(name);
	}

	/*
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon(Reference.PREFIX + name);
	}
	*/
}
