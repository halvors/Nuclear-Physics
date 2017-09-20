package org.halvors.nuclearphysics.common.recipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.recipe.input.FluidInput;
import org.halvors.nuclearphysics.common.recipe.input.ItemStackInput;
import org.halvors.nuclearphysics.common.recipe.input.MachineInput;
import org.halvors.nuclearphysics.common.recipe.machine.BasicMachineRecipe;
import org.halvors.nuclearphysics.common.recipe.machine.ChemicalExtractorRecipe;
import org.halvors.nuclearphysics.common.recipe.machine.GasCentrifugeRecipe;
import org.halvors.nuclearphysics.common.recipe.machine.MachineRecipe;
import org.halvors.nuclearphysics.common.recipe.output.IOutput;
import org.halvors.nuclearphysics.common.recipe.output.ItemStackOutput;

import java.util.HashMap;
import java.util.Map;

/**
 * Class used to handle machine recipes. This is used for both adding and fetching recipes.
 */
public final class RecipeHandler {
    public static void addRecipe(Recipe recipeMap, MachineRecipe recipe) {
        recipeMap.put(recipe);
    }

    public static void addChemicalExtractorRecipe(ItemStack input, ItemStack output) {
        addRecipe(Recipe.CHEMICAL_EXTRACTOR, new ChemicalExtractorRecipe(input, output));
    }

    public static void addGasCentrifugeRecipe(FluidStack input, ItemStack output) {
        addRecipe(Recipe.GAS_CENTRIFUGE, new GasCentrifugeRecipe(input, output));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // TODO: Generify this...
    public static ChemicalExtractorRecipe getRecipe(ItemStackInput input, Map<ItemStackInput, ChemicalExtractorRecipe> recipes) {
        if (input.isValid()) {
            ChemicalExtractorRecipe recipe = getRecipeTryWildcard(input, recipes);

            if (recipe != null) {
                return recipe.copy();
            }
        }

        return null;
    }

    public static <R extends MachineRecipe<ItemStackInput, ?, R>> R getRecipeTryWildcard(ItemStackInput input, Map<ItemStackInput, R> recipes) {
        R recipe = recipes.get(input);

        if (recipe == null) {
            recipe = recipes.get(input.wildCopy());
        }

        return recipe;
    }

    public static GasCentrifugeRecipe getGasCentrifugeRecipe(FluidInput input) {
        if (input.isValid()) {
            Map<FluidInput, GasCentrifugeRecipe> recipes = Recipe.GAS_CENTRIFUGE.get();
            GasCentrifugeRecipe recipe = recipes.get(input);

            NuclearPhysics.getLogger().info("Recipe is: " + (recipe == null ? "null" : "not null"));

            if (recipe != null) {
                return recipe.copy();
            }
        }

        return null;
    }

    public enum Recipe {
        CHEMICAL_EXTRACTOR(ItemStackInput.class, ItemStackOutput.class, ChemicalExtractorRecipe.class),
        GAS_CENTRIFUGE(FluidInput.class, ItemStackOutput.class, GasCentrifugeRecipe.class);

        private Map recipes;
        private Class<? extends MachineInput> inputClass;
        private Class<? extends IOutput> outputClass;
        private Class<? extends MachineRecipe> recipeClass;

        <I extends MachineInput<I>, O extends IOutput<O>, R extends MachineRecipe<I, ?, R>> Recipe(Class<I> input, Class<O> output, Class<R> recipe) {
            this.recipes = new HashMap<I, R>();
            this.inputClass = input;
            this.outputClass = output;
            this.recipeClass = recipe;
        }

        public <R extends MachineRecipe<?, ?, R>> void put(R recipe) {
            recipes.put(recipe.getInput(), recipe);
        }

        public <R extends MachineRecipe<?, ?, R>> void remove(R recipe) {
            recipes.remove(recipe.getInput());
        }

        public Map get() {
            return recipes;
        }
    }
}
