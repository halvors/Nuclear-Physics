package org.halvors.nuclearphysics.common.utility;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictionaryHelper {
    // Items
    public static boolean isFissileFuel(ItemStack itemStack) {
        return hasOreName(itemStack, "fuelFissile");
    }

    public static boolean isBreederFuel(ItemStack itemStack) {
        return hasOreName(itemStack, "fuelBreeder");
    }

    public static boolean isFuel(ItemStack itemStack) {
        return isFissileFuel(itemStack) || isBreederFuel(itemStack);
    }

    public static boolean isEmptyCell(ItemStack itemStack) {
        return hasOreName(itemStack, "cellEmpty");
    }

    public static boolean isDarkmatterCell(ItemStack itemStack) {
        return hasOreName(itemStack, "cellDarkmatter");
    }

    public static boolean isDeuteriumCell(ItemStack itemStack) {
        return hasOreName(itemStack, "cellDeuterium");
    }

    public static boolean isTritiumCell(ItemStack itemStack) {
        return hasOreName(itemStack, "cellTritium");
    }

    public static boolean isWaterCell(ItemStack itemStack) {
        return hasOreName(itemStack, "cellWater");
    }

    public static boolean isYellowCake(ItemStack itemStack) {
        return hasOreName(itemStack, "dustUranium");
    }

    public static boolean isUranium(ItemStack itemStack) {
        return hasOreName(itemStack, "ingotUranium") || hasOreName(itemStack, "itemUranium");
    }

    // Blocks
    public static boolean isUraniumOre(ItemStack itemStack) {
        return hasOreName(itemStack, "oreUranium");
    }

    public static boolean isRadioactiveGrass(ItemStack itemStack) {
        return hasOreName(itemStack, "blockRadioactiveGrass");
    }

    private static boolean hasOreName(ItemStack itemStack, String oreName) {
        if (itemStack != null) {
            int oreId = OreDictionary.getOreID(oreName);

            for (int id : OreDictionary.getOreIDs(itemStack)) {
                if (id == oreId) {
                    return true;
                }
            }
        }

        return false;
    }
}


