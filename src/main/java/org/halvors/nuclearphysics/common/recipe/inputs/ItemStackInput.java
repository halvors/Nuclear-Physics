package org.halvors.nuclearphysics.common.recipe.inputs;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import org.halvors.nuclearphysics.common.utility.ItemStackUtility;

public class ItemStackInput extends MachineInput<ItemStackInput> {
    public ItemStack ingredient;

    public ItemStackInput(ItemStack itemStack) {
        ingredient = itemStack;
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

    public boolean useItemStackFromInventory(ItemStack[] inventory, int index, boolean deplete) {
        if (inputContains(inventory[index], ingredient)) {
            if (deplete) {
                inventory[index] = ItemStackUtility.subtract(inventory[index], ingredient);
            }

            return true;
        }

        return false;
    }

    @Override
    public int hashIngredients() {
        return ItemStackUtility.hashItemStack(ingredient);
    }

    @Override
    public boolean testEquality(ItemStackInput other) {
        return ItemStackUtility.equalsWildcardWithNBT(ingredient, other.ingredient);
    }

    @Override
    public boolean isInstance(Object other) {
        return other instanceof ItemStackInput;
    }
}
