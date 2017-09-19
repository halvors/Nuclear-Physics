package org.halvors.nuclearphysics.client.jei.machine;

import net.minecraft.item.ItemStack;
import org.halvors.nuclearphysics.common.init.ModBlocks;
import org.halvors.nuclearphysics.common.init.ModItems;

import java.util.ArrayList;
import java.util.List;

public final class RecipeMakerChemicalExtractor {
    public static List<RecipeWrapperChemicalExtractor> getRecipes() {
        List<RecipeWrapperChemicalExtractor> recipes = new ArrayList<>();
        recipes.add(new RecipeWrapperChemicalExtractor(new ItemStack(ModBlocks.blockUraniumOre), new ItemStack(ModItems.itemYellowCake)));

        return recipes;
    }
}
