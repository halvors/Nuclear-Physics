package org.halvors.nuclearphysics.common.grid;

public interface IGrid {
    void update();

    boolean canUpdate();

    boolean continueUpdate();
}
