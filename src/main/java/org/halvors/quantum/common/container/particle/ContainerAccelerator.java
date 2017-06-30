package org.halvors.quantum.common.container.particle;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.container.ContainerQuantum;
import org.halvors.quantum.common.tile.particle.TileAccelerator;

public class ContainerAccelerator extends ContainerQuantum {
    private TileAccelerator tile;

    public ContainerAccelerator(InventoryPlayer inventoryPlayer, TileAccelerator tile) {
        super(inventoryPlayer, tile);

        this.tile = tile;

        // Inputs
        addSlotToContainer(new Slot(tile, 0, 132, 26));
        addSlotToContainer(new Slot(tile, 1, 132, 51));

        // Output
        addSlotToContainer(new SlotFurnace(inventoryPlayer.player, tile, 2, 132, 75));
        addSlotToContainer(new SlotFurnace(inventoryPlayer.player, tile, 3, 106, 75));
        addPlayerInventory(inventoryPlayer.player);
    }

    /** Called to transfer a stack from one inventory to the other eg. when shift clicking. */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotId) {
        ItemStack copyStack = null;
        Slot slot = (Slot) inventorySlots.get(slotId);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack = slot.getStack();
            copyStack = itemStack.copy();

            if (slotId > 2) {
                if (itemStack.getItem() == Quantum.itemCell) {
                    if (!mergeItemStack(itemStack, 1, 2, false)) {
                        return null;
                    }
                } else if (!mergeItemStack(itemStack, 0, 1, false)) {
                    return null;
                }
            } else if (!mergeItemStack(itemStack, 3, 36 + 3, false)) {
                return null;
            }

            if (itemStack.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }

            if (itemStack.stackSize == copyStack.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(player, itemStack);
        }

        return copyStack;
    }
}
