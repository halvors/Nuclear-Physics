package org.halvors.quantum.core.item;

import org.halvors.quantum.atomic.common.Quantum;
import org.halvors.quantum.atomic.common.item.ItemQuantum;

public class ItemComponents extends ItemQuantum {
	public ItemComponents(String name) {
		super(name);

		setCreativeTab(Quantum.getCreativeComponentsTab());
	}
}
