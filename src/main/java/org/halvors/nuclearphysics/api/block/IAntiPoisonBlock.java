package org.halvors.nuclearphysics.api.block;

import net.minecraft.world.IBlockAccess;

public interface IAntiPoisonBlock {
    boolean isPoisonPrevention(IBlockAccess world, int x, int y, int z, String name);
}
