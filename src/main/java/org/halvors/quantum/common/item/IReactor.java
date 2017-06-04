package org.halvors.quantum.common.item;

import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidHandler;

public interface IReactor extends IFluidHandler {
    void heat(long paramLong);

    float getTemperature();

    boolean isOverToxic();

    World getWorld();
}