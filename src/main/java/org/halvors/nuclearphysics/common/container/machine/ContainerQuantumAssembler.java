package org.halvors.nuclearphysics.common.container.machine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import org.halvors.nuclearphysics.common.container.ContainerBase;
import org.halvors.nuclearphysics.common.init.ModItems;
import org.halvors.nuclearphysics.common.tile.machine.TileQuantumAssembler;

public class ContainerQuantumAssembler extends ContainerBase<TileQuantumAssembler> {
    public ContainerQuantumAssembler(InventoryPlayer inventoryPlayer, TileQuantumAssembler tile) {
        super(inventoryPlayer, tile);

        yInventoryDisplacement = 148;
        yHotBarDisplacement = 206;

        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 0, 80, 40));
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 1, 53, 56));
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 2, 107, 56));
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 3, 53, 88));
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 4, 107, 88));
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 5, 80, 103));
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 6, 80, 72));

        // Players inventory
        addPlayerInventory(inventoryPlayer.player);
    }

    /** Called to transfer a stack from one inventory to the other eg. when shift clicking. */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotId) {
        ItemStack copyStack = null;
        Slot slot = inventorySlots.get(slotId);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack = slot.getStack();

            if (itemStack != null) {
                copyStack = itemStack.copy();

                if (slotId > 6) {
                    if (itemStack.getItem() == ModItems.itemDarkMatterCell) {
                        if (!mergeItemStack(itemStack, 0, 6, false)) {
                            return null;
                        }
                    } else if (!getSlot(6).getHasStack()) {
                        if (!mergeItemStack(itemStack, 6, 7, false)) {
                            return null;
                        }
                    }
                } else if (!mergeItemStack(itemStack, 7, 36 + 7, false)) {
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
        }

        return copyStack;
    }
}
