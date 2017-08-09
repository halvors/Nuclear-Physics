package org.halvors.quantum.common.utility;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Utility that manages common NBT save and load methods
 */
public class NBTUtility {
    public static NBTTagCompound getNBTTagCompound(ItemStack itemStack) {
        if (itemStack != null) {
            if (itemStack.getTagCompound() == null) {
                itemStack.setTagCompound(new NBTTagCompound());
            }

            return itemStack.getTagCompound();
        }

        return null;
    }
}
