package org.halvors.quantum.common.reactor;

import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidHandler;

public interface IReactor extends IFluidHandler {
    void heat(long energy);

    float getTemperature();

    boolean isOverToxic();

    World getWorld();
}