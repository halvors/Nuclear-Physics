package org.halvors.nuclearphysics.common.tile;

import net.minecraft.util.EnumFacing;

public interface ITileRotatable {
    boolean canSetFacing(EnumFacing facing);

    EnumFacing getFacing();

    void setFacing(EnumFacing facing);
}