package org.halvors.atomicscience.common.base.tile;

/**
 * Implement this if your TileEntity has some form of active state.
 *
 * @author halvors
 */
public interface ITileActiveState {
	/**
	 * Gets the active state as a boolean.
	 */
	boolean isActive();

	/**
	 * Sets the active state to a new boolean value.
	 */
	void setActive(boolean isActive);
}
