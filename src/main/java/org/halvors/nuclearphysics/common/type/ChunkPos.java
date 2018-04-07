package org.halvors.nuclearphysics.common.type;

import net.minecraft.entity.Entity;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class ChunkPos {
    public int dimensionId;

    public int xCoord;
    public int zCoord;

    /**
     * Creates a Chunk3D object from the given x and z coordinates, as well as a dimension.
     * @param x - chunk x location
     * @param z - chunk z location
     * @param dimension - the dimension this Chunk3D is in
     */
    public ChunkPos(int x, int z, int dimension) {
        xCoord = x;
        zCoord = z;

        dimensionId = dimension;
    }

    /**
     * Creates a Chunk3D from an entity based on it's location and dimension.
     * @param entity - the entity to get the Chunk3D object from
     */
    public ChunkPos(Entity entity) {
        xCoord = ((int)entity.posX) >> 4;
        zCoord = ((int)entity.posZ) >> 4;

        dimensionId = entity.dimension;
    }

    /**
     * Whether or not this chunk exists in the given world.
     * @param world - the world to check in
     * @return if the chunk exists
     */
    public boolean exists(World world) {
        return world.getChunkProvider().chunkExists(xCoord, zCoord);
    }

    /**
     * Gets a Chunk object corresponding to this Chunk3D's coordinates.
     * @param world - the world to get the Chunk object from
     * @return the corresponding Chunk object
     */
    public Chunk getChunk(World world) {
        return world.getChunkFromChunkCoords(xCoord, zCoord);
    }

    /**
     * Returns this Chunk3D in the Minecraft-based ChunkCoordIntPair format.
     * @return this Chunk3D as a ChunkCoordIntPair
     */
    public ChunkCoordIntPair toPair() {
        return new ChunkCoordIntPair(xCoord, zCoord);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ChunkPos &&
                ((ChunkPos)obj).xCoord == xCoord &&
                ((ChunkPos)obj).zCoord == zCoord &&
                ((ChunkPos)obj).dimensionId == dimensionId;
    }

    @Override
    public int hashCode() {
        int code = 1;
        code = 31 * code + xCoord;
        code = 31 * code + zCoord;
        code = 31 * code + dimensionId;
        return code;
    }
}

