package org.halvors.atomicscience.common.item;

/**
 * This is a basic Item that is meant to be extended by other Items.
 *
 * @author halvors
 */
public class Item extends net.minecraft.item.Item {
	protected final String name;

	protected Item(String name) {
		this.name = name;

		setUnlocalizedName(name);
		setCreativeTab(org.halvors.electrometrics.AtomicScience.getCreativeTab());
	}
}
