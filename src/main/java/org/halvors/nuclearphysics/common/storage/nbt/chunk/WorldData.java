package org.halvors.nuclearphysics.common.storage.nbt.chunk;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.Reference;

import java.util.HashMap;

import static org.halvors.nuclearphysics.common.storage.nbt.chunk.ChunkDataLayer.CHUNK_WIDTH;

public class WorldData {
    private final HashMap<Long, ChunkData> loadedChunks = new HashMap<>();
    private final IBlockAccess world;

    public WorldData(final IBlockAccess world) {
        this.world = world;
    }

    public int getValue(BlockPos pos) {
        final ChunkData chunkData = getChunkData(pos, false);

        if (chunkData != null) {
            return chunkData.getValue(pos.getX() & (CHUNK_WIDTH - 1), pos.getY(), pos.getZ() & (CHUNK_WIDTH - 1));
        }

        return 0;
    }

    public boolean setValue(BlockPos pos, int value) {
        final ChunkData chunkData = getChunkData(pos, value > 0);

        if (chunkData != null) {
            /*
            int prev_value = getData(x, y, z);

            RadiationMapEvent.UpdateRadiationMaterial event = new RadiationMapEvent.UpdateRadiationMaterial(this, x, y, z, prev_value, amount);

            if (MinecraftForge.EVENT_BUS.post(event)) {
                return false;
            }

            amount = event.new_value;
            */

            // Set value
            boolean hasChanged = chunkData.setValue(pos.getX() & (CHUNK_WIDTH - 1), pos.getY(), pos.getZ() & (CHUNK_WIDTH - 1), value);

            // If changed mark chunk so it saves
            if (hasChanged && world instanceof World) {
                ((World) world).getChunkFromBlockCoords(pos).setChunkModified();
            }

            return hasChanged;
        }

        return true;
    }

    public long getIndex(final int x, final int z) {
        return ChunkPos.asLong(x, z);
    }

    public ChunkData getChunkData(int x, int z, boolean init) {
        final long index = getIndex(x, z);
        ChunkData chunkData = loadedChunks.get(index);

        if (chunkData == null && init) {
            chunkData = new ChunkData();
            loadedChunks.put(index, chunkData);
        }

        return chunkData;
    }

    public ChunkData getChunkData(BlockPos pos, boolean init) {
        return getChunkData(pos.getX() >> 4, pos.getZ() >> 4, init);
    }

    public void clear() {
        loadedChunks.clear();
    }

    public void remove(final ChunkPos pos) {
        final long index = getIndex(pos.chunkXPos, pos.chunkZPos);

        if (loadedChunks.containsKey(index)) {
            final ChunkData chunk = loadedChunks.get(index);

            if (chunk != null) {
                //MinecraftForge.EVENT_BUS.post(new MapSystemEvent.RemoveChunk(this, chunk));
            }

            loadedChunks.remove(index);
        }
    }

    public void readFromNBT(final ChunkPos pos, final NBTTagCompound tag) {
        final long index = getIndex(pos.chunkXPos, pos.chunkZPos);

        // Get chunk
        ChunkData chunkData = null;

        if (loadedChunks.containsKey(index)) {
            chunkData = loadedChunks.get(index);
        }

        // Init chunk data if missing
        if (chunkData == null) {
            chunkData = new ChunkData();
            loadedChunks.put(index, chunkData);
        }

        chunkData.readFromNBT(tag);

        /*
        // Queue to be scanned to update exposure map
        if (radiationChunk != null) {
            MinecraftForge.EVENT_BUS.post(new MapSystemEvent.AddChunk(this, radiationChunk));
        }
        */
    }

    public NBTTagCompound writeToNBT(final ChunkPos pos, final NBTTagCompound tag) {
        final long index = getIndex(pos.chunkXPos, pos.chunkZPos);

        if (loadedChunks.containsKey(index)) {
            final ChunkData chunkData = loadedChunks.get(index);

            if (chunkData != null) {
                chunkData.writeToNBT(tag);
            }
        }

        return tag;
    }
}