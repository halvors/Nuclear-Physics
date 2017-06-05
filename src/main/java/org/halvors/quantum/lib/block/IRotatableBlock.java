package org.halvors.quantum.lib.block;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public interface IRotatableBlock {
    ForgeDirection getDirection(World world, int x, int y, int z);

    void setDirection(World world, int x, int y, int z, ForgeDirection direction);
}
