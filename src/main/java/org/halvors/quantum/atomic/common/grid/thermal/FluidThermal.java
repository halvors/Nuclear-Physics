package org.halvors.quantum.atomic.common.grid.thermal;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class FluidThermal extends Fluid {
    public FluidThermal(String fluidName, ResourceLocation still, ResourceLocation flowing) {
        super(fluidName, still, flowing);
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