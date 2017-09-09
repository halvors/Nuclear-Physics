package org.halvors.nuclearphysics.api.recipe;


import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class QuantumAssemblerRecipes {
    public static final List<ItemStack> recipes = new ArrayList<>();

    public static boolean hasRecipe(ItemStack itemStack) {
        for (ItemStack output : recipes) {
            if (output.isItemEqual(itemStack)) {
                return true;
            }
        }

        return false;
    }

    public static void addRecipe(ItemStack itemStack) {
        if (!itemStack.isEmpty()) {
            if (itemStack.isStackable()) {
                recipes.add(itemStack);
            }
        }
    }
}
