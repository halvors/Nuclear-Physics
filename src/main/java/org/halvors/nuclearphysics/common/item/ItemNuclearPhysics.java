package org.halvors.nuclearphysics.common.item;

import net.minecraft.item.Item;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.Reference;

/**
 * This is a basic ItemNuclearPhysics that is meant to be extended by other Items.
 *
 * @author halvors
 */
public class ItemNuclearPhysics extends Item {
	protected final String name;

	public ItemNuclearPhysics(String name) {
		this.name = name;

		setUnlocalizedName(name);
		setRegistryName(Reference.ID, name);
		setCreativeTab(NuclearPhysics.getCreativeTab());
	}

	public void registerItemModel() {
		NuclearPhysics.getProxy().registerItemRenderer(this, 0, name);
	}
}
