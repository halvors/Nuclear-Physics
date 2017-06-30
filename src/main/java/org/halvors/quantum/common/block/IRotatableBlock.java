package org.halvors.quantum.common.block;

import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public interface IRotatableBlock {
    EnumFacing getDirection(World world, int x, int y, int z);

    void setDirection(World world, int x, int y, int z, EnumFacing direction);
}
