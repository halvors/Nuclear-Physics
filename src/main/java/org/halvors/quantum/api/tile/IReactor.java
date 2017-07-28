package org.halvors.quantum.api.tile;

import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.IFluidHandler;

public interface IReactor {
    void heat(long energy);

    float getTemperature();

    boolean isOverToxic();

    IFluidHandler getTank();

    World getWorld();
}