package org.halvors.nuclearphysics.common.recipe.old.machine;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import org.halvors.nuclearphysics.common.recipe.old.input.AdvancedMachineInput;
import org.halvors.nuclearphysics.common.recipe.output.ItemStackOutput;

public class ChemicalExtractorRecipe extends AdvancedMachineRecipe<ChemicalExtractorRecipe> {
    public ChemicalExtractorRecipe(AdvancedMachineInput input, ItemStackOutput output) {
        super(input, output);
    }

    public ChemicalExtractorRecipe(ItemStack input, Fluid fluid, ItemStack output) {
        super(input, fluid, output);
    }

    @Override
    public ChemicalExtractorRecipe copy() {
        return new ChemicalExtractorRecipe(getInput().copy(), getOutput().copy());
    }
}