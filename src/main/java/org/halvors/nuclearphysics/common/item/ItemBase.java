package org.halvors.nuclearphysics.common.item;

import net.minecraft.item.Item;
import org.halvors.nuclearphysics.NuclearPhysics;
import org.halvors.nuclearphysics.common.Reference;

/**
 * This is a basic ItemBase that is meant to be extended by other Items.
 *
 * @author halvors
 */
public class ItemBase extends Item {
	public ItemBase(final Properties properties) {
		setRegistryName(Reference.ID, name);
		//setCreativeTab(NuclearPhysics.getCreativeTab());
	}

	public void registerItemModel() {
		NuclearPhysics.getProxy().registerItemRenderer(this, 0, name);
	}
}
