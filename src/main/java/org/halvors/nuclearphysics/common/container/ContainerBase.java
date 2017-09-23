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

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        final Slot slot = inventorySlots.get(index);

        if (slot != null && slot.getStack() != null) {
            final ItemStack itemStack = slot.getStack();
            final ItemStack originalStack = itemStack.copy();

            if (index < slotCount) {
                if (!mergeItemStack(itemStack, slotCount, inventorySlots.size(), true)) {
                    return null;
                }
            } else if (!mergeItemStack(itemStack, 0, slotCount, false)) {
                return null;
            }

            if (itemStack.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }

            return originalStack;
        }

        return null;
    }

    /*
    @Override
    public ItemStack transferStackInSlot(final EntityPlayer player, final int index) {
        final Slot slot = inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) { // For 1.11+ !slot.getStack().isEmpty()) {
            final ItemStack itemStack = slot.getStack();
            final ItemStack originalStack = itemStack.copy();

            if (itemStack.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }

            return originalStack;
        }

        return null;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int fromSlot) {
        ItemStack previous = null;
        Slot slot = inventorySlots.get(fromSlot);

        if (slot != null && slot.getHasStack()) {
            ItemStack current = slot.getStack();
            previous = current.copy();

            // [...] Custom behaviour

            if (current.stackSize == 0)
                slot.putStack((ItemStack) null);
            else
                slot.onSlotChanged();

            if (current.stackSize == previous.stackSize)
                return null;
            slot.onPickupFromSlot(playerIn, current);
        }
        return previous;
    }

    // Called to transfer a stack from one inventory to the other eg. when shift clicking.
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotId) {
        ItemStack stack = null;
        Slot currentSlot = inventorySlots.get(slotId);

        if (currentSlot != null && currentSlot.getHasStack()) {
            ItemStack slotStack = currentSlot.getStack();
            stack = slotStack.copy();

            if (slotId == 2) {
                if (!mergeItemStack(slotStack, 3, inventorySlots.size(), true)) {
                    return null;
                }
            } else if (RecipeHandler.getRecipe(new ItemStackInput(slotStack), tileEntity.getRecipes()) != null) {
                if (slotId != 0 && slotId != 1 && slotId != 2) {
                    if (!mergeItemStack(slotStack, 0, 1, false)) {
                        return null;
                    }
                } else {
                    if (!mergeItemStack(slotStack, 3, inventorySlots.size(), true)) {
                        return null;
                    }
                }
            } else if (ChargeUtils.canBeDischarged(slotStack)) {
                if (slotId != 1) {
                    if (!mergeItemStack(slotStack, 1, 2, false)) {
                        return null;
                    }
                } else if (slotId == 1) {
                    if (!mergeItemStack(slotStack, 3, inventorySlots.size(), true)) {
                        return null;
                    }
                }
            } else {
                if (slotId >= 3 && slotId <= 29) {
                    if (!mergeItemStack(slotStack, 30, inventorySlots.size(), false)) {
                        return null;
                    }
                } else if (slotId > 29) {
                    if (!mergeItemStack(slotStack, 3, 29, false)) {
                        return null;
                    }
                } else {
                    if (!mergeItemStack(slotStack, 3, inventorySlots.size(), true)) {
                        return null;
                    }
                }
            }

            if (slotStack.stackSize == 0) {
                currentSlot.putStack(null);
            } else {
                currentSlot.onSlotChanged();
            }

            if (slotStack.stackSize == stack.stackSize) {
                return null;
            }

            currentSlot.onPickupFromSlot(player, slotStack);
        }

        return stack;
    }

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
    */
}