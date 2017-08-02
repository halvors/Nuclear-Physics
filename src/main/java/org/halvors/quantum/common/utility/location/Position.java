package org.halvors.quantum.common.utility.location;

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

/**
 * Coord4D - an integer-based way to keep track of and perform operations on blocks in a Minecraft-based environment. This also takes
 * in account the dimension the coordinate is in.
 *
 * @author aidancbrady
 */
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

    public Position add(double x, double y, double z) {
        return new Position(this.x + x, this.y + y, this.z + z);
    }

    public Position add(Position position) {
        return add(position.getX(), position.getY(), position.getZ());
    }

    public Position subtract(double x, double y, double z) {
        return new Position(this.x - x, this.y - y, this.z - z);
    }

    public Position subtract(Position position) {
        return subtract(position.getX(), position.getY(), position.getZ());
    }

    public Position scale(double amount) {
        return new Position(x * amount, y * amount, z * amount);
    }

    public Position offset(EnumFacing side, double amount) {
        if (side != null && amount > 0) {
            return new Position(x + (side.getFrontOffsetX() * amount), y + (side.getFrontOffsetY() * amount), z + (side.getFrontOffsetZ() * amount));
        }

        return this;
    }

    public Position offset(EnumFacing side) {
        return offset(side, 1);
    }

    /**
     * Creates and returns a new Coord4D with values representing the difference between the defined Coord4D
     *
     * @param other - the Coord4D to subtract from this
     * @return a Coord4D representing the distance between the defined Coord4D
     */
    /*
    public Coord4D difference(Coord4D other) {
        return new Coord4D(x - other.getX(), yCoord - other.yCoord, zCoord - other.zCoord, dimensionId);
    }
    */

    /**
     * A method used to find the EnumFacing represented by the distance of the defined Coord4D. Most likely won't have many
     * applicable uses.
     *
     * @param other - Coord4D to find the side difference of
     * @return EnumFacing representing the side the defined relative Coord4D is on to this
     */
    /*
    public EnumFacing sideDifference(Coord4D other) {
        Coord4D diff = difference(other);

        for (EnumFacing side : EnumFacing.VALUES) {
            if (side.getFrontOffsetX() == diff.xCoord && side.getFrontOffsetY() == diff.yCoord && side.getFrontOffsetZ() == diff.zCoord) {
                return side;
            }
        }

        return null;
    }

    /**
     * Gets the distance to a defined Coord4D.
     *
     * @param obj - the Coord4D to find the distance to
     * @return the distance to the defined Coord4D
     */
    /*
    public int distanceTo(Coord4D obj) {
        int subX = xCoord - obj.xCoord;
        int subY = yCoord - obj.yCoord;
        int subZ = zCoord - obj.zCoord;
        return (int) MathHelper.sqrt(subX * subX + subY * subY + subZ * subZ);
    }

    /**
     * Whether or not the defined side of this Coord4D is visible.
     *
     * @param side  - side to check
     * @param world - world this Coord4D is in
     * @return
     */
    /*
    public boolean sideVisible(EnumFacing side, IBlockAccess world) {
        return world.isAirBlock(step(side).getPos());
    }

    /**
     * Gets a TargetPoint with the defined range from this Coord4D with the appropriate coordinates and dimension ID.
     *
     * @param range - the range the packet can be sent in of this Coord4D
     * @return TargetPoint relative to this Coord4D
     */
    /*
    public TargetPoint getTargetPoint(double range) {
        return new TargetPoint(dimensionId, xCoord, yCoord, zCoord, range);
    }

    /**
     * Steps this Coord4D in the defined side's offset without creating a new value.
     *
     * @param side - side to step towards
     * @return this Coord4D
     */
    /*
    public Coord4D step(EnumFacing side) {
        return translate(side.getFrontOffsetX(), side.getFrontOffsetY(), side.getFrontOffsetZ());
    }

    /**
     * Whether or not the chunk this Coord4D is in exists and is loaded.
     *
     * @param world - world this Coord4D is in
     * @return the chunk of this Coord4D
     */
    /*
    public boolean exists(World world) {
        return world.getChunkProvider().getLoadedChunk(xCoord >> 4, zCoord >> 4) != null;
    }

    /**
     * Gets the chunk this Coord4D is in.
     *
     * @param world - world this Coord4D is in
     * @return the chunk of this Coord4D
     */
    /*
    public Chunk getChunk(World world) {
        return world.getChunkFromBlockCoords(getPos());
    }

    /**
     * Whether or not the block this Coord4D represents is an air block.
     *
     * @param world - world this Coord4D is in
     * @return if this Coord4D is an air block
     */
    /*
    public boolean isAirBlock(IBlockAccess world) {
        return world.isAirBlock(getPos());
    }

    /**
     * Whether or not this block this Coord4D represents is replaceable.
     *
     * @param world - world this Coord4D is in
     * @return if this Coord4D is replaceable
     */
    /*
    public boolean isReplaceable(World world) {
        return getBlock(world).isReplaceable(world, getPos());
    }

    /**
     * Gets a bounding box that contains the area this Coord4D would take up in a world.
     *
     * @return this Coord4D's bounding box
     */
    /*
    public AxisAlignedBB getBoundingBox() {
        return new AxisAlignedBB(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1);
    }
    */
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