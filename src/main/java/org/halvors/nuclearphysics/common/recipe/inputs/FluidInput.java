package org.halvors.nuclearphysics.common.recipe.inputs;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class FluidInput extends MachineInput<FluidInput> {
    private FluidStack ingredient;

    public FluidInput(final FluidStack fluidStack) {
        ingredient = fluidStack;
    }

    public FluidInput() {

    }

    @Override
    public void load(final NBTTagCompound tag) {
        ingredient = FluidStack.loadFluidStackFromNBT(tag.getCompoundTag("input"));
    }

    @Override
    public FluidInput copy() {
        return new FluidInput(ingredient.copy());
    }

    @Override
    public boolean isValid() {
        return ingredient != null;
    }

    public boolean useFluid(final FluidTank fluidTank, final boolean deplete, final int scale) {
        final FluidStack fluidStack = fluidTank.getFluid();

        if (fluidStack != null && fluidStack.containsFluid(ingredient)) {
            fluidTank.drain(ingredient.amount * scale, deplete);

            return true;
        }

        return false;
    }

    @Override
    public int hashIngredients() {
        return ingredient.getFluid() != null ? ingredient.getFluid().hashCode() : 0;
    }

    @Override
    public boolean testEquality(final FluidInput fluidStack) {
        if (!isValid()) {
            return !fluidStack.isValid();
        }

        return ingredient.equals(fluidStack.ingredient);
    }

    @Override
    public boolean isInstance(final Object object) {
        return object instanceof FluidInput;
    }

    public FluidStack getIngredient() {
        return ingredient;
    }
}
