package org.halvors.nuclearphysics.common.container.particle;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import org.halvors.nuclearphysics.common.container.ContainerBase;
import org.halvors.nuclearphysics.common.init.ModItems;
import org.halvors.nuclearphysics.common.tile.particle.TileAccelerator;

public class ContainerAccelerator extends ContainerBase<TileAccelerator> {
    public ContainerAccelerator(InventoryPlayer inventoryPlayer, TileAccelerator tile) {
        super(inventoryPlayer, tile);

        // Inputs
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 0, 132, 26));
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 1, 132, 51));

        // Output
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 2, 132, 75));
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 3, 106, 75));

        // Players inventory
        addPlayerInventory(inventoryPlayer.player);
    }

    /** Called to transfer a stack from one inventory to the other eg. when shift clicking. */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotId) {
        ItemStack copyStack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(slotId);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack = slot.getStack();

            if (!itemStack.isEmpty()) {
                copyStack = itemStack.copy();

                if (slotId > 2) {
                    if (itemStack.getItem() == ModItems.itemCell) {
                        if (!mergeItemStack(itemStack, 1, 2, false)) {
                            return null;
                        }
                    } else if (!mergeItemStack(itemStack, 0, 1, false)) {
                        return null;
                    }
                } else if (!mergeItemStack(itemStack, 3, 36 + 3, false)) {
                    return null;
                }

                if (itemStack.isEmpty()) {
                    slot.putStack(ItemStack.EMPTY);
                } else {
                    slot.onSlotChanged();
                }

                if (itemStack.getCount() == copyStack.getCount()) {
                    return null;
                }

                slot.onTake(player, itemStack);
            }
        }

        return copyStack;
    }
}
