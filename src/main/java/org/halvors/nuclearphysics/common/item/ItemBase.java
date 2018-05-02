package org.halvors.nuclearphysics.common.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.Reference;

/**
 * This is a basic ItemBase that is meant to be extended by other Items.
 *
 * @author halvors
 */
public class ItemBase extends Item {
	protected final String name;

	public ItemBase(final String name) {
		this.name = name;

		setUnlocalizedName(Reference.ID + "." + name);
		setCreativeTab(NuclearPhysics.getCreativeTab());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon(Reference.PREFIX + name);
	}
}
