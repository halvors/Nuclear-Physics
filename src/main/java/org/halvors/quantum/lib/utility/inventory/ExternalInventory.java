package org.halvors.quantum.lib.utility.inventory;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.quantum.lib.tile.IExternalInventory;
import org.halvors.quantum.lib.tile.IExternalInventoryBox;

public class ExternalInventory implements IExternalInventoryBox {
    /**
     * Access able slots side all
     */
    protected int[] openSlots;
    /**
     * Items contained in this inv
     */
    protected ItemStack[] containedItems;
    /**
     * Host tileEntity
     */
    protected TileEntity hostTile;
    /**
     * Host tileEntity as external inv
     */
    protected IExternalInventory inventory;
    /**
     * Default slot max count
     */
    protected final int slots;

    public ExternalInventory(TileEntity chest, IExternalInventory inventory, int slots) {
        this.hostTile = chest;
        this.inventory = inventory;
        this.slots = slots;
    }

    public ExternalInventory(TileEntity chest, int slots) {
        this(chest, ((IExternalInventory) chest), slots);
    }

    public ExternalInventory(Entity entity, int i) {
        this.slots = i;
        this.inventory = (IExternalInventory) entity;
    }

    @Override
    public ItemStack[] getContainedItems() {
        if (containedItems == null) {
            containedItems = new ItemStack[getSizeInventory()];
        }

        return this.containedItems;
    }

    @Override
    public void clear() {
        containedItems = null;
        getContainedItems();
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
    public boolean canInsertItem(int i, ItemStack itemstack, int j) {
        return isItemValidForSlot(i, itemstack) && inventory.canStore(itemstack, i, ForgeDirection.getOrientation(j));
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return inventory.canRemove(itemstack, i, ForgeDirection.getOrientation(j));
    }

    @Override
    public int getSizeInventory() {
        return slots;
    }

    @Override
    public ItemStack getStackInSlot(int slotIn) {
        if (slotIn < getContainedItems().length) {
            return this.getContainedItems()[slotIn];
        }

        return null;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (getContainedItems()[index] != null) {
            ItemStack itemStack;

            if (getContainedItems()[index].stackSize <= count) {
                itemStack = this.getContainedItems()[index];
                getContainedItems()[index] = null;

                markDirty();

                return itemStack;
            } else {
                itemStack = getContainedItems()[index].splitStack(count);

                if (getContainedItems()[index].stackSize == 0) {
                    getContainedItems()[index] = null;
                }

                markDirty();

                return itemStack;
            }
        }

        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int index) {
        if (this.getContainedItems()[index] != null) {
            ItemStack itemStack = getContainedItems()[index];
            getContainedItems()[index] = null;

            return itemStack;
        }

        return null;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack itemStack = getContainedItems()[index] != null ? this.getContainedItems()[index].copy() : null;
        getContainedItems()[index] = stack;

        if (stack != null && stack.stackSize > getInventoryStackLimit()) {
            stack.stackSize = getInventoryStackLimit();
        }

        if (!InventoryUtility.stacksMatchExact(itemStack, getContainedItems()[index])) {
            markDirty();
        }
    }

    @Override
    public String getInventoryName() {
        return "container.chest";
    }

    @Override
    public boolean isCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {
        if (hostTile != null) {
            hostTile.markDirty();
        }
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return hostTile == null || (hostTile.getWorld().getTileEntity(this.hostTile.xCoord, this.hostTile.yCoord, this.hostTile.zCoord) == this.hostTile && player.getDistanceSq(this.hostTile.xCoord + 0.5D, this.hostTile.yCoord + 0.5D, this.hostTile.zCoord + 0.5D) <= 64.0D);

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

    @Override
    public void save(NBTTagCompound nbtTagCompound) {
        NBTTagList list = new NBTTagList();

        for (int i = 0; i < getSizeInventory(); ++i) {
            if (getStackInSlot(i) != null) {
                NBTTagCompound stackTagCompound = new NBTTagCompound();
                stackTagCompound.setByte("Slot", (byte) i);
                getStackInSlot(i).writeToNBT(stackTagCompound);
                list.appendTag(stackTagCompound);
            }
        }

        nbtTagCompound.setTag("Items", list);
    }

    @Override
    public void load(NBTTagCompound tagCompound) {
        clear();

        NBTTagList list = tagCompound.getTagList("Items", 0);

        for (int i = 0; i < list.tagCount(); ++i) {
            NBTTagCompound stackTagCompound = list.getCompoundTagAt(i);
            int slot = stackTagCompound.getByte("Slot") & 255;
            setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(stackTagCompound));
        }

        tagCompound.setTag("Items", list);
    }
}


