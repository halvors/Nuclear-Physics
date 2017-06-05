package org.halvors.quantum.lib.tile;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import org.halvors.quantum.lib.utility.nbt.ISaveObject;

public interface IExternalInventoryBox extends ISidedInventory, ISaveObject {
    /** Gets the inventory array. ForgeDirection.UNKOWN must return all sides */
    ItemStack[] getContainedItems();

    /** Dels all the items in the inventory */
    void clear();
}
