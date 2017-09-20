package org.halvors.nuclearphysics.common.recipe.input;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.oredict.OreDictionary;
import org.halvors.nuclearphysics.common.recipe.old.input.MachineInput;
import org.halvors.nuclearphysics.common.utility.OreDictionaryHelper;

public class ItemStackInput extends MachineInput<ItemStackInput> {
    public ItemStack ingredient;

    public ItemStackInput(ItemStack stack) {
        ingredient = stack;
    }

    public ItemStackInput() {
    }

    @Override
    public void load(NBTTagCompound nbtTags) {
        ingredient = ItemStack.loadItemStackFromNBT(nbtTags.getCompoundTag("input"));
    }

    @Override
    public ItemStackInput copy() {
        return new ItemStackInput(ingredient.copy());
    }

    @Override
    public boolean isValid() {
        return ingredient != null;
    }

    public ItemStackInput wildCopy() {
        return new ItemStackInput(new ItemStack(ingredient.getItem(), ingredient.stackSize, OreDictionary.WILDCARD_VALUE));
    }

    public boolean useItemStackFromInventory(IItemHandlerModifiable inventory, int index, boolean deplete) {
        return useItemStackFromInventory(inventory.getStackInSlot(index), deplete);
    }

    public boolean useItemStackFromInventory(ItemStack itemStack, boolean deplete) {
        if (inputContains(itemStack, ingredient)) {
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
        return OreDictionaryHelper.equalsWildcardWithNBT(ingredient, other.ingredient);
    }
}
