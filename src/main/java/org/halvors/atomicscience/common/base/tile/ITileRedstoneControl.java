package org.halvors.atomicscience.common.base.tile;

import org.halvors.atomicscience.common.base.RedstoneControlType;

public interface ITileRedstoneControl {
	/**
	 * Gets the RedstoneControl type from this block.
	 * @return this block's RedstoneControl type
	 */
	RedstoneControlType getControlType();

	/**
	 * Sets this block's RedstoneControl type to a new value.
	 * @param redstoneControlType - RedstoneControl type to set
	 */
	void setControlType(RedstoneControlType redstoneControlType);

	/**
	 * If the block is getting powered or not by redstone (indirectly).
	 * @return if the block is getting powered indirectly
	 */
	boolean isPowered();

	/**
	 * Set the block to powered state.
	 */
	void setPowered(boolean isPowered);

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
