package org.halvors.nuclearphysics.client.jei.machine;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

public class RecipeHandlerChemicalExtractor implements IRecipeHandler<RecipeWrapperChemicalExtractor> {
    @Override
    @Nonnull
    public Class<RecipeWrapperChemicalExtractor> getRecipeClass() {
        return RecipeWrapperChemicalExtractor.class;
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public String getRecipeCategoryUid() {
        return RecipeCategoryChemicalExtractor.ID;
    }

    @Override
    @Nonnull
    public String getRecipeCategoryUid(@Nonnull RecipeWrapperChemicalExtractor recipe) {
        return RecipeCategoryChemicalExtractor.ID;
    }

    @Override
    @Nonnull
    public IRecipeWrapper getRecipeWrapper(@Nonnull RecipeWrapperChemicalExtractor recipe) {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull RecipeWrapperChemicalExtractor recipe) {
        return true;
    }
}
