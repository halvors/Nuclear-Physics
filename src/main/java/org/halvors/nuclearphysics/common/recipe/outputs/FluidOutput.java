package org.halvors.nuclearphysics.common.recipe.outputs;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class FluidOutput extends MachineOutput<FluidOutput> {
    public FluidStack output;

    public FluidOutput(final FluidStack fluidStack) {
        output = fluidStack;
    }

    public FluidOutput() {

    }

    @Override
    public void load(final NBTTagCompound tag) {
        output = FluidStack.loadFluidStackFromNBT(tag.getCompoundTag("output"));
    }

    @Override
    public FluidOutput copy() {
        return new FluidOutput(output.copy());
    }

    public boolean applyOutputs(final FluidTank fluidTank, final boolean doEmit) {
        if (fluidTank.fill(output, false) > 0) {
            fluidTank.fill(output, doEmit);

            return true;
        }

        return false;
    }
}
