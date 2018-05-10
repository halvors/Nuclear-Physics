package org.halvors.nuclearphysics.common.container.slot;

import cofh.api.energy.IEnergyContainerItem;
import ic2.api.item.IElectricItem;
import net.minecraft.inventory.IInventory;

public class SlotEnergyItem extends SlotSpecific {
    public SlotEnergyItem(IInventory inventory, int index, int x, int y) {
        super(inventory, index, x, y, IEnergyContainerItem.class, IElectricItem.class);
    }
}