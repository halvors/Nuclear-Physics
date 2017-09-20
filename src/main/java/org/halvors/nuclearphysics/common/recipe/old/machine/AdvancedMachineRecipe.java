package org.halvors.nuclearphysics.common.recipe.old.machine;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.halvors.nuclearphysics.common.recipe.machine.MachineRecipe;
import org.halvors.nuclearphysics.common.recipe.old.input.AdvancedMachineInput;
import org.halvors.nuclearphysics.common.recipe.output.ItemStackOutput;

public abstract class AdvancedMachineRecipe<RECIPE extends AdvancedMachineRecipe<RECIPE>> extends MachineRecipe<AdvancedMachineInput, ItemStackOutput, RECIPE> {
    public AdvancedMachineRecipe(AdvancedMachineInput input, ItemStackOutput output) {
        super(input, output);
    }

    public AdvancedMachineRecipe(ItemStack input, Fluid fluid, ItemStack output) {
        this(new AdvancedMachineInput(input, fluid), new ItemStackOutput(output));
    }

    public boolean canOperate(IItemHandlerModifiable inventory, int inputIndex, int outputIndex, FluidTank fluidTank, int amount) {
        return getInput().useItem(inventory, inputIndex, false) && getInput().useSecondary(fluidTank, amount, false) && getOutput().applyOutputs(inventory, outputIndex, false);
    }

    public void operate(IItemHandlerModifiable inventory, int inputIndex, int outputIndex, FluidTank fluidTank, int needed) {
        if (getInput().useItem(inventory, inputIndex, true) && getInput().useSecondary(fluidTank, needed, true)) {
            getOutput().applyOutputs(inventory, outputIndex, true);
        }
    }
}
