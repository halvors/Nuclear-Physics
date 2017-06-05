package org.halvors.quantum.lib.tile;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public interface IExternalInventory {
    IExternalInventoryBox getInventory();

    boolean canStore(ItemStack stack, int slot, ForgeDirection side);

    boolean canRemove(ItemStack stack, int slot, ForgeDirection side);
}