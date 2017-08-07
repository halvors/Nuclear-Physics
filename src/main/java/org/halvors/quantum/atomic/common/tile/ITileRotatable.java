package org.halvors.quantum.atomic.common.tile;

import net.minecraft.util.EnumFacing;

public interface ITileRotatable {
    EnumFacing getFacing();

    void setFacing(EnumFacing facing);
}