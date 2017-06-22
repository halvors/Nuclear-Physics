package org.halvors.quantum.common.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;

import java.util.HashSet;

public class TileElectricInventory extends TileElectric implements ISidedInventory {
    private ItemStack[] inventory;
    private int[] openSlots;

    public TileElectricInventory(int maxSlots) {
        inventory = new ItemStack[maxSlots];
    }

    public TileElectricInventory() {
        this(1);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public int[] getSlotsForFace(int side) {
        if (openSlots == null || openSlots.length != getSizeInventory()) {
            openSlots = new int[getSizeInventory()];

            for (int i = 0; i < openSlots.length; i++) {
                openSlots[i] = i;
            }
        }

        return openSlots;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemstack, int side) {
        return isItemValidForSlot(index, itemstack);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack itemstack, int side) {
        return true;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public int getSizeInventory() {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventory != null ? inventory[slot] : null;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (getStackInSlot(index) != null) {
            ItemStack tempStack;

            if (getStackInSlot(index).stackSize <= count) {
                tempStack = getStackInSlot(index);
                setInventorySlotContents(index, null);
                return tempStack;
            } else {
                tempStack = getStackInSlot(index).splitStack(count);

                if(getStackInSlot(index).stackSize == 0) {
                    setInventorySlotContents(index, null);
                }

                return tempStack;
            }
        }

        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int index) {
        if (getStackInSlot(index) != null) {
            ItemStack tempStack = getStackInSlot(index);
            setInventorySlotContents(index, null);

            return tempStack;
        }

        return null;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack itemStack) {
        inventory[index] = itemStack;

        if (itemStack != null && itemStack.stackSize > getInventoryStackLimit()) {
            itemStack.stackSize = getInventoryStackLimit();
        }

        markDirty();
    }

    @Override
    public String getInventoryName() {
        return getBlockType().getLocalizedName();
    }

    @Override
    public boolean isCustomInventoryName() {
        return true;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return player.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64.0D;
    }

    @Override
    public void openChest() {

    }

    @Override
    public void closeChest() {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return index < getSizeInventory();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void incrStackSize(int slot, ItemStack itemStack) {
        if (getStackInSlot(slot) == null) {
            setInventorySlotContents(slot, itemStack.copy());
        } else if (getStackInSlot(slot).isItemEqual(itemStack)) {
            getStackInSlot(slot).stackSize += itemStack.stackSize;
        }

        markDirty();
    }
}