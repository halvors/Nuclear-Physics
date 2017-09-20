package org.halvors.nuclearphysics.common.recipe.machine;

import net.minecraft.item.ItemStack;
import org.halvors.nuclearphysics.common.recipe.input.ItemStackInput;
import org.halvors.nuclearphysics.common.recipe.output.ItemStackOutput;

public class ChemicalExtractorRecipe extends BasicMachineRecipe<ChemicalExtractorRecipe> {
    public ChemicalExtractorRecipe(ItemStackInput input, ItemStackOutput output) {
        super(input, output);
    }

    public ChemicalExtractorRecipe(ItemStack input, ItemStack output) {
        super(input, output);
    }

    @Override
    public ChemicalExtractorRecipe copy() {
        return new ChemicalExtractorRecipe(getInput().copy(), getOutput().copy());
    }
}
