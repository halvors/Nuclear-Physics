package org.halvors.nuclearphysics.api.tile;

import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public interface IReactor {
    void heat(long energy);

    double getTemperature();

    boolean isOverToxic();

    FluidTank getTank();

    World getWorldObject();
}