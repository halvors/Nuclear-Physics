package org.halvors.nuclearphysics.common.utility;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.halvors.nuclearphysics.common.init.ModItems;

public class OreDictionaryHelper {
    // Items
    public static boolean isEmptyCell(final ItemStack itemStack) {
        return hasOreNames(itemStack, "cellEmpty");
    }

    public static boolean isDarkmatterCell(final ItemStack itemStack) {
        return hasOreNames(itemStack, "cellDarkmatter");
    }

    public static boolean isDeuteriumCell(final ItemStack itemStack) {
        return hasOreNames(itemStack, "cellDeuterium");
    }

    public static boolean isTritiumCell(final ItemStack itemStack) {
        return hasOreNames(itemStack, "cellTritium");
    }

    public static boolean isWaterCell(final ItemStack itemStack) {
        return hasOreNames(itemStack, "cellWater");
    }

    public static boolean isYellowCake(final ItemStack itemStack) {
        return itemStack.getItem() == ModItems.itemYellowCake || hasOreNames(itemStack, "dustUranium");
    }

    public static boolean isUranium(final ItemStack itemStack) {
        return itemStack.getItem() == ModItems.itemUranium || hasOreNames(itemStack, "ingotUranium", "ingotUranium235", "ingotUranium238");
    }

    // Blocks
    public static boolean isUraniumOre(final ItemStack itemStack) {
        return hasOreNames(itemStack, "oreUranium");
    }

    /**
     * Compare to Ore Dict
     */
    public static boolean hasOreNames(final ItemStack itemStack, final String... names) {
        if (!itemStack.isEmpty() && names != null && names.length > 0) {
            for (int id : OreDictionary.getOreIDs(itemStack)) {
                final String name = OreDictionary.getOreName(id);

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


