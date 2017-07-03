package org.halvors.quantum.common.utility.location;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class Location {
	private final int dimensionId;
	private final int x;
	private final int y;
	private final int z;

	public Location(int dimensionId, BlockPos pos) {
		this.dimensionId = dimensionId;
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
	}

	public Location(Entity entity) {
		this.dimensionId = entity.dimension;
		this.x = (int) entity.posX;
		this.y = (int) entity.posY;
		this.z = (int) entity.posZ;
	}

	public Location(TileEntity tileEntity) {
		this.dimensionId = tileEntity.getWorld().provider.getDimension();
		this.x = tileEntity.getPos().getX();
		this.y = tileEntity.getPos().getY();
		this.z = tileEntity.getPos().getZ();
	}

	public Location(int dimensionId, int x, int y, int z) {
		this.dimensionId = dimensionId;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public int getDimensionId() {
		return dimensionId;
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

	public IBlockState getBlockState(IBlockAccess world) {
		return world.getBlockState(new BlockPos(x, y, z));
	}

	public TileEntity getTileEntity(IBlockAccess world) {
		return world.getTileEntity(new BlockPos(x, y, z));
	}

    @Override
    public String toString() {
        return x + ", " + y + ", " + z + " in dimension " + dimensionId;
    }
}
