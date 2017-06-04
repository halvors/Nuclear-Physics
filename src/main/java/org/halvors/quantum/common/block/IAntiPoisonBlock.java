package org.halvors.quantum.common.block;

import net.minecraft.world.World;

public interface IAntiPoisonBlock {
    boolean isPoisonPrevention(World world, int paramInt1, int paramInt2, int paramInt3, String paramString);
}
