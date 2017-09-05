package org.halvors.nuclearphysics.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.halvors.nuclearphysics.common.tile.TileBase;

public class ContainerBase<T extends TileBase> extends Container {
    protected T tile;
    protected IInventory inventory;

    protected int slotCount;
    protected int xInventoryDisplacement = 8;
    protected int yInventoryDisplacement = 135;
    protected int yHotBarDisplacement = 193;

    public ContainerBase(InventoryPlayer inventoryPlayer, T tile) {
        this.tile = tile;
        this.inventory = inventoryPlayer;
        this.slotCount = inventoryPlayer.getSizeInventory();

        if (tile != null) {
            tile.open(inventoryPlayer.player);
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);

        if (tile != null) {
            tile.close(player);
        }
    }

    // Called to transfer a stack from one inventory to the other eg. when shift clicking.
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotId) {
        ItemStack itemstack = null;
        Slot slot = inventorySlots.get(slotId);

        if (slot != null && slot.getHasStack()) {
            ItemStack slot_stack = slot.getStack();
            itemstack = slot_stack.copy();

            if (slotId < slotCount) {
                if (!mergeItemStack(slot_stack, slotCount, inventorySlots.size(), true)) {
                    return null;
                }
            } else if (!mergeItemStack(slot_stack, 0, slotCount, false)) {
                return null;
            }

            if (slot_stack.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return inventory.isUsableByPlayer(player);
    }

    public void addPlayerInventory(EntityPlayer player) {
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                addSlotToContainer(new Slot(player.inventory, x + y * 9 + 9, xInventoryDisplacement + x * 18, yInventoryDisplacement + y * 18));
            }
        }

        for (int x = 0; x < 9; x++) {
            addSlotToContainer(new Slot(player.inventory, x, xInventoryDisplacement + x * 18, yHotBarDisplacement));
        }
    }
}