package org.halvors.nuclearphysics.api.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.halvors.nuclearphysics.api.effect.poison.PoisonType;

public interface IAntiPoisonBlock {
    boolean isPoisonProtective(IBlockAccess world, BlockPos pos, PoisonType type);
}