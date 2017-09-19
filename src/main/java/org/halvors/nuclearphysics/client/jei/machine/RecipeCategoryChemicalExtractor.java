package org.halvors.nuclearphysics.client.jei.machine;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import org.halvors.nuclearphysics.common.Reference;
import org.halvors.nuclearphysics.common.block.machine.BlockMachine;
import org.halvors.nuclearphysics.common.type.Resource;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;

import javax.annotation.Nonnull;

public class RecipeCategoryChemicalExtractor extends BlankRecipeCategory<RecipeWrapperChemicalExtractor> {
    public static final String ID = "refinedstorage.solderer";

    private IDrawable background;

    public RecipeCategoryChemicalExtractor(IGuiHelper helper) {
        background = helper.createBlankDrawable(10, 10);//teDrawable(ResourceUtility.getResource(Resource.GUI, "base.png"), 43, 19, 101, 54);
    }

    @Override
    public String getUid() {
        return ID;
    }

    @Override
    public String getTitle() {
        return I18n.format("tile." + Reference.ID + ".machine." + BlockMachine.EnumMachine.CHEMICAL_EXTRACTOR.ordinal() + ".name");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull RecipeWrapperChemicalExtractor recipeWrapper, @Nonnull IIngredients ingredients) {
        IGuiItemStackGroup group = recipeLayout.getItemStacks();

        group.init(0, false, 0, 18);
        group.init(1, false, 80, 18);
        group.set(0, ingredients.getInputs(ItemStack.class).get(0));
        group.set(1, ingredients.getOutputs(ItemStack.class).get(0));
    }
}
