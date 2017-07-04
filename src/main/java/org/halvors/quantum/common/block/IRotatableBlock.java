package org.halvors.quantum.common.block;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IRotatableBlock {
    EnumFacing getDirection(World world, BlockPos pos);

    void setDirection(World world, BlockPos pos, EnumFacing direction);
}
