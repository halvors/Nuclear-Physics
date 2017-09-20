package org.halvors.nuclearphysics.common.recipe.old.machine;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.halvors.nuclearphysics.common.recipe.machine.MachineRecipe;
import org.halvors.nuclearphysics.common.recipe.input.ItemStackInput;
import org.halvors.nuclearphysics.common.recipe.output.ItemStackOutput;

public abstract class BasicMachineRecipe<RECIPE extends BasicMachineRecipe<RECIPE>> extends MachineRecipe<ItemStackInput, ItemStackOutput, RECIPE> {
    public BasicMachineRecipe(ItemStackInput input, ItemStackOutput output) {
        super(input, output);
    }

    public BasicMachineRecipe(ItemStack input, ItemStack output) {
        this(new ItemStackInput(input), new ItemStackOutput(output));
    }

    public boolean canOperate(IItemHandlerModifiable inventory, int inputIndex, int outputIndex) {
        return getInput().useItemStackFromInventory(inventory, inputIndex, false) && getOutput().applyOutputs(inventory, outputIndex, false);
    }

    public void operate(IItemHandlerModifiable inventory, int inputIndex, int outputIndex) {
        if (getInput().useItemStackFromInventory(inventory, inputIndex, true)) {
            getOutput().applyOutputs(inventory, outputIndex, true);
        }
    }
}
