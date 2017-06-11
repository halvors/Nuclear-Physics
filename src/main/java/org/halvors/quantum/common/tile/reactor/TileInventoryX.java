package org.halvors.quantum.common.tile.reactor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.quantum.common.transform.vector.Vector3;
import org.halvors.quantum.common.transform.vector.VectorWorld;
import org.halvors.quantum.lib.tile.IExternalInventory;
import org.halvors.quantum.lib.tile.IExternalInventoryBox;
import org.halvors.quantum.lib.utility.inventory.ExternalInventory;
import org.halvors.quantum.lib.utility.inventory.InventoryUtility;

/*
 * Prefab for tiles that need a basic inventory
 */
public class TileInventoryX extends TileEntity implements IExternalInventory, ISidedInventory {
    public TileInventoryX() {

    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        getInventory().load(nbt);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt){
        super.writeToNBT(nbt);
        getInventory().save(nbt);
    }

    protected IExternalInventoryBox inventory;
    protected int maxSlots = 1;

    @Override
    public IExternalInventoryBox getInventory() {
        if (inventory == null) {
            inventory = new ExternalInventory(this, maxSlots);
        }

        return inventory;
    }

    @Override
    public int getSizeInventory() {
        return this.getInventory().getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return this.getInventory().getStackInSlot(i);
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        return this.getInventory().decrStackSize(i, j);
    }

    public void incrStackSize(int slot, ItemStack itemStack) {
        if (this.getStackInSlot(slot) == null) {
            setInventorySlotContents(slot, itemStack.copy());
        } else if (this.getStackInSlot(slot).isItemEqual(itemStack)) {
            getStackInSlot(slot).stackSize += itemStack.stackSize;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        return this.getInventory().getStackInSlotOnClosing(i);
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
        this.getInventory().setInventorySlotContents(i, itemStack);
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
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return this.getInventory().isUseableByPlayer(entityplayer);
    }

    @Override
    public void openChest() {
        getInventory().openChest();
    }

    @Override
    public void closeChest() {
        getInventory().closeChest();
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return getInventory().isItemValidForSlot(i, itemstack);
    }

    @Override
    public int[] getSlotsForFace(int side) {
        return getInventory().getSlotsForFace(side);
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, int j) {
        return getInventory().canInsertItem(i, itemstack, j);
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return getInventory().canExtractItem(i, itemstack, j);
    }

    @Override
    public boolean canStore(ItemStack stack, int slot, ForgeDirection side) {
        return false;
    }

    @Override
    public boolean canRemove(ItemStack stack, int slot, ForgeDirection side) {
        return slot < this.getSizeInventory();
    }

    /** Player-Inventory interaction methods. */
    public boolean interactCurrentItem(int slotId, EntityPlayer player) {
        return interactCurrentItem(this, slotId, player);
    }

    public boolean interactCurrentItem(IInventory inventory, int slotID, EntityPlayer player) {
        ItemStack stackInInventory = inventory.getStackInSlot(slotID);
        ItemStack current = player.inventory.getCurrentItem();

        /** Try to insert. */
        if (current != null) {
            if (stackInInventory == null || ItemStack.areItemStacksEqual(stackInInventory, current)) {
                return insertCurrentItem(inventory, slotID, player);
            }
        }

        /** Try to extract. */
        return extractItem(inventory, slotID, player);
    }

    public boolean insertCurrentItem(IInventory inventory, int slotID, EntityPlayer player) {
        ItemStack stackInInventory = inventory.getStackInSlot(slotID);
        ItemStack current = player.inventory.getCurrentItem();

        if (current != null) {
            if (stackInInventory == null || ItemStack.areItemStacksEqual(stackInInventory, current)) {
                if (inventory.isItemValidForSlot(slotID, current)) {
                    /** If control is down, insert one only. */
                    if (player.isSneaking()) { // isControlDown(player)
                        if (stackInInventory == null) {
                            inventory.setInventorySlotContents(slotID, current.splitStack(1));
                        } else {
                            stackInInventory.stackSize++;
                            current.stackSize--;
                        }
                    } else {
                        if (stackInInventory == null) {
                            inventory.setInventorySlotContents(slotID, current);
                        } else {
                            stackInInventory.stackSize += current.stackSize;
                            current.stackSize = 0;
                        }

                        current = null;
                    }

                    if (current == null || current.stackSize <= 0) {
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                    }

                    return true;
                }
            }
        }

        return false;
    }

    public boolean extractItem(IInventory inventory, int slotID, EntityPlayer player) {
        ItemStack stackInInventory = inventory.getStackInSlot(slotID);

        if (stackInInventory != null) {
            // If control is down, insert one only.
            if (player.isSneaking()) { // isControlDown(player)
                InventoryUtility.dropItemStack(player.worldObj, new Vector3(player), stackInInventory.splitStack(1), 0);
            } else {
                InventoryUtility.dropItemStack(player.worldObj, new Vector3(player), stackInInventory, 0);
                stackInInventory = null;
            }

            if (stackInInventory == null || stackInInventory.stackSize <= 0) {
                inventory.setInventorySlotContents(slotID, null);
            }

            return true;
        }

        return false;
    }

    /*
    @Override
    public void onRemove(int par5, int par6) {
        super.onRemove(par5, par6);
        dropEntireInventory(par5, par6);
    }
    */

    public void dropEntireInventory(int par5, int par6) {
        IInventory inventory = this;

        for (int i = 0; i < inventory.getSizeInventory(); ++i) {
            ItemStack dropStack = inventory.getStackInSlot(i);

            if (dropStack != null) {
                int var11 = dropStack.stackSize;
                dropStack.stackSize -= var11;
                InventoryUtility.dropItemStack(worldObj, new VectorWorld(this).translate(0.5), dropStack);

                if (dropStack.stackSize <= 0) {
                    inventory.setInventorySlotContents(i, null);
                }
            }
        }
    }
}
