package org.halvors.nuclearphysics.common.utility;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class VectorUtility {
    private static final String NBT_X = "x";
    private static final String NBT_Y = "y";
    private static final String NBT_Z = "z";

    public static BlockPos scale(final BlockPos pos, final double amount) {
        return new BlockPos(pos.getX() * amount, pos.getY() * amount, pos.getZ() * amount);
    }

    public static double getMagnitude(final BlockPos pos) {
        return Math.sqrt(getMagnitudeSquared(pos));
    }

    public static double getMagnitudeSquared(final BlockPos pos) {
        return pos.getX() * pos.getX() + pos.getY() * pos.getY() + pos.getZ() * pos.getZ();
    }

    public static BlockPos normalize(final BlockPos pos) {
        double magnitude = getMagnitude(pos);

        return scale(pos, magnitude != 0 ? 1 / magnitude : 0);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static BlockPos readFromNBT(final NBTTagCompound tag) {
        return new BlockPos(tag.getDouble(NBT_X), tag.getDouble(NBT_Y), tag.getDouble(NBT_Z));
    }

    public static NBTTagCompound writeToNBT(final BlockPos pos, final NBTTagCompound tag) {
        tag.setDouble(NBT_X, pos.getX());
        tag.setDouble(NBT_Y, pos.getY());
        tag.setDouble(NBT_Z, pos.getZ());

        return tag;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static BlockPos handlePacketData(final ByteBuf dataStream) {
        return new BlockPos(dataStream.readInt(), dataStream.readInt(), dataStream.readInt());
    }

    public static List<Object> getPacketData(final BlockPos pos, final List<Object> objects) {
        objects.add(pos.getX());
        objects.add(pos.getY());
        objects.add(pos.getZ());

        return objects;
    }
}
