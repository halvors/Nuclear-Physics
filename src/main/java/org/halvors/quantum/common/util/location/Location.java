package org.halvors.quantum.common.util.location;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.world.IBlockAccess;
import org.halvors.quantum.common.tile.TileEntity;

public class Location {
	private final int dimensionId;
	private final int x;
	private final int y;
	private final int z;

	public Location(int dimensionId, int x, int y, int z) {
		this.dimensionId = dimensionId;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Location(Entity entity) {
		this.dimensionId = entity.dimension;
		this.x = (int) entity.posX;
		this.y = (int) entity.posY;
		this.z = (int) entity.posZ;
	}

	public Location(TileEntity tileEntity) {
		this.dimensionId = tileEntity.getWorld().provider.dimensionId;
		this.x = tileEntity.xCoord;
		this.y = tileEntity.yCoord;
		this.z = tileEntity.zCoord;
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

	public Block getBlock(IBlockAccess world) {
		return world.getBlock(x, y, z);
	}

	public TileEntity getTileEntity(IBlockAccess world) {
		return TileEntity.getTileEntity(world, x, y, z);
	}

    @Override
    public String toString() {
        return x + ", " + y + ", " + z + " in dimension " + dimensionId;
    }
}
