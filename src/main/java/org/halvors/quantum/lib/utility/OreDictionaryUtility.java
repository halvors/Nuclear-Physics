package org.halvors.quantum.lib.utility;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictionaryUtility {
    public static boolean isItemStackOreDictionaryCompatible(ItemStack itemStack, String... names) {
        if (itemStack != null && names != null && names.length > 0) {
            for (String oreName : OreDictionary.getOreNames()) {
                for (String compareOreName : names) {
                    if (oreName.equals(compareOreName)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static boolean  isItemStackWaterCell(ItemStack itemStack) {
        return isItemStackOreDictionaryCompatible(itemStack, "cellWater");
    }

    public static boolean  isItemStackUraniumOre(ItemStack itemStack) {
        return isItemStackOreDictionaryCompatible(itemStack, "dropUranium", "oreUranium");
    }

    public static boolean  isItemStackDeuteriumCell(ItemStack itemStack) {
        return isItemStackOreDictionaryCompatible(itemStack, "molecule_1d", "molecule_1h2", "cellDeuterium");
    }

    public static boolean  isItemStackTritiumCell(ItemStack itemStack) {
        return isItemStackOreDictionaryCompatible(itemStack, "molecule_h3", "cellTritium");
    }
}


