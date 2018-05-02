package org.halvors.nuclearphysics.api.block;

import net.minecraft.block.state.IBlockState;

public interface IRadioactiveBlock {
    boolean canSpread(IBlockState state);

    int getRadius(IBlockState state);

    int getAmplifier(IBlockState state);

    boolean canPoisonEntity(IBlockState state);

    boolean shouldSpawnParticles(IBlockState state);
}
