package org.halvors.quantum.common.item;

import org.halvors.quantum.Quantum;

public class ItemCell extends ItemMetadata {
    public ItemCell(String name) {
        super(name);

        if (!name.equalsIgnoreCase("cellEmpty")) {
            setContainerItem(Quantum.itemCell);
        }
    }
}