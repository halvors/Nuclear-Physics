package org.halvors.quantum.common.utility.location;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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

    public Position(Entity entity)
    {
        this(entity.posX, entity.posY, entity.posZ);
    }

    public Position(TileEntity tile) {
        this(tile.getPos());
    }

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

    public IBlockState getBlockState(World world) {
        return world.getBlockState(getPos());
    }

    public TileEntity getTileEntity(World world) {
        return world.getTileEntity(getPos());
    }

    public Position scale(double amount) {
        this.x *= amount;
        this.y *= amount;
        this.z *= amount;

        return this;
    }
}
