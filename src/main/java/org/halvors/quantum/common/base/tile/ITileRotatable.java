package org.halvors.quantum.common.base.tile;

/**
 * This makes a TileEntity rotatable, it's meant to be extended.
 *
 * @author halvors
 */
public interface ITileRotatable {
	/**
	 * Whether or not this block's orientation can be changed to a specific direction.
	 */
	boolean canSetFacing(int facing);

	/**
	 * The direction this block is facing.
	 * @return facing
	 */
	int getFacing();

	/**
	 * Sets the rotation of this block.
	 * @param facing the facing that should be set.
	 */
	void setFacing(int facing);
}