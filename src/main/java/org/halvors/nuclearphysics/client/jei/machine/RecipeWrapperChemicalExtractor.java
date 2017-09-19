package org.halvors.nuclearphysics.client.jei.machine;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class RecipeWrapperChemicalExtractor extends BlankRecipeWrapper {
    private ItemStack input;
    private ItemStack output;

    public RecipeWrapperChemicalExtractor(ItemStack input, ItemStack output) {
        this.input = input;
        this.output = output;
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        ingredients.setInput(ItemStack.class, input);
        ingredients.setOutput(ItemStack.class, output);
    }

    @Override
    @SuppressWarnings("deprecation")
    public List<ItemStack> getInputs() {
        return Collections.singletonList(input);
    }

    @Override
    @SuppressWarnings("deprecation")
    public List<ItemStack> getOutputs() {
        return Collections.singletonList(output);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RecipeWrapperChemicalExtractor)) {
            return false;
        }

        RecipeWrapperChemicalExtractor other = (RecipeWrapperChemicalExtractor) obj;

        /*
        for (int i = 0; i < input.size(); i++) {
            if (!ItemStack.areItemStacksEqual(inputs.get(i), other.inputs.get(i))) {
                return false;
            }
        }
        */

        return ItemStack.areItemStacksEqual(output, other.output);
    }

    @Override
    public String toString() {
        return input + " = " + output;
    }
}
