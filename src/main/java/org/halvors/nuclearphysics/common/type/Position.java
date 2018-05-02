package org.halvors.nuclearphysics.common.type;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

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

    public Position(final Entity entity) {
        this.x = entity.posX;
        this.y = entity.posY;
        this.z = entity.posZ;
    }

    public Position(final TileEntity tile) {
        this(tile.xCoord, tile.yCoord, tile.zCoord);
    }

    public Position(final NBTTagCompound tag) {
        this(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z"));
    }

    public Position(final ByteBuf dataStream) {
        this(dataStream.readDouble(), dataStream.readDouble(), dataStream.readDouble());
    }

    public Position(ForgeDirection facing) {
        this(facing.offsetX, facing.offsetY, facing.offsetZ);
    }

    public Position(MovingObjectPosition position) {
        this(position.blockX, position.blockY, position.blockZ);
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

    public Block getBlock(final IBlockAccess world) {
        return world.getBlock(getIntX(), getIntY(), getIntZ());
    }

    public int getBlockMetadata(final IBlockAccess world) {
        return world.getBlockMetadata(getIntX(), getIntY(), getIntZ());
    }

    public TileEntity getTileEntity(final IBlockAccess world) {
        return world.getTileEntity(getIntX(), getIntY(), getIntZ());
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

    public Position translate(ForgeDirection facing, double amount) {
        return translate(new Position(facing).scale(amount));
    }

    public Position translate(ForgeDirection facing) {
        return translate(facing, 1);
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

    public Position offset(final ForgeDirection side, final double amount) {
        return new Position(x + (side.offsetX * amount), y + (side.offsetY * amount), z + (side.offsetZ * amount));
    }

    public Position offset(final ForgeDirection side) {
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