package com.calclavia.core.api.atomicscience;

import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidHandler;

public abstract interface IReactor extends IFluidHandler
{
    public abstract void heat(long paramLong);

    public abstract boolean isOverToxic();

    public abstract World getWorld();
}
