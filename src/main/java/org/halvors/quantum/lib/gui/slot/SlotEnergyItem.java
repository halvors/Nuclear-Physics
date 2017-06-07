package org.halvors.quantum.lib.gui.slot;

import net.minecraft.inventory.IInventory;
import cofh.api.energy.IEnergyContainerItem;
import universalelectricity.api.item.IEnergyItem;

public class SlotEnergyItem extends SlotSpecific {
    public SlotEnergyItem(IInventory inventory, int par3, int par4, int par5) {
        super(inventory, par3, par4, par5, IEnergyItem.class, IEnergyContainerItem.class);
    }

}
