package org.halvors.nuclearphysics.common.recipe.machine;

import org.halvors.nuclearphysics.common.recipe.input.MachineInput;
import org.halvors.nuclearphysics.common.recipe.output.IOutput;

public abstract class MachineRecipe<I extends MachineInput, O extends IOutput, R extends MachineRecipe<I, O, R>> {
    private I recipeInput;
    private O recipeOutput;

    public MachineRecipe(I input, O output) {
        recipeInput = input;
        recipeOutput = output;
    }

    public I getInput() {
        return recipeInput;
    }

    public O getOutput() {
        return recipeOutput;
    }

    public abstract R copy();
}
