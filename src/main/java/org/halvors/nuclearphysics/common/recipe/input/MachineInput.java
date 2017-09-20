package org.halvors.nuclearphysics.common.recipe.input;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.halvors.nuclearphysics.common.utility.OreDictionaryHelper;

public abstract class MachineInput<INPUT extends MachineInput<INPUT>> {
    public abstract boolean isValid();

    public abstract INPUT copy();

    public abstract void load(NBTTagCompound tag);

    /**
     * Test equality to another input.
     * This should return true if the input matches this one,
     * IGNORING AMOUNTS.
     * Allows usage of HashMap optimisation to get recipes.
     *
     * @param other
     * @return
     */
    public abstract boolean testEquality(INPUT other);

    public static boolean inputContains(ItemStack container, ItemStack contained) {
        return container != null && container.stackSize >= contained.stackSize && OreDictionaryHelper.equalsWildcardWithNBT(contained, container) && container.stackSize >= contained.stackSize;
    }
}
