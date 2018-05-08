package org.halvors.nuclearphysics.api.recipe;


import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class QuantumAssemblerRecipes {
    private static final List<ItemStack> RECIPES = new ArrayList<>();

    public static boolean hasRecipe(ItemStack itemStack) {
        for (ItemStack output : RECIPES) {
            if (output.isItemEqual(itemStack)) {
                return true;
            }
        }

        return false;
    }

    public static void addRecipe(ItemStack itemStack) {
        if (itemStack != null) {
            if (itemStack.isStackable()) {
                RECIPES.add(itemStack);
            }
        }
    }
}
