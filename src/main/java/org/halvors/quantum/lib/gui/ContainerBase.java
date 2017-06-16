package org.halvors.quantum.lib.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.halvors.quantum.lib.IPlayerUsing;

public class ContainerBase extends Container {
    protected int slotCount = 0;
    protected int xInventoryDisplacement = 8;
    protected int yInventoryDisplacement = 135;
    protected int yHotBarDisplacement = 193;
    private IInventory inventory;

    public ContainerBase(IInventory inventory) {
        this.inventory = inventory;
        this.slotCount = inventory.getSizeInventory();
    }

    public ContainerBase(EntityPlayer player, IInventory inventory) {
        this(inventory);

        if (inventory instanceof IPlayerUsing) {
            ((IPlayerUsing) inventory).getPlayersUsing().add(player);
        }
    }

    // Called to transfer a stack from one inventory to the other eg. when shift clicking.
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotId) {
        ItemStack itemstack = null;
        Slot slot = (Slot) inventorySlots.get(slotId);

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
        return inventory.isUseableByPlayer(player);
    }

    @Override
    public void onContainerClosed(EntityPlayer entityplayer) {
        if (inventory instanceof IPlayerUsing) {
            ((IPlayerUsing) inventory).getPlayersUsing().remove(entityplayer);
        }

        super.onContainerClosed(entityplayer);
    }

    public void addPlayerInventory(EntityPlayer player) {
        if (this.inventory instanceof IPlayerUsing) {
            ((IPlayerUsing) inventory).getPlayersUsing().add(player);
        }

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