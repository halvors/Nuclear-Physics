package org.halvors.nuclearphysics.common.tile;

import net.minecraft.util.EnumFacing;

public interface ITileRotatable {
    EnumFacing getFacing();

    void setFacing(EnumFacing facing);
}