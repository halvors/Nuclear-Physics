package org.halvors.nuclearphysics.client.jei;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.item.ItemStack;
import org.halvors.nuclearphysics.client.gui.process.GuiChemicalExtractor;
import org.halvors.nuclearphysics.common.block.machine.BlockMachine.EnumMachine;
import org.halvors.nuclearphysics.common.container.process.ContainerChemicalExtractor;
import org.halvors.nuclearphysics.common.init.ModBlocks;

import javax.annotation.Nonnull;

@JEIPlugin
public class JEIIntegration extends BlankModPlugin implements IModPlugin {
    @Override
    public void register(@Nonnull IModRegistry registry) {
        registry.addRecipeHandlers(new RecipeHandlerSolderer());


        //registry.getRecipeTransferRegistry().addUniversalRecipeTransferHandler(new RecipeTransferHandlerPattern());
        //registry.getRecipeTransferRegistry().addRecipeTransferHandler(new RecipeTransferHandlerGrid(), "minecraft.crafting");
        registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerChemicalExtractor.class, RecipeCategorySolderer.ID, 0, 3, 8, 36);

        registry.addRecipeCategories(new RecipeCategorySolderer(registry.getJeiHelpers().getGuiHelper()));

        registry.addRecipeHandlers(new RecipeHandlerSolderer());

        registry.addRecipes(RecipeMakerSolderer.getRecipes());

        registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.blockMachineModel, 1, EnumMachine.CHEMICAL_EXTRACTOR.ordinal()), RecipeCategorySolderer.ID);

        //registry.addAdvancedGuiHandlers(new GuiHandlerGrid());

        registry.addRecipeClickArea(GuiChemicalExtractor.class, 80, 36, 22, 15, RecipeCategorySolderer.ID);
    }
}
