package org.halvors.quantum.lib.thermal;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class FluidThermal extends Fluid {
    public FluidThermal(String fluidName) {
        super(fluidName);
    }

    public int getTemperature(FluidStack stack) {
        if (stack.tag.hasKey("temperature")) {
            return stack.tag.getInteger("temperature");
        }

        return getTemperature();
    }

    public void setTemperature(FluidStack stack, int temperature) {
        stack.tag.setInteger("temperature", temperature);
    }
}