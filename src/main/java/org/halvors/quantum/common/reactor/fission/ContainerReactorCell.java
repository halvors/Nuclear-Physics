package org.halvors.quantum.common.reactor.fission;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.halvors.quantum.common.item.ItemBreederFuel;
import org.halvors.quantum.common.item.ItemFissileFuel;
import org.halvors.quantum.lib.gui.ContainerBase;
import org.halvors.quantum.lib.gui.slot.SlotSpecific;

public class ContainerReactorCell extends ContainerBase {
    public ContainerReactorCell(EntityPlayer player, TileReactorCell tileEntity) {
        super(tileEntity);

        addSlotToContainer(new SlotSpecific(tileEntity, 0, 79, 17, ItemFissileFuel.class, ItemBreederFuel.class));
        addPlayerInventory(player);
    }

    /** Called to transfer a stack from one inventory to the other eg. when shift clicking. */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int par1) {
        ItemStack var2 = null;
        Slot slot = (Slot) inventorySlots.get(par1);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack = slot.getStack();
            var2 = itemStack.copy();

            if (par1 >= slotCount) {
                if (getSlot(0).isItemValid(itemStack)) {
                    if (!mergeItemStack(itemStack, 0, 1, false)) {
                        return null;
                    }
                } else if (par1 < 27 + slotCount) {
                    if (!mergeItemStack(itemStack, 27 + slotCount, 36 + slotCount, false)) {
                        return null;
                    }
                } else if (par1 >= 27 + slotCount && par1 < 36 + slotCount && !mergeItemStack(itemStack, 4, 30, false)) {
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

            if (itemStack.stackSize == var2.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(player, itemStack);
        }

        return var2;
    }
}
