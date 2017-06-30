package org.halvors.quantum.common.tile;

import net.minecraft.util.EnumFacing;

public interface ITileRotatable {
    EnumFacing getDirection();

    void setDirection(EnumFacing direction);
}