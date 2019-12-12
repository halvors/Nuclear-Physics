package org.halvors.nuclearphysics.api.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.halvors.nuclearphysics.api.effect.poison.EnumPoisonType;

public interface IAntiPoisonBlock {
    boolean isPoisonProtective(World world, BlockPos pos, EnumPoisonType type);
}