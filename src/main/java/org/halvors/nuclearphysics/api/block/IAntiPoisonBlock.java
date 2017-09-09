package org.halvors.nuclearphysics.api.block;

import net.minecraft.world.World;

public interface IAntiPoisonBlock {
    boolean isPoisonPrevention(World world, int x, int y, int z, String name);
}
