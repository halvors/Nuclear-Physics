package org.halvors.quantum.lib.prefab.tile;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.quantum.lib.tile.IExternalInventory;
import org.halvors.quantum.lib.tile.IExternalInventoryBox;
import org.halvors.quantum.lib.utility.inventory.ExternalInventory;

public class TileElectricalInventory extends TileElectrical implements IExternalInventory, ISidedInventory {
    protected IExternalInventoryBox inventory;
    protected int maxSlots = 1;

    public TileElectricalInventory() {
        super(null);
    }

    public TileElectricalInventory(Material material) {
        super(material);
    }

    @Override
    public int[] getSlotsForFace(int slotIn) {
        return getInventory().getSlotsForFace(slotIn);
    }

    @Override
    public boolean canInsertItem(int index, ItemStack stack, int side) {
        return getInventory().canInsertItem(index, stack, side);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, int side) {
        return getInventory().canExtractItem(index, stack, side);
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

    public void incrStackSize(int slot, ItemStack stack) {
        if (getStackInSlot(slot) == null) {
            setInventorySlotContents(slot, stack.copy());
        } else if (getStackInSlot(slot).isItemEqual(stack)) {
            getStackInSlot(slot).stackSize += stack.stackSize;
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
        getInventory().closeChest();
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return getInventory().isItemValidForSlot(index, stack);
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
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        getInventory().load(nbt);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);

        getInventory().save(nbt);
    }
}

/*
public Class<? extends Container> getContainer()
{
    return null;
}


*/
