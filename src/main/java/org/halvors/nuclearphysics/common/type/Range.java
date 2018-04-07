package org.halvors.nuclearphysics.common.type;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class Range {
	private World world;
	private int minX;
	private int minY;
	private int minZ;
	private int maxX;
	private int maxY;
	private int maxZ;

	public Range(World world, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		this.world = world;
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}

	public Range(World world, int x, int y, int z) {
		this.world = world;
		this.minX = x;
		this.minY = y;
		this.minZ = z;
		this.maxX = x + 1;
		this.maxY = y + 1;
		this.maxZ = z + 1;
	}

	public Range(World world, ChunkPos chunkPos) {
		this.world = world;
		this.minX = chunkPos.xCoord*16;
		this.minY = 0;
		this.minZ = chunkPos.zCoord*16;
		this.maxX = minX + 16;
		this.maxY = 255;
		this.maxZ = minZ + 16;
	}

	public Range(Entity entity) {
		this(entity.worldObj, (int) entity.posX, (int) entity.posY, (int) entity.posZ);
	}

	public Range(TileEntity tile) {
		this(tile.getWorld(), tile.xCoord, tile.yCoord, tile.zCoord);
	}

	public static Range getChunkRange(EntityPlayer player) {
		int radius = FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().getViewDistance();
		ChunkPos chunkPos = new ChunkPos(player);

		return new Range(player.getEntityWorld(), chunkPos).expandChunks(radius);
	}

	public Range expandChunks(int chunks) {
		this.minX -= chunks * 16;
		this.maxX += chunks * 16;
		this.minZ -= chunks * 16;
		this.maxZ += chunks * 16;

		return this;
	}

	public boolean intersects(Range range) {
		return (maxX + 1 - 1.E-05D > range.minX) &&
				(range.maxX + 1 - 1.E-05D > minX) &&
				(maxY + 1 - 1.E-05D > range.minY) &&
				(range.maxY + 1 - 1.E-05D > minY) &&
				(maxZ + 1 - 1.E-05D > range.minZ) &&
				(range.maxZ + 1 - 1.E-05D > minZ);
	}

	public World getWorld() {
		return world;
	}

	public int getMinX() {
		return minX;
	}

	public int getMinY() {
		return minY;
	}

	public int getMinZ() {
		return minZ;
	}

	public int getMaxX() {
		return maxX;
	}

	public int getMaxY() {
		return maxY;
	}

	public int getMaxZ() {
		return maxZ;
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof Range) {
			Range range = (Range) object;

			return range.minX == minX &&
					range.minY == minY &&
					range.minZ == minZ &&
					range.maxX == maxX &&
					range.maxY == maxY &&
					range.maxZ == maxZ &&
					range.world.equals(world);
		}

		return false;
	}
}
