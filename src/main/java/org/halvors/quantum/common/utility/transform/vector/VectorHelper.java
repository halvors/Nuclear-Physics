package org.halvors.quantum.common.utility.transform.vector;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class VectorHelper {
    public static final int[][] RELATIVE_MATRIX = { { 3, 2, 1, 0, 5, 4 }, { 4, 5, 0, 1, 2, 3 }, { 0, 1, 3, 2, 4, 5 }, { 0, 1, 2, 3, 5, 4 }, { 0, 1, 5, 4, 3, 2 }, { 0, 1, 4, 5, 2, 3 } };

    public static ForgeDirection getOrientationFromSide(ForgeDirection front, ForgeDirection side) {
        if (front != ForgeDirection.UNKNOWN && side != ForgeDirection.UNKNOWN) {
            return ForgeDirection.getOrientation(RELATIVE_MATRIX[front.ordinal()][side.ordinal()]);
        }

        return ForgeDirection.UNKNOWN;
    }

    public static TileEntity getTileEntityFromSide(World world, Vector3 position, ForgeDirection side) {
        return position.clone().translate(side).getTileEntity(world);
    }
}
