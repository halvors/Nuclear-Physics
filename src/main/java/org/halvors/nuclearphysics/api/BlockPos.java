package org.halvors.nuclearphysics.api;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockPos {
    public static final BlockPos ORIGIN = new BlockPos(0, 0, 0);

    private final int x;
    private final int y;
    private final int z;

    public BlockPos(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BlockPos(final double x, final double y, final double z) {
        this((int) x, (int) y, (int) z);
    }

    public BlockPos(final Entity entity) {
        this(Math.floor(entity.posX), Math.floor(entity.posY), Math.floor(entity.posZ));
    }

    public BlockPos(final TileEntity tile) {
        this(tile.xCoord, tile.yCoord, tile.zCoord);
    }

    public BlockPos(final Vec3 vec) {
        this(vec.xCoord, vec.yCoord, vec.zCoord);
    }

    public BlockPos(final MovingObjectPosition movingObjectPosition) {
        this(movingObjectPosition.blockX, movingObjectPosition.blockY, movingObjectPosition.blockZ);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public Block getBlock(final IBlockAccess world) {
        return world.getBlock(x, y, z);
    }

    public void setBlock(final Block block, final World world) {
        world.setBlock(x, y, z, block);
    }

    public void setBlock(final Block block, int metadata, int flags, final World world) {
        world.setBlock(x, y, z, block, metadata, flags);
    }

    public float getBlockHardness(final Block block, final World world) {
        return block.getBlockHardness(world, x, y, z);
    }

    public int getBlockMetadata(final IBlockAccess world) {
        return world.getBlockMetadata(x, y, z);
    }

    public boolean isBlockReplaceable(final Block block, final IBlockAccess world) {
        return block.isReplaceable(world, x, y, z);
    }

    public TileEntity getTileEntity(final IBlockAccess world) {
        return world.getTileEntity(x, y, z);
    }

    public boolean isAirBlock(final IBlockAccess world) {
        return world.isAirBlock(x, y, z);
    }

    public boolean setBlockToAir(final World world) {
        return world.setBlockToAir(x, y, z);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Add the given coordinates to the coordinates of this BlockPos
     */
    public BlockPos add(final double x, final double y, final double z) {
        return x == 0 && y == 0 && z == 0 ? this : new BlockPos(this.x + x, this.y + y, this.z + z);
    }

    /**
     * Add the given coordinates to the coordinates of this BlockPos
     */
    public BlockPos add(final int x, final int y, final int z) {
        return x == 0 && y == 0 && z == 0 ? this : new BlockPos(this.x + x, this.y + y, this.z + z);
    }

    /**
     * Add the given Vector to this BlockPos
     */
    public BlockPos add(final BlockPos pos) {
        return pos.getX() == 0 && pos.getY() == 0 && pos.getZ() == 0 ? this : new BlockPos(x + pos.getX(), y + pos.getY(), z + pos.getZ());
    }

    /**
     * Subtract the given Vector from this BlockPos
     */
    public BlockPos subtract(final BlockPos pos) {
        return pos.getX() == 0 && pos.getY() == 0 && pos.getZ() == 0 ? this : new BlockPos(x - pos.getX(), y - pos.getY(), z - pos.getZ());
    }

    public double getDistance(int x, int y, int z) {
        double d0 = (double)(this.x - x);
        double d1 = (double)(this.y - y);
        double d2 = (double)(this.z - z);
        return Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
    }

    /**
     * Offset this BlockPos 1 block up
     */
    public BlockPos up() {
        return this.up(1);
    }

    /**
     * Offset this BlockPos n blocks up
     */
    public BlockPos up(final int n) {
        return offset(ForgeDirection.UP, n);
    }

    /**
     * Offset this BlockPos 1 block down
     */
    public BlockPos down() {
        return this.down(1);
    }

    /**
     * Offset this BlockPos n blocks down
     */
    public BlockPos down(final int n) {
        return offset(ForgeDirection.DOWN, n);
    }

    /**
     * Offset this BlockPos 1 block in northern direction
     */
    public BlockPos north() {
        return north(1);
    }

    /**
     * Offset this BlockPos n blocks in northern direction
     */
    public BlockPos north(final int n) {
        return offset(ForgeDirection.NORTH, n);
    }

    /**
     * Offset this BlockPos 1 block in southern direction
     */
    public BlockPos south() {
        return this.south(1);
    }

    /**
     * Offset this BlockPos n blocks in southern direction
     */
    public BlockPos south(final int n) {
        return offset(ForgeDirection.SOUTH, n);
    }

    /**
     * Offset this BlockPos 1 block in western direction
     */
    public BlockPos west()
    {
        return west(1);
    }

    /**
     * Offset this BlockPos n blocks in western direction
     */
    public BlockPos west(final int n) {
        return offset(ForgeDirection.WEST, n);
    }

    /**
     * Offset this BlockPos 1 block in eastern direction
     */
    public BlockPos east()
    {
        return east(1);
    }

    /**
     * Offset this BlockPos n blocks in eastern direction
     */
    public BlockPos east(final int n) {
        return offset(ForgeDirection.EAST, n);
    }

    /**
     * Offset this BlockPos 1 block in the given direction
     */
    public BlockPos offset(final ForgeDirection facing) {
        return offset(facing, 1);
    }

    /**
     * Offsets this BlockPos n blocks in the given direction
     */
    public BlockPos offset(final ForgeDirection facing, final int n) {
        return n == 0 ? this : new BlockPos(this.x + facing.offsetX * n, this.y + facing.offsetY * n, this.z + facing.offsetZ * n);
    }
}
