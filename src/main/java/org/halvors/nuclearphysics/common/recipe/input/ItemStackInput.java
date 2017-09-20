package org.halvors.nuclearphysics.common.recipe.input;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.oredict.OreDictionary;
import org.halvors.nuclearphysics.common.utility.OreDictionaryHelper;

public class ItemStackInput extends MachineInput<ItemStackInput> {
    public ItemStack itemStack;

    public ItemStackInput(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public void load(NBTTagCompound nbtTags) {
        itemStack = ItemStack.loadItemStackFromNBT(nbtTags.getCompoundTag("input"));
    }

    @Override
    public ItemStackInput copy() {
        return new ItemStackInput(itemStack.copy());
    }

    @Override
    public boolean isValid() {
        return itemStack != null;
    }

    public ItemStackInput wildCopy() {
        return new ItemStackInput(new ItemStack(itemStack.getItem(), itemStack.stackSize, OreDictionary.WILDCARD_VALUE));
    }

    public boolean useItemStackFromInventory(IItemHandlerModifiable inventory, int index, boolean deplete) {
        return useItemStackFromInventory(inventory.getStackInSlot(index), deplete);
    }

    public boolean useItemStackFromInventory(ItemStack itemStack, boolean deplete) {
        if (inputContains(itemStack, itemStack)) {
            if (deplete) {
                // TODO: Consume item?
                //inventory[index] = StackUtils.subtract(inventory[index], ingredient);
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean testEquality(ItemStackInput other) {
        return OreDictionaryHelper.equalsWildcardWithNBT(itemStack, other.itemStack);
    }
}
