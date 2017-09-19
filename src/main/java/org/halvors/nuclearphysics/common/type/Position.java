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

    public Position(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Position(BlockPos pos) {
        this(pos.getX(), pos.getY(), pos.getZ());
    }

    public Position(Entity entity) {
        this.x = entity.posX;
        this.y = entity.posY;
        this.z = entity.posZ;
    }

    public Position(TileEntity tile) {
        this(tile.getPos());
    }

    public Position(RayTraceResult mop) {
        this(mop.getBlockPos());
    }

    public Position(NBTTagCompound tag) {
        this(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z"));
    }

    public Position(ByteBuf dataStream) {
        this(dataStream.readDouble(), dataStream.readDouble(), dataStream.readDouble());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static Position readFromNBT(NBTTagCompound tag) {
        return new Position(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z"));
    }

    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setDouble("x", x);
        tag.setDouble("y", y);
        tag.setDouble("z", z);

        return tag;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void handlePacketData(ByteBuf dataStream) {
        this.x = dataStream.readDouble();
        this.y = dataStream.readDouble();
        this.z = dataStream.readDouble();
    }

    public List<Object> getPacketData(List<Object> objects) {
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

    public IBlockState getBlockState(IBlockAccess world) {
        return world.getBlockState(getPos());
    }

    public Block getBlock(IBlockAccess world) {
        return getBlockState(world).getBlock();
    }

    public TileEntity getTileEntity(IBlockAccess world) {
        return world.getTileEntity(getPos());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Position translate(double x, double y, double z) {
        return new Position(this.x + x, this.y + y, this.z + z);
    }

    public Position translate(double addition) {
        return translate(addition, addition, addition);
    }

    public Position translate(Position position) {
        return translate(position.getX(), position.getY(), position.getZ());
    }

    public Position add(double x, double y, double z) {
        return translate(x, y, z);
    }

    public Position add(Position position) {
        return translate(position);
    }

    public Position subtract(double x, double y, double z) {
        return translate(-x, -y, -z);
    }

    public Position subtract(Position position) {
        return subtract(position.getX(), position.getY(), position.getZ());
    }

    public Position scale(double amount) {
        return new Position(x * amount, y * amount, z * amount);
    }

    public Position offset(EnumFacing side, double amount) {
        return new Position(x + (side.getFrontOffsetX() * amount), y + (side.getFrontOffsetY() * amount), z + (side.getFrontOffsetZ() * amount));
    }

    public Position offset(EnumFacing side) {
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

    public double distance(double x, double y, double z) {
        return subtract(x, y, z).getMagnitude();
    }

    public double distance(Position compare) {
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
    public boolean equals(Object obj) {
        return obj instanceof Position &&
                ((Position) obj).getX() == x &&
                ((Position) obj).getY() == y &&
                ((Position) obj).getZ() == z;
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