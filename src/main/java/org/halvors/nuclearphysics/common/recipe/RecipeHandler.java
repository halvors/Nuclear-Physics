package org.halvors.nuclearphysics.common.recipe;

/*
public final class RecipeHandler {
    public static void addGasCentrifugeRecipe(FluidStack input, ItemStack output) {
        addRecipe(Recipe.GAS_CENTRIFUGE, new GasCentrifugeRecipe(input, output));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static GasCentrifugeRecipe getGasCentrifugeRecipe(FluidInput input) {
        if (input.isValid()) {
            HashMap<FluidInput, GasCentrifugeRecipe> recipes = Recipe.GAS_CENTRIFUGE.get();

            GasCentrifugeRecipe recipe = recipes.get(input);

            NuclearPhysics.getLogger().info("Recipe is: " + (recipe == null ? "null" : "not null"));

            return recipe == null ? null : recipe.copy();
        }

        return null;
    }

    public enum Recipe {
        GAS_CENTRIFUGE(EnumMachine.GAS_CENTRIFUGE.getName(), FluidInput.class, ItemStackOutput.class, GasCentrifugeRecipe.class);

*/

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.recipe.input.ItemStackInput;
import org.halvors.nuclearphysics.common.recipe.machine.GasCentrifugeRecipe;
import org.halvors.nuclearphysics.common.recipe.machine.MachineRecipe;
import org.halvors.nuclearphysics.common.recipe.old.input.AdvancedMachineInput;
import org.halvors.nuclearphysics.common.recipe.old.input.FluidInput;
import org.halvors.nuclearphysics.common.recipe.old.input.MachineInput;
import org.halvors.nuclearphysics.common.recipe.old.machine.AdvancedMachineRecipe;
import org.halvors.nuclearphysics.common.recipe.old.machine.BasicMachineRecipe;
import org.halvors.nuclearphysics.common.recipe.old.output.MachineOutput;
import org.halvors.nuclearphysics.common.recipe.output.ItemStackOutput;
import org.halvors.nuclearphysics.common.utility.OreDictionaryHelper;

import java.lang.reflect.Constructor;
import java.util.*;

/**
 * Class used to handle machine recipes. This is used for both adding and fetching recipes.
 */
public final class RecipeHandler {
    public static void addRecipe(Recipe recipeMap, MachineRecipe recipe) {
        recipeMap.put(recipe);
    }

    public static void removeRecipe(Recipe recipeMap, MachineRecipe recipe) {
        List<MachineInput> toRemove = new ArrayList<>();

        for (Iterator iter = recipeMap.get().keySet().iterator(); iter.hasNext(); ) {
            MachineInput iterInput = (MachineInput) iter.next();

            if (iterInput.testEquality(recipe.getInput())) {
                toRemove.add(iterInput);
            }
        }

        for (MachineInput iterInput : toRemove) {
            recipeMap.get().remove(iterInput);
        }
    }

    public static void addGasCentrifugeRecipe(FluidStack input, ItemStack output) {
        addRecipe(Recipe.GAS_CENTRIFUGE, new GasCentrifugeRecipe(input, output));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static GasCentrifugeRecipe getGasCentrifugeRecipe(FluidInput input) {
        if (input.isValid()) {
            HashMap<FluidInput, GasCentrifugeRecipe> recipes = Recipe.GAS_CENTRIFUGE.get();

            GasCentrifugeRecipe recipe = recipes.get(input);

            NuclearPhysics.getLogger().info("Recipe is: " + (Recipe.GAS_CENTRIFUGE.get().get(input) == null ? "null" : "not null"));

            return recipe == null ? null : recipe.copy();
        }

        return null;
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
     * Gets the AdvancedMachineRecipe of the AdvancedInput in the parameters, using the map in the paramaters.
     *
     * @param input   - AdvancedInput
     * @param recipes - Map of recipes
     * @return AdvancedMachineRecipe
     */
    public static <RECIPE extends AdvancedMachineRecipe<RECIPE>> RECIPE getRecipe(AdvancedMachineInput input, Map<AdvancedMachineInput, RECIPE> recipes) {
        if (input.isValid()) {
            RECIPE recipe = recipes.get(input);
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

                if (required.useItemStackFromInventory(itemstack, false)) {
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

    public enum Recipe {
        GAS_CENTRIFUGE("GasCentrifuge", FluidInput.class, ItemStackOutput.class, GasCentrifugeRecipe.class);

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
            for (Object obj : get().entrySet()) {
                if (obj instanceof Map.Entry) {
                    Map.Entry entry = (Map.Entry) obj;

                    if (entry.getKey() instanceof ItemStackInput) {
                        ItemStack stack = ((ItemStackInput) entry.getKey()).ingredient;

                        if (OreDictionaryHelper.equalsWildcard(stack, input)) {
                            return true;
                        }
                    } else if (entry.getKey() instanceof FluidInput) {
                        if (((FluidInput) entry.getKey()).ingredient.isFluidEqual(input)) {
                            return true;
                        }
                    } else if (entry.getKey() instanceof AdvancedMachineInput) {
                        ItemStack stack = ((AdvancedMachineInput) entry.getKey()).itemStack;

                        if (OreDictionaryHelper.equalsWildcard(stack, input)) {
                            return true;
                        }
                    }
                }
            }

            return false;
        }

        public boolean containsRecipe(Fluid input) {
            for (Object obj : get().entrySet()) {
                if (obj instanceof Map.Entry) {
                    Map.Entry entry = (Map.Entry) obj;

                    if (entry.getKey() instanceof FluidInput) {
                        if (((FluidInput) entry.getKey()).ingredient.getFluid() == input) {
                            return true;
                        }
                    }
                }
            }

            return false;
        }

        public HashMap get() {
            return recipes;
        }
    }
}
