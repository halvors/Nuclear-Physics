package org.halvors.quantum.common.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import org.halvors.quantum.common.Reference;

import java.util.List;

public abstract class ItemTextured extends Item {
	protected ItemTextured(String name) {
		super(name);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon(Reference.PREFIX + name);
	}

	public abstract void getSubBlocks(Item item, CreativeTabs tab, List list);
}
