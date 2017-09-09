package org.halvors.nuclearphysics.common.utility.location;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Chunk - an integer-based way to keep track of and perform operations on chunks in a Minecraft-based environment. This also takes
 * in account the dimension the chunk is in.
 * @author aidancbrady
 *
 */
public class Chunk {
	private final int dimensionId;
	private final int x;
	private final int z;

	/**
	 * Creates a Chunk object from the given x and z coordinates, as well as a dimension.
	 * @param x - chunk x location
	 * @param z - chunk z location
	 * @param dimensionId - the dimension this Chunk is in
	 */
	public Chunk(int dimensionId, int x, int z) {
		this.dimensionId = dimensionId;
		this.x = x;
		this.z = z;
	}

	/**
	 * Creates a Chunk from a Location based on it's coordinates and dimension.
	 * @param location - the Location object to get this Chunk from
	 */
	public Chunk(Location location) {
		this.dimensionId = location.getWorld().provider.getDimension();
		this.x = location.getPos().getX() >> 4;
		this.z = location.getPos().getZ() >> 4;
	}

	/**
	 * Creates a Chunk from an entity based on it's location and dimension.
	 * @param entity - the entity to get the Chunk object from
	 */
	public Chunk(Entity entity) {
		this(new Location(entity));
	}

	public Chunk(TileEntity tileEntity) {
		this(new Location(tileEntity));
	}

	/**
	 * Whether or not this chunk exists in the given world.
	 * @param world - the world to check in
	 * @return if the chunk exists
	 */
	public boolean exists(World world) {
		return world.getChunkProvider().getLoadedChunk(x, z) != null;
	}

	/**
	 * Gets a Chunk object corresponding to this Chunk's coordinates.
	 * @param world - the world to get the Chunk object from
	 * @return the corresponding Chunk object
	 */
	public net.minecraft.world.chunk.Chunk getChunk(World world) {
		return world.getChunkFromChunkCoords(x, z);
	}

	public int getDimensionId() {
		return dimensionId;
	}

	public int getX() {
		return x;
	}

	public int getZ() {
		return z;
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof Chunk) {
			Chunk chunk = (Chunk) object;

			return chunk.dimensionId == dimensionId &&
					chunk.x == x &&
					chunk.z == z;
		}

		return false;
	}
}