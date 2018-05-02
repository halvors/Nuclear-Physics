package org.halvors.nuclearphysics.common.tile;

import net.minecraft.util.EnumFacing;

public interface ITileRotatable {
    boolean canSetFacing(final EnumFacing facing);

    EnumFacing getFacing();

    void setFacing(final EnumFacing facing);
}