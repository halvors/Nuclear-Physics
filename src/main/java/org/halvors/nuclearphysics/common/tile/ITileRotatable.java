package org.halvors.nuclearphysics.common.tile;

import net.minecraft.util.Direction;

public interface ITileRotatable {
    boolean canSetDirection(final Direction direction);

    Direction getDirection();

    void setDirection(final Direction direction);
}