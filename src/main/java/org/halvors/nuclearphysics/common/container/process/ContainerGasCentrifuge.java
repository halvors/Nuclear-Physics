package org.halvors.nuclearphysics.common.container.process;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import org.halvors.nuclearphysics.common.container.ContainerBase;
import org.halvors.nuclearphysics.common.init.ModItems;
import org.halvors.nuclearphysics.common.tile.process.TileGasCentrifuge;
import org.halvors.nuclearphysics.common.utility.OreDictionaryHelper;

public class ContainerGasCentrifuge extends ContainerBase<TileGasCentrifuge> {
    public ContainerGasCentrifuge(InventoryPlayer inventoryPlayer, TileGasCentrifuge tile) {
        super(inventoryPlayer, tile);

        slotCount = 4;

        // Electric Item
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 0, 131, 26));

        // Uranium Gas Tank
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 1, 25, 50));

        // Output Uranium 235
        //addSlotToContainer(new SlotFurnaceOutput(inventoryPlayer.player, tile, 2, 81, 26));
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 2, 81, 26));

        // Output Uranium 238
        //addSlotToContainer(new SlotFurnaceOutput(inventoryPlayer.player, tile, 3, 101, 26));
        addSlotToContainer(new SlotItemHandler(tile.getInventory(), 3, 101, 26));

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

                if (slotId >= slotCount) {
                    if (getSlot(0).isItemValid(itemStack)) {
                        if (!mergeItemStack(itemStack, 0, 1, false)) {
                            return null;
                        }
                    } else if (OreDictionaryHelper.isUraniumOre(itemStack)) {
                        if (!mergeItemStack(itemStack, 1, 2, false)) {
                            return null;
                        }
                    } else if (itemStack.getItem() == ModItems.itemCell) {
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
