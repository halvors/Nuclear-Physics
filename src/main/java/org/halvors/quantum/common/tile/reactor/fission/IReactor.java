package org.halvors.quantum.common.tile.reactor.fission;

import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidHandler;

public interface IReactor extends IFluidHandler {
    void heat(long energy);

    float getTemperature();

    boolean isOverToxic();

    World getWorld();
}