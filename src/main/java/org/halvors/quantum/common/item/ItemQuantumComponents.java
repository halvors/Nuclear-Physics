package org.halvors.quantum.common.item;

import org.halvors.quantum.common.Quantum;

public class ItemQuantumComponents extends ItemQuantum {
	public ItemQuantumComponents(String name) {
		super(name);

		setCreativeTab(Quantum.getCreativeComponentsTab());
	}
}
