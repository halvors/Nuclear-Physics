package org.halvors.nuclearphysics.common.recipe.input;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class FluidInput extends MachineInput<FluidInput> {
    private FluidStack fluidStack;

    public FluidInput(FluidStack fluidStack) {
        this.fluidStack = fluidStack;
    }

    @Override
    public void load(NBTTagCompound tag) {
        fluidStack = FluidStack.loadFluidStackFromNBT(tag.getCompoundTag("input"));
    }

    @Override
    public FluidInput copy() {
        return new FluidInput(fluidStack.copy());
    }

    @Override
    public boolean isValid() {
        return fluidStack != null;
    }

    public boolean useFluid(FluidTank fluidTank, boolean deplete, int scale) {
        if (fluidTank.getFluid() != null && fluidTank.getFluid().containsFluid(fluidStack)) {
            fluidTank.drain(fluidStack.amount * scale, deplete);

            return true;
        }

        return false;
    }

    @Override
    public boolean testEquality(FluidInput other) {
        if (!isValid()) {
            return !other.isValid();
        }

        return fluidStack.equals(other.fluidStack);
    }
}
