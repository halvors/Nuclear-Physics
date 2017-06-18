package org.halvors.quantum.common.container.machine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.tile.machine.TileGasCentrifuge;
import org.halvors.quantum.lib.container.ContainerBase;
import org.halvors.quantum.lib.container.slot.SlotEnergyItem;
import org.halvors.quantum.lib.utility.OreDictionaryUtility;

public class ContainerGasCentrifuge extends ContainerBase {
    private static final int slotCount = 4;
    private TileGasCentrifuge tile;

    public ContainerGasCentrifuge(InventoryPlayer inventoryPlayer, TileGasCentrifuge tile) {
        super(tile);

        this.tile = tile;

        // Electric Item
        addSlotToContainer(new SlotEnergyItem(tile, 0, 131, 26));

        // Uranium Gas Tank
        addSlotToContainer(new Slot(tile, 1, 25, 50));

        // Output Uranium 235
        addSlotToContainer(new SlotFurnace(inventoryPlayer.player, tile, 2, 81, 26));

        // Output Uranium 238
        addSlotToContainer(new SlotFurnace(inventoryPlayer.player, tile, 3, 101, 26));
        addPlayerInventory(inventoryPlayer.player);
        tile.openChest();
    }
    
    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return tile.isUseableByPlayer(player);
    }

    /** Called to transfer a stack from one inventory to the other eg. when shift clicking. */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotId) {
        ItemStack copyStack = null;
        Slot slot = (Slot) inventorySlots.get(slotId);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack = slot.getStack();
            copyStack = itemStack.copy();

            if (slotId >= slotCount) {
                if (getSlot(0).isItemValid(itemStack)) {
                    if (!mergeItemStack(itemStack, 0, 1, false)) {
                        return null;
                    }
                } else if (OreDictionaryUtility.isUraniumOre(itemStack)) {
                    if (!mergeItemStack(itemStack, 1, 2, false)) {
                        return null;
                    }
                } else if (itemStack.getItem() == Quantum.itemCell) {
                    if (!mergeItemStack(itemStack, 3, 4, false)) {
                        return null;
                    }
                } else if (slotId < 27 + slotCount) {
                    if (!mergeItemStack(itemStack, 27 + slotCount, 36 + slotCount, false)) {
                        return null;
                    }
                } else if (slotId >= 27 + slotCount && slotId < 36 + slotCount && !mergeItemStack(itemStack, 4, 30, false)) {
                    return null;
                }
            } else if (!mergeItemStack(itemStack, slotCount, 36 + slotCount, false)) {
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
