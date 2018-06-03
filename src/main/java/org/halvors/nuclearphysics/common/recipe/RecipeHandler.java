package org.halvors.nuclearphysics.common.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.halvors.nuclearphysics.common.recipe.inputs.FluidInput;
import org.halvors.nuclearphysics.common.recipe.inputs.ItemStackInput;
import org.halvors.nuclearphysics.common.recipe.inputs.MachineInput;
import org.halvors.nuclearphysics.common.recipe.machines.BasicMachineRecipe;
import org.halvors.nuclearphysics.common.recipe.machines.ChemicalExtractorRecipe;
import org.halvors.nuclearphysics.common.recipe.machines.GasCentrifugeRecipe;
import org.halvors.nuclearphysics.common.recipe.machines.MachineRecipe;
import org.halvors.nuclearphysics.common.recipe.outputs.FluidOutput;
import org.halvors.nuclearphysics.common.recipe.outputs.ItemStackOutput;
import org.halvors.nuclearphysics.common.recipe.outputs.MachineOutput;
import org.halvors.nuclearphysics.common.utility.InventoryUtility;

import java.lang.reflect.Constructor;
import java.util.*;

/**
 * Class used to handle machine recipes. This is used for both adding and fetching recipes.
 *
 * @author AidanBrady, unpairedbracket
 */
public final class RecipeHandler {
    public static void addRecipe(Recipe recipeMap, MachineRecipe recipe) {
        recipeMap.put(recipe);
    }

    public static void removeRecipe(Recipe recipeMap, MachineRecipe recipe) {
        List<MachineInput> toRemove = new ArrayList<>();

        for (Iterator iter = recipeMap.recipes.keySet().iterator(); iter.hasNext(); ) {
            MachineInput iterInput = (MachineInput) iter.next();

            if (iterInput.testEquality(recipe.getInput())) {
                toRemove.add(iterInput);
            }
        }

        for (MachineInput iterInput : toRemove) {
            recipeMap.recipes.remove(iterInput);
        }
    }

    /**
     * Gets the BasicMachineRecipe of the ItemStackInput in the parameters, using the map in the parameters.
     *
     * @param input   - ItemStackInput
     * @param recipes - Map of recipes
     * @return BasicMachineRecipe
     */
    public static <RECIPE extends BasicMachineRecipe<RECIPE>> RECIPE getRecipe(ItemStackInput input, Map<ItemStackInput, RECIPE> recipes) {
        if (input.isValid()) {
            RECIPE recipe = getRecipeTryWildcard(input, recipes);
            return recipe == null ? null : recipe.copy();
        }

        return null;
    }

