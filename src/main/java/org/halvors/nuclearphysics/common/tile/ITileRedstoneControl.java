package org.halvors.nuclearphysics.common.tile;

import org.halvors.nuclearphysics.common.type.EnumRedstoneControl;

public interface ITileRedstoneControl {
    /**
     * Gets the EnumRedstoneControl type from this block.
     *
     * @return this block's EnumRedstoneControl type
     */
    EnumRedstoneControl getRedstoneControl();

    /**
     * Sets this block's EnumRedstoneControl type to a new value.
     *
     * @param redstoneControl - EnumRedstoneControl type to set
     */
    void setRedstoneControl(final EnumRedstoneControl redstoneControl);

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
