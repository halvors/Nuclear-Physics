package org.halvors.nuclearphysics.common.utility;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundNBT;
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

    public static BlockPos read(final CompoundNBT compound) {
        return new BlockPos(compound.getDouble(NBT_X), compound.getDouble(NBT_Y), compound.getDouble(NBT_Z));
    }

    public static CompoundNBT write(final BlockPos pos, final CompoundNBT compound) {
        compound.putDouble(NBT_X, pos.getX());
        compound.putDouble(NBT_Y, pos.getY());
        compound.putDouble(NBT_Z, pos.getZ());

        return compound;
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
