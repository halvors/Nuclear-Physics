package org.halvors.nuclearphysics.api.recipe;


import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class QuantumAssemblerRecipes {
    private static final List<Item> recipes = new ArrayList<>();

    public static boolean hasRecipe(Item item) {
        return recipes.contains(item);
    }

    public static void addRecipe(Item item) {
        if (item != null) {
            recipes.add(item);
        }
    }
}
