package org.halvors.quantum.api.tile;

import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidTank;

public interface IReactor {
    void heat(long energy);

    float getTemperature();

    boolean isOverToxic();

    FluidTank getTank();

    World getWorld();
}