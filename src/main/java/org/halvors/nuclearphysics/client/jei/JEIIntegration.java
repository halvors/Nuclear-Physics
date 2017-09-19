package org.halvors.nuclearphysics.client.jei;

import mezz.jei.api.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.halvors.nuclearphysics.client.gui.process.GuiChemicalExtractor;
import org.halvors.nuclearphysics.client.jei.machine.RecipeCategoryChemicalExtractor;
import org.halvors.nuclearphysics.client.jei.machine.RecipeHandlerChemicalExtractor;
import org.halvors.nuclearphysics.client.jei.machine.RecipeMakerChemicalExtractor;
import org.halvors.nuclearphysics.common.block.machine.BlockMachine.EnumMachine;
import org.halvors.nuclearphysics.common.container.process.ContainerChemicalExtractor;
import org.halvors.nuclearphysics.common.init.ModBlocks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@JEIPlugin
public class JEIIntegration extends BlankModPlugin implements IModPlugin {
    @Override
    public void registerItemSubtypes(@Nonnull ISubtypeRegistry subtypeRegistry) {
        subtypeRegistry.registerSubtypeInterpreter(Item.getItemFromBlock(ModBlocks.blockMachine), new ISubtypeRegistry.ISubtypeInterpreter() {
            @Nullable
            @Override
            public String getSubtypeInfo(ItemStack itemStack) {
                return Integer.toString(itemStack.getMetadata());
            }
        });
    }

    @Override
    public void register(@Nonnull IModRegistry registry) {
        registry.addRecipeHandlers(new RecipeHandlerChemicalExtractor());

        //registry.getRecipeTransferRegistry().addUniversalRecipeTransferHandler(new RecipeTransferHandlerPattern());
        //registry.getRecipeTransferRegistry().addRecipeTransferHandler(new RecipeTransferHandlerGrid(), "minecraft.crafting");
        registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerChemicalExtractor.class, RecipeCategoryChemicalExtractor.ID, 0, 3, 8, 36);

        registry.addRecipeCategories(new RecipeCategoryChemicalExtractor(registry.getJeiHelpers().getGuiHelper()));

        registry.addRecipeHandlers(new RecipeHandlerChemicalExtractor());

        registry.addRecipes(RecipeMakerChemicalExtractor.getRecipes());

        registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.blockMachine, 1, EnumMachine.CHEMICAL_EXTRACTOR.ordinal()), RecipeCategoryChemicalExtractor.ID);

        //registry.addAdvancedGuiHandlers(new GuiHandlerGrid());

        registry.addRecipeClickArea(GuiChemicalExtractor.class, 80, 36, 22, 15, RecipeCategoryChemicalExtractor.ID);
    }
}
