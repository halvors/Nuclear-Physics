package org.halvors.nuclearphysics.common.utility;

public class RotationUtility {
    private static final int[] sideRotMap = new int[] { 3, 4, 2, 5, 3, 5, 2, 4, 1, 5, 0, 4, 1, 4, 0, 5, 1, 2, 0, 3, 1, 3, 0, 2 };

    public static final int[][] relativeMatrix = new int[][] { new int[] { 3, 2, 1, 0, 5, 4 },
            new int[] { 4, 5, 0, 1, 2, 3 },
            new int[] { 0, 1, 3, 2, 4, 5 },
            new int[] { 0, 1, 2, 3, 5, 4 },
            new int[] { 0, 1, 5, 4, 3, 2 },
            new int[] { 0, 1, 4, 5, 2, 3 }
    };

    /**
     * Rototes a relative side into a ForgeDirection global size.
     *
     * @param side - The current face we are on (0-6)
     * @param rotation - The rotation to be applied (0-3)
     * @return The ForgeDirection ordinal from 0-5.
     */
    public static int rotateSide(int side, int rotation) {
        return sideRotMap[side << 2 | rotation];
    }
}