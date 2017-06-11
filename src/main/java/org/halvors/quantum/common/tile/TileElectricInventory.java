package org.halvors.quantum.common.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.quantum.lib.tile.IExternalInventory;
import org.halvors.quantum.lib.tile.IExternalInventoryBox;
import org.halvors.quantum.lib.utility.inventory.ExternalInventory;

public class TileElectricInventory extends TileElectricStorage implements IExternalInventory, ISidedInventory {
    protected IExternalInventoryBox inventory;
    protected int maxSlots = 1;

    public TileElectricInventory() {

    }

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
}