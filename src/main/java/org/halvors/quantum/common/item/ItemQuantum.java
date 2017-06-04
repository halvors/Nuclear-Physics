package org.halvors.quantum.common.item;

import net.minecraft.item.Item;
import org.halvors.quantum.Quantum;

/**
 * This is a basic ItemQuantum that is meant to be extended by other Items.
 *
 * @author halvors
 */
public class ItemQuantum extends Item {
	protected final String name;

	protected ItemQuantum(String name) {
		this.name = name;

		setUnlocalizedName(name);
		setCreativeTab(Quantum.getCreativeTab());
	}
}
