package org.halvors.nuclearphysics.common.type;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;

import java.util.List;

public class Position {
    private double x;
    private double y;
    private double z;

    public Position() {

    }

    public Position(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Position(final BlockPos pos) {
        this(pos.getX(), pos.getY(), pos.getZ());
    }

    public Position(final Entity entity) {
        this.x = entity.posX;
        this.y = entity.posY;
        this.z = entity.posZ;
    }

    public Position(final TileEntity tile) {
        this(tile.getPos());
    }

    public Position(final RayTraceResult mop) {
        this(mop.getBlockPos());
    }

    public Position(final NBTTagCompound tag) {
        this(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z"));
    }

    public Position(final ByteBuf dataStream) {
        this(dataStream.readDouble(), dataStream.readDouble(), dataStream.readDouble());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static Position readFromNBT(final NBTTagCompound tag) {
        return new Position(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z"));
    }

    public NBTTagCompound writeToNBT(final NBTTagCompound tag) {
        tag.setDouble("x", x);
        tag.setDouble("y", y);
        tag.setDouble("z", z);

        return tag;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void handlePacketData(final ByteBuf dataStream) {
        this.x = dataStream.readDouble();
        this.y = dataStream.readDouble();
        this.z = dataStream.readDouble();
    }

    public List<Object> getPacketData(final List<Object> objects) {
        objects.add(x);
        objects.add(y);
        objects.add(z);

        return objects;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public int getIntX() {
        return (int) Math.floor(x);
    }

    public int getIntY() {
        return (int) Math.floor(y);
    }

    public int getIntZ() {
        return (int) Math.floor(z);
    }

    public BlockPos getPos() {
        return new BlockPos(x, y, z);
    }

    public IBlockState getBlockState(final IBlockAccess world) {
        return world.getBlockState(getPos());
    }

    public Block getBlock(final IBlockAccess world) {
        return getBlockState(world).getBlock();
    }

    public TileEntity getTileEntity(final IBlockAccess world) {
        return world.getTileEntity(getPos());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Position translate(final double x, final double y, final double z) {
        return new Position(this.x + x, this.y + y, this.z + z);
    }

    public Position translate(final double addition) {
        return translate(addition, addition, addition);
    }

    public Position translate(final Position position) {
        return translate(position.getX(), position.getY(), position.getZ());
    }

    public Position add(final double x, final double y, final double z) {
        return translate(x, y, z);
    }

    public Position add(final Position position) {
        return translate(position);
    }

    public Position subtract(final double x, final double y, final double z) {
        return translate(-x, -y, -z);
    }

    public Position subtract(final Position position) {
        return subtract(position.getX(), position.getY(), position.getZ());
    }

    public Position scale(final double amount) {
        return new Position(x * amount, y * amount, z * amount);
    }

    public Position offset(final EnumFacing side, final double amount) {
        return new Position(x + (side.getFrontOffsetX() * amount), y + (side.getFrontOffsetY() * amount), z + (side.getFrontOffsetZ() * amount));
    }

    public Position offset(final EnumFacing side) {
        return offset(side, 1);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public double getMagnitude() {
        return Math.sqrt(getMagnitudeSquared());
    }

    public double getMagnitudeSquared() {
        return x * x + y * y + z * z;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public double distance(final double x, final double y, final double z) {
        return subtract(x, y, z).getMagnitude();
    }

    public double distance(final Position compare) {
        return distance(compare.getX(), compare.getY(), compare.getZ());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Position normalize() {
        double magnitude = getMagnitude();

        return scale(magnitude != 0 ? 1 / magnitude : 0);
    }



    @Override
    public Position clone() {
        return new Position(x, y, z);
    }

    @Override
    public String toString() {
        return "[Coord4D: " + x + ", " + y + ", " + z + "]";
    }

    @Override
    public boolean equals(final Object object) {
        return object instanceof Position &&
                ((Position) object).getX() == x &&
                ((Position) object).getY() == y &&
                ((Position) object).getZ() == z;
    }

    @Override
    public int hashCode() {
        int code = 1;
        code = 31 * code + (int) x;
        code = 31 * code + (int) y;
        code = 31 * code + (int) z;

        return code;
    }
}