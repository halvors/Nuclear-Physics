package org.halvors.nuclearphysics.common.tile;

import org.halvors.nuclearphysics.common.type.RedstoneControl;

public interface ITileRedstoneControl {
    /**
     * Gets the RedstoneControl type from this block.
     *
     * @return this block's RedstoneControl type
     */
    RedstoneControl getRedstoneControl();

    /**
     * Sets this block's RedstoneControl type to a new value.
     *
     * @param redstoneControl - RedstoneControl type to set
     */
    void setRedstoneControl(RedstoneControl redstoneControl);

    /**
     * If the block is getting powered or not by redstone (indirectly).
     *
     * @return if the block is getting powered indirectly
     */
    boolean isPowered();

    /**
     * If the block was getting powered or not by redstone, last tick.
     * Used for PULSE mode.
     */
    boolean wasPowered();

    /**
     * If the machine can be pulsed.
     */
    boolean canPulse();
}
