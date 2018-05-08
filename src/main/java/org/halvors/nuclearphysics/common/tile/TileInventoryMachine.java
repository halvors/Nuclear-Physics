package org.halvors.nuclearphysics.common.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import org.halvors.nuclearphysics.common.block.machine.BlockMachine.EnumMachine;

public abstract class TileInventoryMachine extends TileMachine implements ISidedInventory {
    private static final String NBT_SLOTS = "slots";

    private ItemStack[] inventory;
    private int[] openSlots;

    public TileInventoryMachine() {

    }

    public TileInventoryMachine(final EnumMachine type, final int maxSlots) {
        super(type);

        inventory = new ItemStack[maxSlots];
    }

    @Override
    public void readFromNBT(final NBTTagCompound tag) {
        super.readFromNBT(tag);

        NBTTagList tagList = tag.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);

        for (int tagCount = 0; tagCount < tagList.tagCount(); tagCount++) {
            NBTTagCompound slotTagCompound = tagList.getCompoundTagAt(tagCount);
            byte index = slotTagCompound.getByte("Slot");

            if (index >= 0 && index < getSizeInventory()) {
                setInventorySlotContents(index, ItemStack.loadItemStackFromNBT(slotTagCompound));
            }
        }

        //InventoryUtility.readFromNBT(tag, inventory);
    }

    @Override
    public void writeToNBT(final NBTTagCompound tag) {
        super.writeToNBT(tag);

        NBTTagList tagList = new NBTTagList();

        for (int index = 0; index < getSizeInventory(); index++) {
            if (getStackInSlot(index) != null) {
                NBTTagCompound slotTagCompound = new NBTTagCompound();
                slotTagCompound.setByte("Slot", (byte) index);
                getStackInSlot(index).writeToNBT(slotTagCompound);
                tagList.appendTag(slotTagCompound);
            }
        }

        tag.setTag("Inventory", tagList);
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

    @Override
    public int getSizeInventory() {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventory[slot];
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
    public ItemStack getStackInSlotOnClosing(final int index) {
        if (getStackInSlot(index) != null) {
            ItemStack tempStack = getStackInSlot(index);
            setInventorySlotContents(index, null);

            return tempStack;
        }

        return null;
    }

    @Override
    public void setInventorySlotContents(final int index, final ItemStack itemStack) {
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
    public boolean isUseableByPlayer(final EntityPlayer player) {
        return player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) <= 64;
    }

    @Override
    public void openChest() {

    }

    @Override
    public void closeChest() {

    }

    @Override
    public boolean isItemValidForSlot(final int index, final ItemStack stack) {
        return index < getSizeInventory();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void incrStackSize(final int slot, final ItemStack itemStack) {
        if (getStackInSlot(slot) == null) {
            setInventorySlotContents(slot, itemStack.copy());
        } else if (getStackInSlot(slot).isItemEqual(itemStack)) {
            getStackInSlot(slot).stackSize += itemStack.stackSize;
        }

        markDirty();
    }
}
