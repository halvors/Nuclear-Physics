package org.halvors.quantum.common.accelerator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.lib.gui.ContainerBase;

/** Accelerator container */
public class ContainerAccelerator extends ContainerBase {
    private TileAccelerator tileEntity;

    public ContainerAccelerator(InventoryPlayer inventoryPlayer, TileAccelerator tileEntity) {
        super(tileEntity);

        this.tileEntity = tileEntity;

        // Inputs
        addSlotToContainer(new Slot(tileEntity, 0, 132, 26));
        addSlotToContainer(new Slot(tileEntity, 1, 132, 51));
        // Output
        addSlotToContainer(new SlotFurnace(inventoryPlayer.player, tileEntity, 2, 132, 75));
        addSlotToContainer(new SlotFurnace(inventoryPlayer.player, tileEntity, 3, 106, 75));
        addPlayerInventory(inventoryPlayer.player);
    }

    /** Called to transfer a stack from one inventory to the other eg. when shift clicking. */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
        ItemStack var2 = null;
        Slot var3 = (Slot) this.inventorySlots.get(slot);

        if (var3 != null && var3.getHasStack()) {
            ItemStack itemStack = var3.getStack();
            var2 = itemStack.copy();

            if (slot > 2) {
                if (itemStack.getItem() == Quantum.itemCell) {
                    if (!this.mergeItemStack(itemStack, 1, 2, false)) {
                        return null;
                    }
                } else if (!this.mergeItemStack(itemStack, 0, 1, false)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemStack, 3, 36 + 3, false)) {
                return null;
            }

            if (itemStack.stackSize == 0) {
                var3.putStack(null);
            } else {
                var3.onSlotChanged();
            }

            if (itemStack.stackSize == var2.stackSize) {
                return null;
            }

            var3.onPickupFromSlot(player, itemStack);
        }

        return var2;
    }
}
