package org.halvors.quantum.common.tile;

import net.minecraftforge.common.util.ForgeDirection;

public interface ITileRotatable {
    ForgeDirection getDirection();

    void setDirection(ForgeDirection direction);
}