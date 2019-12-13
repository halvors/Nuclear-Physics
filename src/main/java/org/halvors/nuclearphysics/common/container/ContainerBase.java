package org.halvors.nuclearphysics.common.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import org.halvors.nuclearphysics.common.tile.TileBase;

import javax.annotation.Nonnull;

public class ContainerBase<T extends TileBase> extends Container {
    protected final IInventory inventory;
    protected final T tile;
    protected final int slotCount;
    protected final int xInventoryDisplacement = 8;
    protected int yInventoryDisplacement = 135;
    protected int yHotBarDisplacement = 193;


    public ContainerBase(final int slotCount, final PlayerInventory playerInventory, final T tile) {
        this.slotCount = slotCount;
        this.inventory = playerInventory;
        this.tile = tile;
    }

    @Override
    public boolean canInteractWith(@Nonnull final PlayerEntity player) {
        return inventory.isUsableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(final PlayerEntity player, final int index) {
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

    protected void addPlayerInventory(final PlayerEntity player) {
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                addSlot(new Slot(player.inventory, x + y * 9 + 9, xInventoryDisplacement + x * 18, yInventoryDisplacement + y * 18));
            }
        }

        for (int x = 0; x < 9; x++) {
            addSlot(new Slot(player.inventory, x, xInventoryDisplacement + x * 18, yHotBarDisplacement));
        }
    }
}