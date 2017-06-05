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
    protected IExternalInventory inv;
    /**
     * Default slot max count
     */
    protected final int slots;

    public ExternalInventory(TileEntity chest, IExternalInventory inv, int slots) {
        this.hostTile = chest;
        this.slots = slots;
        this.inv = inv;
    }

    public ExternalInventory(TileEntity chest, int slots) {
        this(chest, ((IExternalInventory) chest), slots);
    }

    public ExternalInventory(Entity entity, int i) {
        this.slots = i;
        this.inv = (IExternalInventory) entity;
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
        return isItemValidForSlot(i, itemstack) && inv.canStore(itemstack, i, ForgeDirection.getOrientation(j));
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return inv.canRemove(itemstack, i, ForgeDirection.getOrientation(j));
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
        if (hostTile != null) {
            return hostTile.getWorld().getTileEntity(this.hostTile.xCoord, this.hostTile.yCoord, this.hostTile.zCoord) != this.hostTile ? false : player.getDistanceSq(this.hostTile.xCoord + 0.5D, this.hostTile.yCoord + 0.5D, this.hostTile.zCoord + 0.5D) <= 64.0D;
        }

        return true;
    }

    @Override
    public void openChest() {

    }

    @Override
    public void closeChest() {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (index >= getSizeInventory()) {
            return false;
        }

        return true;
    }

    @Override
    public void save(NBTTagCompound nbtTagCompound) {
        NBTTagList nbtList = new NBTTagList();

        for (int i = 0; i < this.getSizeInventory(); ++i) {
            if (getStackInSlot(i) != null) {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) i);
                getStackInSlot(i).writeToNBT(var4);
                nbtList.appendTag(var4);
            }
        }

        nbtTagCompound.setTag("Items", nbtList);
    }

    @Override
    public void load(NBTTagCompound nbtTagCompound) {
        clear();

        NBTTagList nbtList = nbtTagCompound.getTagList("Items", 0);

        for (int i = 0; i < nbtList.tagCount(); ++i) {
            NBTTagCompound stackTag = nbtList.getCompoundTagAt(i);
            byte id = stackTag.getByte("Slot");

            if (id >= 0 && id < getSizeInventory()) {
                setInventorySlotContents(id, ItemStack.loadItemStackFromNBT(stackTag));
            }
        }

        nbtTagCompound.setTag("Items", nbtList);
    }
}


