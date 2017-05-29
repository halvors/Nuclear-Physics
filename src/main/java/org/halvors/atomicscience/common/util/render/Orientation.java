package org.halvors.atomicscience.common.util.render;

import net.minecraftforge.common.util.ForgeDirection;

public class Orientation {
	/**
	 * Returns an integer facing that converts a world-based orientation to a machine-based oriention.
	 */
	public static int getBaseOrientation(int side, int blockFacing) {
		if (blockFacing == 3 || side == 1 || side == 0) {
			if (side == 2 || side == 3) {
				return ForgeDirection.getOrientation(side).getOpposite().ordinal();
			}

			return side;
		} else if (blockFacing == 2) {
			if (side == 2 || side == 3) {
				return side;
			}

			return ForgeDirection.getOrientation(side).getOpposite().ordinal();
		} else if (blockFacing == 4) {
			if (side == 2 || side == 3) {
				return getRight(side).ordinal();
			}

			return getLeft(side).ordinal();
		} else if (blockFacing == 5) {
			if (side == 2 || side == 3) {
				return getLeft(side).ordinal();
			}

			return getRight(side).ordinal();
		}

		return side;
	}

	/**
	 * Gets the left side of a certain orientation.
	 */
	private static ForgeDirection getLeft(int orientation) {
		switch (orientation) {
			case 2:
				return ForgeDirection.EAST;

			case 3:
				return ForgeDirection.WEST;

			case 4:
				return ForgeDirection.NORTH;

			default:
				return ForgeDirection.SOUTH;
		}
	}

	/**
	 * Gets the right side of a certain orientation.
	 */
	private static ForgeDirection getRight(int orientation) {
		return getLeft(orientation).getOpposite();
	}
}
