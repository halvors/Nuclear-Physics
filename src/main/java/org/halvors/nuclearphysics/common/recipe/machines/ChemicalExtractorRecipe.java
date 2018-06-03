package org.halvors.nuclearphysics.common.recipe.machines;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import org.halvors.nuclearphysics.common.recipe.inputs.FluidInput;
import org.halvors.nuclearphysics.common.recipe.outputs.FluidOutput;

public class ChemicalExtractorRecipe extends MachineRecipe<FluidInput, FluidOutput, ChemicalExtractorRecipe> {
    public ChemicalExtractorRecipe(FluidStack fluidStackInput, FluidStack fluidStackOutput) {
        super(new FluidInput(fluidStackInput), new FluidOutput(fluidStackOutput));
    }

    public ChemicalExtractorRecipe(final FluidInput fluidInput, final FluidOutput fluidOutput) {
        super(fluidInput, fluidOutput);
    }

    @Override
    public ChemicalExtractorRecipe copy() {
        return new ChemicalExtractorRecipe(getInput(), getOutput());
    }

    public boolean canOperate(final FluidTank fluidTankInput, final FluidTank fluidTankOutput) {
        return getInput().useFluid(fluidTankInput, false, 1) && getOutput().applyOutputs(fluidTankOutput, false);
    }

    public void operate(final FluidTank inputTank, final FluidTank outputTank) {
        if (getInput().useFluid(inputTank, true, 1)) {
            getOutput().applyOutputs(outputTank, true);
        }
    }
}
