package org.halvors.quantum.common.item;

import org.halvors.quantum.common.QuantumItems;

public class ItemCell extends ItemMetadata {
    public ItemCell(String name) {
        super(name);

        if (!name.equalsIgnoreCase("cell_empty")) {
            setContainerItem(QuantumItems.itemCell);
        }
    }
}