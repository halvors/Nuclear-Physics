package org.halvors.nuclearphysics.common.recipe.machines;

import org.halvors.nuclearphysics.common.recipe.inputs.MachineInput;
import org.halvors.nuclearphysics.common.recipe.outputs.MachineOutput;

public abstract class MachineRecipe<INPUT extends MachineInput, OUTPUT extends MachineOutput, RECIPE extends MachineRecipe<INPUT, OUTPUT, RECIPE>> {
    public INPUT recipeInput;
    public OUTPUT recipeOutput;

    public MachineRecipe(INPUT input, OUTPUT output) {
        recipeInput = input;
        recipeOutput = output;
    }

    public INPUT getInput() {
        return recipeInput;
    }

    public OUTPUT getOutput() {
        return recipeOutput;
    }

    public abstract RECIPE copy();
}
