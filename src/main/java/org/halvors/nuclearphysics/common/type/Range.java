package org.halvors.nuclearphysics.common.type;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class Range {
	private World world;
	private int minX;
	private int minY;
	private int minZ;
	private int maxX;
	private int maxY;
	private int maxZ;

	public Range(final World world, final int minX, final int minY, final int minZ, final int maxX, final int maxY, final int maxZ) {
		this.world = world;
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}

	public Range(final World world, final ChunkPos pos) {
		this.world = world;
		this.minX = pos.chunkXPos * 16;
		this.minY = 0;
		this.minZ = pos.chunkZPos * 16;
		this.maxX = minX + 16;
		this.maxY = 255;
		this.maxZ = minZ + 16;
	}

	public Range(final World world, final BlockPos pos) {
		this.world = world;
		this.minX = pos.getX();
		this.minY = pos.getY();
		this.minZ = pos.getZ();
		this.maxX = pos.getX() + 1;
		this.maxY = pos.getY() + 1;
		this.maxZ = pos.getZ() + 1;
	}

	public Range(final Entity entity) {
		this(entity.getEntityWorld(), entity.getPosition());
	}

	public Range(final TileEntity tile) {
		this(tile.getWorld(), tile.getPos());
	}

	public static Range getChunkRange(final EntityPlayer player) {
		final int radius = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getViewDistance();
		final ChunkPos chunkPos = new ChunkPos(player.getPosition());

		return new Range(player.getEntityWorld(), chunkPos).expandChunks(radius);
	}

	public Range expandChunks(final int chunks) {
		this.minX -= chunks * 16;
		this.maxX += chunks * 16;
		this.minZ -= chunks * 16;
		this.maxZ += chunks * 16;

		return this;
	}

	public boolean intersects(final Range range) {
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
	public boolean equals(final Object object) {
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
