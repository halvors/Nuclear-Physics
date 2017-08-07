package org.halvors.quantum.common.utility.transform.vector;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class VectorHelper {
    private static final int[][] RELATIVE_MATRIX = { { 3, 2, 1, 0, 5, 4 }, { 4, 5, 0, 1, 2, 3 }, { 0, 1, 3, 2, 4, 5 }, { 0, 1, 2, 3, 5, 4 }, { 0, 1, 5, 4, 3, 2 }, { 0, 1, 4, 5, 2, 3 } };

    public static EnumFacing getOrientationFromSide(EnumFacing front, EnumFacing side) {
        if (front != null && side != null) {
            return EnumFacing.getFront(RELATIVE_MATRIX[front.ordinal()][side.ordinal()]);
        }

        return null;
    }

    public static TileEntity getTileEntityFromSide(World world, Vector3 position, EnumFacing side) {
        return position.clone().translate(side).getTileEntity(world);
    }
}
