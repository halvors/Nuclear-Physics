package org.halvors.quantum.common.tile.machine;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ITickable;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import org.halvors.quantum.api.recipe.RecipeResource;
import org.halvors.quantum.common.utility.InventoryUtility;

/*
 * General class for all machines that do traditional recipe processing.
 */
public abstract class TileProcess extends TileMachine implements ITickable {
    protected int inputSlot;
    protected int outputSlot;

    protected int tankInputFillSlot;
    protected int tankInputDrainSlot;
    protected int tankOutputFillSlot;
    protected int tankOutputDrainSlot;

    @Override
    public void update() {
        if (getInputTank() != null) {
            fillOrDrainTank(tankInputFillSlot, tankInputDrainSlot, getInputTank());
        }

        if (getOutputTank() != null) {
            fillOrDrainTank(tankOutputFillSlot, tankOutputDrainSlot, getOutputTank());
        }
    }

    /*
     * Takes an fluid container item and try to fill the tank, dropping the remains in the output slot.
     */
    public void fillOrDrainTank(int containerInput, int containerOutput, FluidTank tank) {
        ItemStack inputStack = inventory.getStackInSlot(containerInput);
        ItemStack outputStack = inventory.getStackInSlot(containerOutput);

        if (FluidContainerRegistry.isFilledContainer(inputStack)) {
            FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(inputStack);
            ItemStack result = inputStack.getItem().getContainerItem(inputStack);

            if (result != null && tank.fill(fluidStack, false) >= fluidStack.amount && (outputStack == null || result.isItemEqual(outputStack))) {
                tank.fill(fluidStack, true);

                InventoryUtility.decrStackSize(inventory, containerInput);
                inventory.insertItem(containerOutput, result, false);
            }
        } else if (FluidContainerRegistry.isEmptyContainer(inputStack)) {
            FluidStack avaliable = tank.getFluid();

            if (avaliable != null) {
                ItemStack result = FluidContainerRegistry.fillFluidContainer(avaliable, inputStack);
                FluidStack filled = FluidContainerRegistry.getFluidForFilledItem(result);

                if (result != null && filled != null && (outputStack == null || result.isItemEqual(outputStack))) {
                    InventoryUtility.decrStackSize(inventory, containerInput);
                    inventory.insertItem(containerOutput, result, false);
                    tank.drain(filled.amount, true);
                }
            }
        }
    }

    /*
     * Gets the current result of the input set up.
     */
    public RecipeResource[] getResults() {
        /*
        ItemStack inputStack = getStackInSlot(inputSlot);
        RecipeResource[] mixedResult = MachineRecipes.INSTANCE.getOutput(machineName, inputStack, getInputTank().getFluid());

        if (mixedResult.length > 0) {
            return mixedResult;
        }

        return MachineRecipes.INSTANCE.getOutput(machineName, inputStack);
        */

        return null;
    }

    public boolean hasResult() {
        return getResults().length > 0;
    }

    public abstract FluidTank getInputTank();

    public abstract FluidTank getOutputTank();
}
