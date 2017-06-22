package org.halvors.quantum.common.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.quantum.lib.tile.IExternalInventory;
import org.halvors.quantum.lib.tile.IExternalInventoryBox;
import org.halvors.quantum.lib.utility.inventory.ExternalInventory;

public class TileInventory extends TileEntity implements ISidedInventory {
    // The inventory slot itemstacks used by this block.
    private ItemStack[] inventory;
    protected int[] openSlots;
    protected int maxSlots = 1;

    public TileInventory() {
        inventory = new ItemStack[maxSlots];
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);

        NBTTagList tagList = tagCompound.getTagList("Items", Constants.NBT.TAG_COMPOUND);

        for (int tagCount = 0; tagCount < tagList.tagCount(); tagCount++) {
            NBTTagCompound slotTagCompound = tagList.getCompoundTagAt(tagCount);
            byte slotID = tagCompound.getByte("Slot");

            if (slotID >= 0 && slotID < getSizeInventory()) {
                setInventorySlotContents(slotID, ItemStack.loadItemStackFromNBT(slotTagCompound));
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        NBTTagList tagList = new NBTTagList();

        for (int slotCount = 0; slotCount < getSizeInventory(); slotCount++) {
            if (getStackInSlot(slotCount) != null) {
                NBTTagCompound slotTagCompound = new NBTTagCompound();
                slotTagCompound.setByte("Slot", (byte) slotCount);
                getStackInSlot(slotCount).writeToNBT(slotTagCompound);
                tagList.appendTag(slotTagCompound);
            }
        }

        tagCompound.setTag("Items", tagList);
    }

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

    /*
    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);

        if (getInventory() != null) {
            getInventory().load(tagCompound);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        if (getInventory() != null) {
            getInventory().save(tagCompound);
        }
    }

    @Override
    public IExternalInventoryBox getInventory() {
        if (inventory == null) {
            inventory = new ExternalInventory(this, maxSlots);
        }

        return inventory;
    }

    @Override
    public boolean canStore(ItemStack stack, int slot, ForgeDirection side) {
        return false;
    }

    @Override
    public boolean canRemove(ItemStack stack, int slot, ForgeDirection side) {
        return slot < getSizeInventory();
    }

    @Override
    public int[] getSlotsForFace(int face) {
        return getInventory().getSlotsForFace(face);
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemStack, int j) {
        return getInventory().canInsertItem(i, itemStack, j);
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemStack, int j) {
        return getInventory().canExtractItem(i, itemStack, j);
    }

    @Override
    public int getSizeInventory() {
        return getInventory().getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int slotIn) {
        return getInventory().getStackInSlot(slotIn);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return getInventory().decrStackSize(index, count);
    }

    public void incrStackSize(int slot, ItemStack itemStack) {
        if (getStackInSlot(slot) == null) {
            setInventorySlotContents(slot, itemStack.copy());
        } else if (getStackInSlot(slot).isItemEqual(itemStack)) {
            getStackInSlot(slot).stackSize += itemStack.stackSize;
        }

        markDirty();
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int index) {
        return getInventory().getStackInSlotOnClosing(index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        getInventory().setInventorySlotContents(index, stack);
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
        return getInventory().getInventoryStackLimit();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return getInventory().isUseableByPlayer(player);
    }

    @Override
    public void openChest() {
        getInventory().openChest();
    }

    @Override
    public void closeChest() {
        getInventory().openChest();
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack itemStack) {
        return getInventory().isItemValidForSlot(index, itemStack);
    }
    */
}
