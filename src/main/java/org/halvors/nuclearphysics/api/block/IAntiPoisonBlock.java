package org.halvors.nuclearphysics.api.block;

import net.minecraft.world.IBlockAccess;
import org.halvors.nuclearphysics.api.effect.poison.EnumPoisonType;

public interface IAntiPoisonBlock {
    boolean isPoisonProtective(IBlockAccess world, int x, int y, int z, EnumPoisonType type);
}