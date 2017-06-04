package org.halvors.quantum.common.util.location;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.halvors.quantum.common.tile.TileEntity;

public class Range {
	private int dimensionId;
	private int minX;
	private int minY;
	private int minZ;
	private int maxX;
	private int maxY;
	private int maxZ;

	public Range(int minX, int minY, int minZ, int maxX, int maxY, int maxZ, int dimensionId) {
		this.dimensionId = dimensionId;
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}

	public Range(Chunk chunk) {
		this.dimensionId = chunk.getDimensionId();
		this.minX = chunk.getX() * 16;
		this.minY = 0;
		this.minZ = chunk.getZ() * 16;
		this.maxX = minX + 16;
		this.maxY = 255;
		this.maxZ = minZ + 16;
	}

	public Range(Location location) {
		this.dimensionId = location.getDimensionId();
		this.minX = location.getX();
		this.minY = location.getY();
		this.minZ = location.getZ();
		this.maxX = location.getX() + 1;
		this.maxY = location.getY() + 1;
		this.maxZ = location.getZ() + 1;
	}

	public Range(Entity entity) {
		this(new Location(entity));
	}

	public Range(TileEntity tileEntity) {
		this(new Location(tileEntity));
	}

	public static Range getChunkRange(EntityPlayer player) {
		int radius = FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().getViewDistance();

		return new Range(new Chunk(player)).expandChunks(radius);
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

	public int getDimensionId() {
		return dimensionId;
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
					range.dimensionId == dimensionId;
		}

		return false;
	}
}
