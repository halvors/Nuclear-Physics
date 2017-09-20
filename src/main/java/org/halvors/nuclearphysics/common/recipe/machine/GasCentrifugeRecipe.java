package org.halvors.nuclearphysics.common.recipe.machine;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.halvors.nuclearphysics.common.recipe.old.input.FluidInput;
import org.halvors.nuclearphysics.common.recipe.output.ItemStackOutput;

public class GasCentrifugeRecipe extends MachineRecipe<FluidInput, ItemStackOutput, GasCentrifugeRecipe> {
    public GasCentrifugeRecipe(FluidInput input, ItemStackOutput output) {
        super(input, output);
    }

    public GasCentrifugeRecipe(FluidStack input, ItemStack output) {
        this(new FluidInput(input), new ItemStackOutput(output));
    }

    public boolean canOperate(FluidTank fluidTank, IItemHandlerModifiable inventory) {
        return getInput().useFluid(fluidTank, false, 1) && getOutput().applyOutputs(inventory, 1, false);
    }

    public void operate(FluidTank inputTank, IItemHandlerModifiable inventory) {
        if (getInput().useFluid(inputTank, true, 1)) {
            getOutput().applyOutputs(inventory, 1, true);
        }
    }

    @Override
    public GasCentrifugeRecipe copy() {
        return new GasCentrifugeRecipe(getInput().copy(), getOutput().copy());
    }
}
