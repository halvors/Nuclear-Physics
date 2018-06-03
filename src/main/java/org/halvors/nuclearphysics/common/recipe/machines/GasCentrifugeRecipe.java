package org.halvors.nuclearphysics.common.recipe.machines;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import org.halvors.nuclearphysics.common.recipe.inputs.FluidInput;
import org.halvors.nuclearphysics.common.recipe.outputs.FluidOutput;

public class GasCentrifugeRecipe extends MachineRecipe<FluidInput, FluidOutput, GasCentrifugeRecipe> {
    public GasCentrifugeRecipe(FluidStack fluidStackInput, FluidStack fluidStackOutput) {
        super(new FluidInput(fluidStackInput), new FluidOutput(fluidStackOutput));
    }

    public GasCentrifugeRecipe(final FluidInput fluidInput, final FluidOutput fluidOutput) {
        super(fluidInput, fluidOutput);
    }

    @Override
    public GasCentrifugeRecipe copy() {
        return new GasCentrifugeRecipe(getInput(), getOutput());
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
