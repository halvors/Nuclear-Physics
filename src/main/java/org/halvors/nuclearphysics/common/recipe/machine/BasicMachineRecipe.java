package org.halvors.nuclearphysics.common.recipe.machine;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.halvors.nuclearphysics.common.recipe.input.ItemStackInput;
import org.halvors.nuclearphysics.common.recipe.output.ItemStackOutput;

public abstract class BasicMachineRecipe<R extends BasicMachineRecipe<R>> extends MachineRecipe<ItemStackInput, ItemStackOutput, R> {
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
