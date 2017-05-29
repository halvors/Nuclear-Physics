package com.calclavia.core.api.atomicscience.block;

import net.minecraft.world.World;

public abstract interface IAntiPoisonBlock {
    public abstract boolean isPoisonPrevention(World paramWorld, int paramInt1, int paramInt2, int paramInt3, String paramString);
}
