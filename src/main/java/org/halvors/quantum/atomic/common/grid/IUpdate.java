package org.halvors.quantum.atomic.common.grid;

public interface IUpdate {
    void update();

    boolean canUpdate();

    boolean continueUpdate();
}
