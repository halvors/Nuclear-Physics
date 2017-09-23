package org.halvors.nuclearphysics.common.utility;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.halvors.nuclearphysics.common.init.ModItems;

public class OreDictionaryHelper {
    // Items
    public static boolean isEmptyCell(ItemStack itemStack) {
        return hasOreNames(itemStack, "cellEmpty");
    }

    public static boolean isDarkmatterCell(ItemStack itemStack) {
        return hasOreNames(itemStack, "cellDarkmatter");
    }

    public static boolean isDeuteriumCell(ItemStack itemStack) {
        return hasOreNames(itemStack, "cellDeuterium");
    }

    public static boolean isTritiumCell(ItemStack itemStack) {
        return hasOreNames(itemStack, "cellTritium");
    }

    public static boolean isWaterCell(ItemStack itemStack) {
        return hasOreNames(itemStack, "cellWater");
    }

    public static boolean isYellowCake(ItemStack itemStack) {
        return itemStack.getItem() == ModItems.itemYellowCake || hasOreNames(itemStack, "dustUranium");
    }

    public static boolean isUranium(ItemStack itemStack) {
        return itemStack.getItem() == ModItems.itemUranium || hasOreNames(itemStack, "ingotUranium", "ingotUranium235", "ingotUranium238");
    }

    // Blocks
    public static boolean isUraniumOre(ItemStack itemStack) {
        return hasOreNames(itemStack, "oreUranium");
    }

    /**
     * Compare to Ore Dict
     */
    public static boolean hasOreNames(ItemStack itemStack, String... names) {
        if (!itemStack.isEmpty() && names != null && names.length > 0) {
            for (int id : OreDictionary.getOreIDs(itemStack)) {
                String name = OreDictionary.getOreName(id);

                for (String compareName : names) {
                    if (name.equals(compareName)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}


