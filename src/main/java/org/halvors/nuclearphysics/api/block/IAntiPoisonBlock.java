package org.halvors.nuclearphysics.api.block;

import net.minecraft.world.IBlockAccess;
import org.halvors.nuclearphysics.api.effect.poison.PoisonType;

public interface IAntiPoisonBlock {
    boolean isPoisonProtective(IBlockAccess world, int x, int y, int z, PoisonType type);
}