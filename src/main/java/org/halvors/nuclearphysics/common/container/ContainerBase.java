package org.halvors.nuclearphysics.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.halvors.nuclearphysics.common.tile.TileBase;

public class ContainerBase<T extends TileBase> extends Container {
    protected IInventory inventory;
    protected T tile;
    protected int slotCount;

    protected int xInventoryDisplacement = 8;
    protected int yInventoryDisplacement = 135;
    protected int yHotBarDisplacement = 193;

    public ContainerBase(int slotCount, InventoryPlayer inventory, T tile) {
        this.slotCount = slotCount;
        this.inventory = inventory;
        this.tile = tile;

        if (tile != null) {
            tile.open(inventory.player);
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return inventory.isUsableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        final Slot slot = inventorySlots.get(index);

        if (slot != null && !slot.getStack().isEmpty()) {
            final ItemStack itemStack = slot.getStack();
            final ItemStack originalStack = itemStack.copy();

            if (index < slotCount) {
                if (!mergeItemStack(itemStack, slotCount, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!mergeItemStack(itemStack, 0, slotCount, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            return originalStack;
        }

        return ItemStack.EMPTY;
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);

        if (tile != null) {
            tile.close(player);
        }
    }

    protected void addPlayerInventory(EntityPlayer player) {
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