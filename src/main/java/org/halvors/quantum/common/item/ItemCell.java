package org.halvors.quantum.common.item;

import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.item.old.ItemTexturedMetadata;

public class ItemCell extends ItemTexturedMetadata {
    public ItemCell(String name) {
        super(name);

        if (!name.equalsIgnoreCase("cellEmpty")) {
            setContainerItem(Quantum.itemCell);
        }
    }
}