    /**
     * Gets the whether the input ItemStack is in a recipe
     *
     * @param itemstack - input ItemStack
     * @param recipes   - Map of recipes
     * @return whether the item can be used in a recipe
     */
    public static <RECIPE extends MachineRecipe<ItemStackInput, ?, RECIPE>> boolean isInRecipe(ItemStack itemstack, Map<ItemStackInput, RECIPE> recipes) {
        if (itemstack != null) {
            for (RECIPE recipe : recipes.values()) {
                ItemStackInput required = recipe.getInput();

                if (required.useItemStackFromInventory(new ItemStack[]{itemstack}, 0, false)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static <RECIPE extends MachineRecipe<ItemStackInput, ?, RECIPE>> RECIPE getRecipeTryWildcard(ItemStack stack, Map<ItemStackInput, RECIPE> recipes) {
        return getRecipeTryWildcard(new ItemStackInput(stack), recipes);
    }

    public static <RECIPE extends MachineRecipe<ItemStackInput, ?, RECIPE>> RECIPE getRecipeTryWildcard(ItemStackInput input, Map<ItemStackInput, RECIPE> recipes) {
        RECIPE recipe = recipes.get(input);

        if (recipe == null) {
            recipe = recipes.get(input.wildCopy());
        }

        return recipe;
    }

    /**
     * Add an Enrichment Chamber recipe.
     *
     * @param input  - input ItemStack
     * @param output - output ItemStack
     */
    public static void addChemicalExtractorRecipe(FluidStack input, FluidStack output) {
        addRecipe(Recipe.CHEMICAL_EXTRACTOR, new GasCentrifugeRecipe(input, output));
    }

    public enum Recipe {
        CHEMICAL_EXTRACTOR("chemical_extractor", ItemStackInput.class, ItemStackOutput.class, ChemicalExtractorRecipe.class); // TODO: get string from enum.

        private HashMap recipes;
        private String recipeName;

        private Class<? extends MachineInput> inputClass;
        private Class<? extends MachineOutput> outputClass;
        private Class<? extends MachineRecipe> recipeClass;

        <INPUT extends MachineInput<INPUT>, OUTPUT extends MachineOutput<OUTPUT>, RECIPE extends MachineRecipe<INPUT, ?, RECIPE>> Recipe(String name, Class<INPUT> input, Class<OUTPUT> output, Class<RECIPE> recipe) {
            recipeName = name;

            inputClass = input;
            outputClass = output;
            recipeClass = recipe;

            recipes = new HashMap<INPUT, RECIPE>();
        }

        public <RECIPE extends MachineRecipe<?, ?, RECIPE>> void put(RECIPE recipe) {
            recipes.put(recipe.getInput(), recipe);
        }

        public <RECIPE extends MachineRecipe<?, ?, RECIPE>> void remove(RECIPE recipe) {
            recipes.remove(recipe.getInput());
        }

        public String getRecipeName() {
            return recipeName;
        }

        public <INPUT> INPUT createInput(NBTTagCompound nbtTags) {
            try {
                MachineInput input = inputClass.newInstance();
                input.load(nbtTags);

                return (INPUT) input;
            } catch (Exception e) {
                return null;
            }
        }

        public <RECIPE, INPUT> RECIPE createRecipe(INPUT input, NBTTagCompound nbtTags) {
            try {
                MachineOutput output = outputClass.newInstance();
                output.load(nbtTags);

                try {
                    Constructor<? extends MachineRecipe> construct = recipeClass.getDeclaredConstructor(inputClass, outputClass);

                    return (RECIPE) construct.newInstance(input, output);
                } catch (Exception e) {
                    Constructor<? extends MachineRecipe> construct = recipeClass.getDeclaredConstructor(inputClass, outputClass, NBTTagCompound.class);

                    return (RECIPE) construct.newInstance(input, output, nbtTags);
                }
            } catch (Exception e) {
                return null;
            }
        }

        public boolean containsRecipe(ItemStack input) {
            for (Object obj : recipes.entrySet()) {
                if (obj instanceof Map.Entry) {
                    Map.Entry entry = (Map.Entry) obj;

                    if (entry.getKey() instanceof ItemStackInput) {
                        ItemStack stack = ((ItemStackInput) entry.getKey()).ingredient;

                        if (InventoryUtility.equalsWildcard(stack, input)) {
                            return true;
                        }
                    } else if (entry.getKey() instanceof FluidInput) {
                        if (((FluidInput) entry.getKey()).getIngredient().isFluidEqual(input)) {
                            return true;
                        }
                    /*
                    } else if (entry.getKey() instanceof AdvancedMachineInput) {
                        ItemStack stack = ((AdvancedMachineInput) entry.getKey()).itemStack;

                        if (InventoryUtility.equalsWildcard(stack, input)) {
                            return true;
                        }
                    */
                    }
                }
            }

            return false;
        }

        public boolean containsRecipe(Fluid input) {
            for (Object obj : recipes.entrySet()) {
                if (obj instanceof Map.Entry) {
                    Map.Entry entry = (Map.Entry) obj;

                    if (entry.getKey() instanceof FluidInput) {
                        if (((FluidInput) entry.getKey()).getIngredient().getFluid() == input) {
                            return true;
                        }
                    }
                }
            }

            return false;
        }

        /*
        public boolean containsRecipe(Gas input) {
            for (Object obj : get().entrySet()) {
                if (obj instanceof Map.Entry) {
                    Map.Entry entry = (Map.Entry) obj;

                    if (entry.getKey() instanceof GasInput) {
                        if (((GasInput) entry.getKey()).ingredient.getGas() == input) {
                            return true;
                        }
                    }
                }
            }

            return false;
        }
        */

        public HashMap getRecipes() {
            return recipes;
        }
    }
}
