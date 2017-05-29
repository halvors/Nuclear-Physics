package org.halvors.atomicscience.old.process;

import calclavia.api.recipe.MachineRecipes;
import calclavia.api.recipe.RecipeResource;
import calclavia.lib.prefab.tile.TileElectricalInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public abstract class TileProcess
        extends TileElectricalInventory
{
    protected int inputSlot;
    protected int outputSlot;
    protected int tankInputFillSlot;
    protected int tankInputDrainSlot;
    protected int tankOutputFillSlot;
    protected int tankOutputDrainSlot;
    protected String machineName;

    public void func_70316_g()
    {
        super.func_70316_g();
        if (getInputTank() != null) {
            fillOrDrainTank(this.tankInputFillSlot, this.tankInputDrainSlot, getInputTank());
        }
        if (getOutputTank() != null) {
            fillOrDrainTank(this.tankOutputFillSlot, this.tankOutputDrainSlot, getOutputTank());
        }
    }

    public void fillOrDrainTank(int containerInput, int containerOutput, FluidTank tank)
    {
        ItemStack inputStack = func_70301_a(containerInput);
        ItemStack outputStack = func_70301_a(containerOutput);
        if (FluidContainerRegistry.isFilledContainer(inputStack))
        {
            FluidStack fluidStack = FluidContainerRegistry.getFluidForFilledItem(inputStack);
            ItemStack result = inputStack.func_77973_b().getContainerItemStack(inputStack);
            if ((result != null) && (tank.fill(fluidStack, false) >= fluidStack.amount) && ((outputStack == null) || (result.func_77969_a(outputStack))))
            {
                tank.fill(fluidStack, true);
                func_70298_a(containerInput, 1);
                incrStackSize(containerOutput, result);
            }
        }
        else if (FluidContainerRegistry.isEmptyContainer(inputStack))
        {
            FluidStack avaliable = tank.getFluid();
            if (avaliable != null)
            {
                ItemStack result = FluidContainerRegistry.fillFluidContainer(avaliable, inputStack);
                FluidStack filled = FluidContainerRegistry.getFluidForFilledItem(result);
                if ((result != null) && (filled != null) && ((outputStack == null) || (result.func_77969_a(outputStack))))
                {
                    func_70298_a(containerInput, 1);
                    incrStackSize(containerOutput, result);
                    tank.drain(filled.amount, true);
                }
            }
        }
    }

    public RecipeResource[] getResults()
    {
        ItemStack inputStack = func_70301_a(this.inputSlot);
        RecipeResource[] mixedResult = MachineRecipes.INSTANCE.getOutput(this.machineName, new Object[] { inputStack, getInputTank().getFluid() });
        if (mixedResult.length > 0) {
            return mixedResult;
        }
        return MachineRecipes.INSTANCE.getOutput(this.machineName, new Object[] { inputStack });
    }

    public boolean hasResult()
    {
        return getResults().length > 0;
    }

    public abstract FluidTank getInputTank();

    public abstract FluidTank getOutputTank();
}
