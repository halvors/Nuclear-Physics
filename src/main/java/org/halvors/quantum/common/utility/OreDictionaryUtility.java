package org.halvors.quantum.common.utility;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictionaryUtility {
    public static boolean isItemStackOreDictionaryCompatible(ItemStack itemStack, String... names) {
        if (itemStack != null && names != null && names.length > 0) {
            String name = OreDictionary.getOreName(OreDictionary.getOreID(itemStack));

            for (String compareName : names) {
                if (name.equals(compareName)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isEmptyCell(ItemStack itemStack) {
        return isItemStackOreDictionaryCompatible(itemStack, "cellEmpty");
    }

    public static boolean isWaterCell(ItemStack itemStack) {
        return isItemStackOreDictionaryCompatible(itemStack, "cellWater");
    }

    public static boolean isUraniumOre(ItemStack itemStack) {
        return isItemStackOreDictionaryCompatible(itemStack, "dropUranium", "oreUranium");
    }

    public static boolean isDeuteriumCell(ItemStack itemStack) {
        return isItemStackOreDictionaryCompatible(itemStack, "molecule_1d", "molecule_1h2", "cellDeuterium");
    }

    public static boolean isTritiumCell(ItemStack itemStack) {
        return isItemStackOreDictionaryCompatible(itemStack, "molecule_h3", "cellTritium");
    }
}


