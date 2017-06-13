package org.halvors.quantum.common.container.particle;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.tile.machine.TileQuantumAssembler;

public class ContainerQuantumAssembler extends Container {
    private TileQuantumAssembler tileEntity;

    public ContainerQuantumAssembler(InventoryPlayer inventoryPlayer, TileQuantumAssembler tileEntity) {
        this.tileEntity = tileEntity;

        addSlotToContainer(new Slot(tileEntity, 0, 80, 40));
        addSlotToContainer(new Slot(tileEntity, 1, 53, 56));
        addSlotToContainer(new Slot(tileEntity, 2, 107, 56));
        addSlotToContainer(new Slot(tileEntity, 3, 53, 88));
        addSlotToContainer(new Slot(tileEntity, 4, 107, 88));
        addSlotToContainer(new Slot(tileEntity, 5, 80, 103));
        addSlotToContainer(new Slot(tileEntity, 6, 80, 72));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 148 + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 206));
        }

        tileEntity.openChest();
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return tileEntity.isUseableByPlayer(player);
    }

    /** Called to transfer a stack from one inventory to the other eg. when shift clicking. */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotId) {
        ItemStack copyStack = null;
        Slot slot = (Slot) inventorySlots.get(slotId);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack = slot.getStack();
            copyStack = itemStack.copy();

            if (slotId > 6) {
                if (itemStack.getItem() == Quantum.itemDarkMatter) {
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

        return copyStack;
    }
}
