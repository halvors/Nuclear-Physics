package org.halvors.nuclearphysics.common.grid;

public interface IUpdate {
    void update();

    boolean canUpdate();

    boolean continueUpdate();
}
