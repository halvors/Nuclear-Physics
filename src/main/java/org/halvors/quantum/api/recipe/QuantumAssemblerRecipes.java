package org.halvors.quantum.api.recipe;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class QuantumAssemblerRecipes {
    public static final List<ItemStack> recipes = new ArrayList<ItemStack>();

    public static boolean hasItemStack(ItemStack itemStack) {
        for (ItemStack output : recipes) {
            if (output.isItemEqual(itemStack)) {
                return true;
            }
        }

        return false;
    }

    public static void addRecipe(ItemStack itemStack) {
        if (itemStack != null) {
            if (itemStack.isStackable()) {
                recipes.add(itemStack);
            }
        }
    }
}
