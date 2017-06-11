package org.halvors.quantum.lib;

import net.minecraftforge.common.util.ForgeDirection;

public interface IRotatable {
    ForgeDirection getDirection();

    void setDirection(ForgeDirection direction);
}